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
package com.apress.prospring7.classic.nine;

import com.apress.prospring7.classic.nine.config.DataJpaCfg;
import com.apress.prospring7.classic.nine.services.AlbumService;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author iulianacosmina on 05/12/2025
 */
@Testcontainers
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringJUnitConfig(classes = {AlbumServiceTest.TestContainersConfig.class})
public class AlbumServiceTest extends TestContainersBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumServiceTest.class);

    @Autowired
    AlbumService albumService;

    @Test
    public void testFindWithReleaseDateGreaterThan(){
        var albums = albumService
                .findWithReleaseDateGreaterThan(LocalDate.of(2010, 1, 1))
                .peek(s -> LOGGER.info(s.toString())).toList();
        assertEquals(2, albums.size());
    }

    @Test
    public void testFindByTitle(){
        var albums = albumService
                .findByTitle("The")
                .peek(s -> LOGGER.info(s.toString())).toList();
        assertEquals(1, albums.size());
    }

    // needed because these tests need different containers
    @Configuration
    @Import(DataJpaCfg.class)
    public static class TestContainersConfig {
        @Primary
        @Bean
        public Properties jpaProperties() {
            final var jpaProps = new Properties();
            jpaProps.put(Environment.FORMAT_SQL, true);
            jpaProps.put(Environment.USE_SQL_COMMENTS, true);
            jpaProps.put(Environment.SHOW_SQL, true);
            jpaProps.put(Environment.HIGHLIGHT_SQL, true);
            return jpaProps;
        }
    }
}
