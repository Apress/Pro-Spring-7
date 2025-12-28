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
 package com.apress.prospring7.boot.nine;

 import com.apress.prospring7.boot.nine.service.SingerService;
 import org.junit.jupiter.api.Test;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.test.context.ActiveProfiles;

 import static org.junit.jupiter.api.Assertions.assertEquals;
 import static org.junit.jupiter.api.Assertions.assertNotNull;

 /**
  * @author iulianacosmina on 15/12/2025
  */
 @ActiveProfiles("test")
 @SpringBootTest(classes = {Chapter9Application.class})
 public class Chapter9ApplicationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chapter9ApplicationTest.class);

    @Autowired
    SingerService singerService;

    @Test
    public void testFindAll(){
      var singers = singerService.findAll().peek(s -> LOGGER.info(s.toString())).toList();
      assertEquals(3, singers.size());
    }

    @Test
    public void testFindAllWithAlbums(){
     var singers = singerService.findAllWithAlbums()
           .peek(s -> {
            LOGGER.info(s.toString());
            if (s.getAlbums() != null) {
             s.getAlbums().forEach(a -> LOGGER.info("\t" + a.toString()));
            }
           }).toList();
     assertEquals(3, singers.size());
    }

    @Test
    public void testUpdateName() {
     singerService.updateFirstName("John Clayton", 1L);

     var singer = singerService.findById(1L).orElse(null);
     //making sure such singer exists
     assertNotNull(singer);
     //making sure we got expected record
     assertEquals("John Clayton", singer.getFirstName());
    }

 }
