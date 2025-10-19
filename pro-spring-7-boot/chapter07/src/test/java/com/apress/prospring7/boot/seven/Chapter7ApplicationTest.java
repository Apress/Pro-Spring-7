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
package com.apress.prospring7.boot.seven;

import com.apress.prospring7.boot.seven.entities.Album;
import com.apress.prospring7.boot.seven.entities.Singer;
import com.apress.prospring7.boot.seven.service.SingerService;
import com.apress.prospring7.boot.seven.service.SingerSummaryService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author iulianacosmina on 19/10/2025
 */
@ActiveProfiles("test")
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql({ "classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql" })
@SpringBootTest(classes = Chapter7Application.class)
public class Chapter7ApplicationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Chapter7ApplicationTest.class);

    @Autowired
    SingerService singerService;

    @Test
    @DisplayName("should return all singers")
    void testFindAllWithJdbcTemplate() {
        var singers = singerService.findAll().peek(singer -> LOGGER.info(singer.toString())).toList();
        assertEquals(3, singers.size());
    }

    @Test
    @DisplayName("should return all singers with albums")
    void testFindAllWithAlbum(){
        var singers = singerService.findAllWithAlbum().peek(
                s -> {
                    LOGGER.info(s.toString());
                    if (s.getAlbums() != null) {
                        s.getAlbums().forEach(a -> LOGGER.info("\tAlbum: {}" , a.toString()));
                    }
                    if (s.getInstruments() != null) {
                        s.getInstruments().forEach(i -> LOGGER.info("\tInstrument: {}" , i.getInstrumentId()));
                    }
                }).toList();
        assertEquals(3, singers.size());
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
}
