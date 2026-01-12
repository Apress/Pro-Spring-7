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
package com.apress.prospring7.classic.ten;

import com.apress.prospring7.classic.ten.config.R2dbcConfiguration;
import com.apress.prospring7.classic.ten.document.Singer;
import com.apress.prospring7.classic.ten.repos.SingerRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mariadb.MariaDBContainer;
import org.testcontainers.utility.MountableFile;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

///
/// @author iulianacosmina on 30/12/2025
///
@Testcontainers
@SpringJUnitConfig(classes = {R2dbcConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class R2dbcDataRepoTest {

    public static Singer NICK = new Singer(1L, "Nick", "Drake", LocalDate.of(1948,6, 19));

    @Container
    static MariaDBContainer mariaDB = new MariaDBContainer("mariadb:latest")
            .withCopyFileToContainer(MountableFile.forClasspathResource("testcontainers/create-schema.sql"), "/docker-entrypoint-initdb.d/init.sql");


    @DynamicPropertySource // this does the magic
    static void setUp(DynamicPropertyRegistry registry) {
        registry.add("r2dbc.database", mariaDB::getDatabaseName);
        registry.add("r2dbc.host", () -> "localhost");
        registry.add("r2dbc.username", mariaDB::getUsername);
        registry.add("r2dbc.password", mariaDB::getPassword);
        registry.add("r2dbc.port", () -> {
            // needed because mariaDB.getExposedPorts() returns 3306, not the mapped port
            var url = mariaDB.getJdbcUrl();
            var idx = url.indexOf("localhost:");
            return url.substring(idx + 10,  url.lastIndexOf("/"));
        });
    }

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
