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
package com.apress.prospring7.classic.six;

import com.apress.prospring7.classic.six.base.config.HibernateConfig;
import com.apress.prospring7.classic.six.base.dao.SingerDao;
import com.apress.prospring7.classic.six.base.entities.Album;
import com.apress.prospring7.classic.six.base.entities.Singer;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.apress.prospring7.classic.six.base.HibernateDemoV2.listSingersWithAlbum;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author iulianacosmina on 30/09/2025
 */
@Testcontainers
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringJUnitConfig(classes = {HibernateConfig.class})
public class HibernateTcOneTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTcOneTest.class);

    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:latest");

    @DynamicPropertySource
    static void setUp(DynamicPropertyRegistry registry) {
        registry.add("jdbc.driverClassName", mariaDB::getDriverClassName);
        registry.add("jdbc.url", mariaDB::getJdbcUrl);
        registry.add("jdbc.username", mariaDB::getUsername);
        registry.add("jdbc.password", mariaDB::getPassword);
    }

    @Autowired
    SingerDao singerDao;

    @Test
    @DisplayName("should return all singers")
    void testFindAll(){
        final var singers = singerDao.findAll();
        assertEquals(3, singers.size());
        singers.forEach(singer -> LOGGER.info(singer.toString()));
    }

    @Test
    @DisplayName("should return singer by id")
    void testFindById(){
        final var singer = singerDao.findById(2L);
        assertEquals("Ben", singer.getFirstName());
        LOGGER.info(singer.toString());
    }

    @Test
    @DisplayName("should insert a singer with associations")
    @Sql(statements =  { // avoid dirtying up the test context
            "delete from ALBUM where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')",
            "delete from SINGER_INSTRUMENT where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')",
            "delete from SINGER where FIRST_NAME = 'BB'"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testInsertSinger(){
        var singer = new Singer();
        singer.setFirstName("BB");
        singer.setLastName("King");
        singer.setBirthDate(LocalDate.of(1940, 8, 16));

        var album = new Album();
        album.setTitle("My Kind of Blues");
        album.setReleaseDate(LocalDate.of(1961, 7, 18));
        singer.addAlbum(album);

        album = new Album();
        album.setTitle("A Heart Full of Blues");
        album.setReleaseDate(LocalDate.of(1962, 3, 20));
        singer.addAlbum(album);
        singerDao.save(singer);

        assertNotNull(singer.getId());

        var singers = singerDao.findAllWithAlbum();
        assertEquals(4, singers.size());
        listSingersWithAlbum(singers);
    }
}
