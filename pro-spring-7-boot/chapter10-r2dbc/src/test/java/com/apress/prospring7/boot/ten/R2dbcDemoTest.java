/*
Freeware License, some rights reserved

Copyright (c) 2025 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy
of this software and associated documentation files (the "Software"),
to work with the Software within the limits of freeware distribution and fair use.
This includes the rights to use, copy, and modify the Software for personal use.
Users are also allowed and encouraged to submit corrections and modifications
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for
commercial use in any way, or for a user's educational materials such as books
or blog articles without prior permission from the copyright holder.

The above copyright notice and this permission notice need to be included
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring7.boot.ten;

import com.apress.prospring7.boot.ten.document.Singer;
import com.apress.prospring7.boot.ten.repos.SingerRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mariadb.MariaDBContainer;
import org.testcontainers.utility.MountableFile;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

///
/// @author iulianacosmina on 29/12/2025
///
@ActiveProfiles("test")
@DataR2dbcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class R2dbcDemoTest extends TestContainerBase {

    @Autowired
    SingerRepository singerRepo;

    @Order(1)
    @Test
    void testCount() {
        var result = singerRepo.count();
        assertNotNull(result);

        result.log()
                .as(StepVerifier::create)
                .consumeNextWith(p -> assertEquals(7L, p))
                .verifyComplete();
    }

    @Order(2)
    @Test
    void testFindByFullName(){
        singerRepo.findByFullName("Nick", "Drake").log()
                .as(StepVerifier::create)
                .expectNext(NICK)
                .verifyComplete();
    }

    @Order(3)
    @Test
    public void testCreateSinger() {
        singerRepo.save(new Singer(null, "B.B.", "King", LocalDate.of(1925, 9,16)))
                .log()
                .as(StepVerifier::create)
                .assertNext(s -> assertNotNull(s.id()))
                .verifyComplete();
    }

    @Order(4)
    @Test
    public void testFailToCreateSinger() {
        singerRepo.save(new Singer(null, "B.B.", null,
                        LocalDate.of(1925, 9,16))).log()
                .as(StepVerifier::create)
                .verifyError(TransientDataAccessResourceException.class);
    }

    @Order(5)
    @Test
    public void testFindByFistNameAndLastNameNoResult() {
        singerRepo.findByFullName("Gigi", "Pedala")
                .log()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Order(6)
    @Test
    public void testDeleteSinger() {
        singerRepo.deleteById(4L)
                .log()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

}
