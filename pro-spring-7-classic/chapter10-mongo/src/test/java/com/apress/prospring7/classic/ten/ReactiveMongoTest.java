/*
Freeware License, some rights reserved

Copyright (c) 2026 Iuliana Cosmina

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

import com.apress.prospring7.classic.ten.config.ReactiveMongoCfg;
import com.apress.prospring7.classic.ten.repos.SingerRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

///
/// @author iulianacosmina on 03/01/2026
///
@SpringJUnitConfig(classes = {ReactiveMongoCfg.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReactiveMongoTest extends ReactiveTestBase {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    SingerRepository singerRepo;

    @Order(1)
    @Test
    void testSave(){
        ctx.getBeanDefinitionNames();
        singerRepo.save(NICK).as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        singerRepo.save(JOHN).as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        try {
            Thread.sleep(100);
        } catch (InterruptedException _) {}

    }

    @Order(2)
    @Test
    void testCount() {
        var result = singerRepo.count();
        assertNotNull(result);

        result.log()
                .as(StepVerifier::create)
                .consumeNextWith(p -> assertEquals(2L, p))
                .verifyComplete();
    }

    @Order(3)
    @Test
    void testFindByFullName(){
        singerRepo.findByFirstName("Nick").log()
                .as(StepVerifier::create)
                .consumeNextWith(p -> assertEquals(NICK, p))
                .verifyComplete();
    }

    @Order(4)
    @Test
    public void testFindByFistNameAndLastNameNoResult() {
        singerRepo.findByPositionedParams("Gigi", "Pedala")
                .log()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Order(5)
    @Test
    public void testDelete() {
        var singer = singerRepo.findByPositionedParams("Nick", "Drake").blockLast();

        assert singer != null;
        singerRepo.deleteById(singer.id())
                .log()
                .as(StepVerifier::create)
                .verifyComplete();
    }

}
