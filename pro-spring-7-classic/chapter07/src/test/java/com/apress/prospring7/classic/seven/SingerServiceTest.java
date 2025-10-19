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
package com.apress.prospring7.classic.seven;

import com.apress.prospring7.classic.seven.config.JpaConfig;
import com.apress.prospring7.classic.seven.entities.Album;
import com.apress.prospring7.classic.seven.entities.Singer;
import com.apress.prospring7.classic.seven.service.SingerService;
import com.apress.prospring7.classic.seven.service.SingerSummaryService;
import org.hibernate.cfg.Environment;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author iulianacosmina on 13/10/2025
 */
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringJUnitConfig(classes = {SingerServiceTest.TestContainersConfig.class})
public class SingerServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingerServiceTest.class);

    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:latest");

    @DynamicPropertySource // this does the magic
    static void setUp(DynamicPropertyRegistry registry) {
        registry.add("jdbc.driverClassName", mariaDB::getDriverClassName);
        registry.add("jdbc.url", mariaDB::getJdbcUrl);
        registry.add("jdbc.username", mariaDB::getUsername);
        registry.add("jdbc.password", mariaDB::getPassword);
    }

    @Autowired
    @Qualifier("jpaSingerService")
    SingerService singerService;

    @Test
    @DisplayName("should return all singers")
    void testFindAll(){
        var singers = singerService.findAll().toList();
        assertEquals(3, singers.size());
        singers.forEach(singer -> LOGGER.info(singer.toString()));
    }

    @Test
    @DisplayName("should return all singers with albums")
    void testFindAllWithAlbum(){
        var singers = singerService.findAllWithAlbum().toList();
        assertEquals(3, singers.size());
        singers.forEach(s -> {
            LOGGER.info(s.toString());
            if (s.getAlbums() != null) {
                s.getAlbums().forEach(a -> LOGGER.info("\tAlbum:{}", a.toString()));
            }
            if (s.getInstruments() != null) {
                s.getInstruments().forEach(i -> LOGGER.info("\tInstrument: {}", i.getInstrumentId()));
            }
        });
    }

    @Autowired
    SingerSummaryService singerSummaryService;

    @Test
    @DisplayName("should return all singers and their most recent album as records")
    void testFindAllWithAlbumAsRecords(){
        var singers =
                singerSummaryService.findAllAsRecord()
                        .peek(s -> LOGGER.info(s.toString()))
                        .toList();
        assertEquals(2, singers.size());
    }

    @Test
    @DisplayName("should return all singers and their most recent album as POJOs")
    void testFindAllAsPojos(){
        var singers =
                singerSummaryService.findAll()
                        .peek(s -> LOGGER.info(s.toString())).toList();
        assertEquals(2, singers.size());
    }


    @Test
    @DisplayName("should insert a singer with associations")
    @Sql(statements =  { // avoid dirtying up the test context
            "delete from ALBUM where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')",
            "delete from SINGER_INSTRUMENT where SINGER_ID = (select ID from SINGER where FIRST_NAME = 'BB')",
            "delete from SINGER where FIRST_NAME = 'BB'"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testInsert(){
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
        singerService.save(singer);

        assertNotNull(singer.getId());

        var singers = findAndLogAllSingers();
        assertEquals(4, singers.size());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {"classpath:testcontainers/add-nina.sql"},
                    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = {"classpath:testcontainers/remove-nina.sql"},
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @DisplayName("should update a singer")
    void testUpdate() {
        var singer = singerService.findById(5L).orElse(null);
        //making sure such singer exists
        assertNotNull(singer);
        //making sure we got expected singer
        assertEquals("Simone", singer.getLastName());
        //retrieve the album
        var album = singer.getAlbums().stream().filter(
                a -> a.getTitle().equals("I Put a Spell on You")).findFirst().orElse(null);
        assertNotNull(album);

        singer.setFirstName("Eunice Kathleen");
        singer.setLastName("Waymon");
        singer.removeAlbum(album);
        int version =  singer.getVersion();

        singerService.save(singer);

        var nina =  singerService.findById(5L).orElse(null);
        assertAll( "nina was updated" ,
                () -> {
                    assertNotNull(nina);
                    Assertions.assertEquals(version +1, nina.getVersion());
                }
        );
    }

    @Test
    public void testUpdateAlbumSet() {
        var singer = singerService.findById(1L).orElse(null);
        //making sure such singer exists
        assertNotNull(singer);
        //making sure we got expected record
        assertEquals("Mayer", singer.getLastName());
        //retrieve the album
        var album = singer.getAlbums().stream().filter(a -> a.getTitle().equals("Battle Studies")).findAny().orElse(null);

        singer.setFirstName("John Clayton");
        singer.removeAlbum(album);
        singerService.save(singer);

        var singers = findAndLogAllSingers();
        assertEquals(3, singers.size());
    }

    @Test
    @Sql(scripts = {"classpath:testcontainers/add-chuck.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("should delete a singer")
    public void testDelete() {
        var singer = singerService.findById(6L).orElse(null);
        //making sure such singer exists
        assertNotNull(singer);
        singerService.delete(singer);

        var deleted = singerService.findById(6L);
        assertTrue(deleted.isEmpty());
    }

    @Test
    public void testFindAllByNativeQuery() {
        var singers = singerService.findAllByNativeQuery().toList();
        assertEquals(3, singers.size());
    }

    @Sql({ "classpath:testcontainers/stored-function.sql" })
    @Test
    void testFindFirstNameById () {
        var res = singerService.findFirstNameById(1L);
        assertEquals("John", res);
    }

    @Disabled("")
    @Sql({ "classpath:testcontainers/stored-procedure.sql" })
    @Test
    void testFindFirstNameByIdUsingProc () {
        var res = singerService.findFirstNameByIdUsingProc(1L);
        assertEquals("John", res);
    }

    private @NotNull List<Singer> findAndLogAllSingers() {
        return singerService.findAllWithAlbum().peek(
                s -> {
                    LOGGER.info(s.toString());
                    if (s.getAlbums() != null) {
                        s.getAlbums().forEach(a -> LOGGER.info("\tAlbum:{}", a.toString()));
                    }
                    if (s.getInstruments() != null) {
                        s.getInstruments().forEach(i -> LOGGER.info("\tInstrument: {}", i.getInstrumentId()));
                    }
                }).toList();
    }

    @Configuration
    @Import(JpaConfig.class)
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
