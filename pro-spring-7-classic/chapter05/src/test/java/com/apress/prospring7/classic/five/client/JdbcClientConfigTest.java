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
package com.apress.prospring7.classic.five.client;

import com.apress.prospring7.classic.five.config.EmbeddedJdbcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author iulianacosmina on 02/11/2025
 */
public class JdbcClientConfigTest {

    @Test
    void testJdbcClientWithH2Db() {
        try(final var ctx = new AnnotationConfigApplicationContext(TestDbCfg.class)) {
            final var jdbcClient = ctx.getBean("jdbcClient", JdbcClient.class);
            assertNotNull(jdbcClient);

            var singerDao = ctx.getBean("singerDao", SingerDao.class);

            assertEquals("John Mayer", singerDao.findNameById(1L));
            assertEquals(3, singerDao.findAll().size());

            // it does not work when records have child records in another table
            //assertNotNull(singerDao.findByFirstName("Ben"));
        }
    }

    @Import(EmbeddedJdbcConfig.class)
    @Configuration
    static class TestDbCfg {

        @Autowired
        DataSource dataSource;

        @Bean
        JdbcClient jdbcClient(){
            return JdbcClient.create(dataSource);
        }

        @Bean
        SingerDao singerDao(){
            final var dao = new JdbcSingerDao();
            dao.setJdbcClient(jdbcClient());
            return dao;
        }
    }


}
