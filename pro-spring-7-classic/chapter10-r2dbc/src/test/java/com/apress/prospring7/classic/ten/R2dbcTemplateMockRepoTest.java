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

import com.apress.prospring7.classic.ten.document.Singer;
import com.apress.prospring7.classic.ten.repos.SingerTemplateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

///
/// @author iulianacosmina on 30/12/2025
///
public class R2dbcTemplateMockRepoTest {

    public static Singer NICK = new Singer(1L, "Nick", "Drake", LocalDate.of(1948,6, 19));
    public static Singer JOHN = new Singer(2L, "John", "Mayer", LocalDate.of(1977, 10,16));
    private static Singer BB = new Singer(3L, "B.B.", "King", LocalDate.of(1925, 9,16));

    private static R2dbcEntityTemplate mockTemplate;
    private static SingerTemplateRepository singerRepo;

    @BeforeEach
    void setUp() {
        // RETURNS_DEEP_STUBS is used for mock sql, filter, all together in a stubbing statement.
        mockTemplate = mock(R2dbcEntityTemplate.class, RETURNS_DEEP_STUBS);
        singerRepo = new SingerTemplateRepository(mockTemplate);
    }

    @Test
    void testCount() {
        given(mockTemplate.count(any(Query.class), eq(Singer.class))).willReturn(Mono.just(4L));

        var result = singerRepo.count();

        assertNotNull(result);

        result.log()
                .as(StepVerifier:: create)
                .consumeNextWith(p -> assertEquals(4L, p))
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        given(mockTemplate.select(Singer.class).all()).willReturn(Flux.just(NICK,JOHN));

        var result = singerRepo.findAll();

        assertNotNull(result);

        result.log()
                .as(StepVerifier:: create)
                .expectNext(NICK)
                .expectNext(JOHN)
                .verifyComplete();
    }

    @Test
    void testFindByFullName(){
        given(mockTemplate.select(Singer.class)
                .matching(any(Query.class))
                .all())
                .willReturn(Flux.just(NICK));

        var result = singerRepo.findByFullName("Nick", "Drake");

        assertNotNull(result);

        result.log()
                .as(StepVerifier:: create)
                .expectNext(NICK)
                .verifyComplete();
    }

    @Test
    public void testCreateSinger() {
        given(mockTemplate.insert(Singer.class)
                .using(any(Singer.class)))
                .willReturn(Mono.just(BB));

        singerRepo.save(new Singer(null, "B.B.", "King", LocalDate.of(1925, 9,16)))
                .log()
                .as(StepVerifier:: create)
                .assertNext(Assertions::assertNotNull)
                .verifyComplete();
    }

    @Test
    public void testFailToCreateSinger() {
        given(mockTemplate.insert(Singer.class)
                .using(any(Singer.class)))
                .willThrow(TransientDataAccessResourceException.class);

        assertThrows(TransientDataAccessResourceException.class, () ->
        singerRepo.save(new Singer(null, "B.B.", null, LocalDate.of(1925, 9,16))));
    }


}
