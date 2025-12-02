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
package com.apress.prospring7.classic.eight.services;

import com.apress.prospring7.classic.eight.config.ProgrammaticTransactionCfg;
import com.apress.prospring7.classic.eight.programmatic.ProgrammaticService;
import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author iulianacosmina on 24/11/2025
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringJUnitConfig(classes = {ProgrammaticServiceTest.TestContainersConfig.class})
public class ProgrammaticServiceTest extends TestContainersBase {

    @Autowired
    ProgrammaticService service;

    @Test
    @DisplayName("should count singers")
    void testCount() {
        var count = service.countSingers();

        assertEquals(3, count );
    }

    @Configuration
    @Import(ProgrammaticTransactionCfg.class)
    public static class TestContainersConfig {

        @Primary
        @Bean
        public Properties jpaProperties() {
            final var jpaProps = new Properties();
            jpaProps.put(Environment.FORMAT_SQL, true);
            jpaProps.put(Environment.USE_SQL_COMMENTS, true);
            jpaProps.put(Environment.SHOW_SQL, true);
            jpaProps.put(Environment.HIGHLIGHT_SQL, true);
            jpaProps.put(Environment.STATEMENT_BATCH_SIZE, 30); // needed because it is referenced in AlbumRepoImpl
            return jpaProps;
        }
    }

}
