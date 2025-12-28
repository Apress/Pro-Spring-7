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
package com.apress.prospring7.classic.five.repo;

import com.apress.prospring7.classic.five.config.EmbeddedJdbcConfig;
import com.apress.prospring7.classic.five.plain.records.Album;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.apress.prospring7.classic.five.plain.records.Singer;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

///
/// @author iulianacosmina on 15/09/2025
///
public class RepoBeanTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepoBeanTest.class);

    @Test
    public void testFindAllWithMappingSqlQuery() {
        try(final var ctx = new AnnotationConfigApplicationContext(EmbeddedJdbcConfig.class, SingerJdbcRepo.class)) {
            final var singerRepo = ctx.getBean("singerRepo", SingerRepo.class);
            assertNotNull(singerRepo);

            List<Singer> singers = singerRepo.findAll();
            assertEquals(3, singers.size());
            singers.forEach(singer -> LOGGER.info(singer.toString()));
        }
    }

    @Test
    public void testFindByNameWithMappingSqlQuery() {
        try(final var ctx = new AnnotationConfigApplicationContext(EmbeddedJdbcConfig.class, SingerJdbcRepo.class)) {
            final var singerRepo = ctx.getBean("singerRepo", SingerRepo.class);
            assertNotNull(singerRepo);

            final var singers = singerRepo.findByFirstName("Ben");
            assertEquals(1, singers.size());
            LOGGER.info("Result: {}", singers.getFirst());
        }
    }

    @Test
    public void testFindNameByIdWithMappingSqlQuery() {
        try(final var ctx = new AnnotationConfigApplicationContext(EmbeddedJdbcConfig.class, SingerJdbcRepo.class)) {
            final var singerRepo = ctx.getBean("singerRepo", SingerRepo.class);
            assertNotNull(singerRepo);

            final var singer = singerRepo.findNameById(1L);
            assertTrue(singer.isPresent());
            LOGGER.info("Result: {}", singer.get());
        }
    }

    @Test
    public void testUpdateWithSqlUpdate() {
        try(final var ctx = new AnnotationConfigApplicationContext(EmbeddedJdbcConfig.class, SingerJdbcRepo.class)) {
            final var singerRepo = ctx.getBean("singerRepo", SingerRepo.class);
            assertNotNull(singerRepo);

            final var singer = new Singer(1L, "John Clayton", "Mayer",
                    LocalDate.of(1977,10, 16), Set.of());
            singerRepo.update(singer);

            final var singers = singerRepo.findByFirstName("John Clayton");
            assertEquals(1, singers.size());
            LOGGER.info("Result: {}", singers.getFirst());
        }
    }

    @Test
    public void testInsertWithSqlUpdate() {
        try(final var ctx = new AnnotationConfigApplicationContext(EmbeddedJdbcConfig.class, SingerJdbcRepo.class)) {
            final var singerRepo = ctx.getBean("singerRepo", SingerRepo.class);
            assertNotNull(singerRepo);

            final var singer = new Singer(null,"Ed","Sheeran",
                    LocalDate.of(1991,1, 17), Set.of());
            singerRepo.insert(singer);

            final var singers = singerRepo.findByFirstName("Ed");
            assertEquals(1, singers.size());
            LOGGER.info("Result: {}", singers.getFirst());
        }
    }

    @Test
    public void testInsertAlbumsWithBatchSqlUpdate() {
        try(final var ctx = new AnnotationConfigApplicationContext(EmbeddedJdbcConfig.class, SingerJdbcRepo.class)) {
            final var singerRepo = ctx.getBean("singerRepo", SingerRepo.class);
            assertNotNull(singerRepo);

            var singer = new Singer(null,"BB","King",
                    LocalDate.of(1940,9, 17), new HashSet<>());

            var album = new Album(null, null,"My Kind of Blues",
                    LocalDate.of(1961,8, 17));
            singer.albums().add(album);

            album = new Album(null, null, "A Heart Full of Blues",
                    LocalDate.of(1962,4, 20));
            singer.albums().add(album);

            singerRepo.insertWithAlbum(singer);

            var singers = singerRepo.findAllWithAlbums();
            assertEquals(4, singers.size());
            singers.forEach(s -> LOGGER.info(s.toString()));
        }
    }
}
