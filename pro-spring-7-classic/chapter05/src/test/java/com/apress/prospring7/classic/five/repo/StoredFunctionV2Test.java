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

import com.apress.prospring7.classic.five.config.C3p0DataSourceCfg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author iulianacosmina on 20/09/2025
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringJUnitConfig(classes = { C3p0DataSourceCfg.class, SingerJdbcRepo.class})
public class StoredFunctionV2Test {

    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:latest");

    @DynamicPropertySource // this does the magic
    static void setUp(DynamicPropertyRegistry registry) {
        registry.add("driverClassName", mariaDB::getDriverClassName);
        registry.add("url", mariaDB::getJdbcUrl);
        registry.add("username", mariaDB::getUsername);
        registry.add("password", mariaDB::getPassword);
    }

    @Autowired
    SingerRepo singerRepo;

    @Test
    void testFindAllQuery(){
        var singers = singerRepo.findAll();
        assertEquals(3, singers.size());
    }

    @Test
    @Sql({ "classpath:testcontainers/original-stored-function.sql" }) // this does not work!
    // Testcontainers simply can't support all SQL dialects to 100%.
    //@Sql({ "classpath:testcontainers/stored-function.sql" }) // different SQL syntax
    void testStoredFunction(){
        var firstName = singerRepo.findFirstNameById(2L).orElse(null);
        assertEquals("Ben", firstName);
    }
}
