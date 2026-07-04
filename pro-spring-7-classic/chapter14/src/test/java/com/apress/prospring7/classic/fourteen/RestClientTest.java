/*
Freeware License, some rights reserved

Copyright (c) 2026 Iuliana Cosmina

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
package com.apress.prospring7.classic.fourteen;

import com.apress.prospring7.classic.fourteen.entities.Singer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

///
/// @author iulianacosmina on 31/01/2026
///
@Disabled("Start Apache Tomcat and deploy the app first, then comment this line, " +
        "then run each test manually and notice the result!")
public class RestClientTest {

 final static Logger LOGGER = LoggerFactory.getLogger(RestClientTest.class);

 RestClient restClient = RestClient.builder()
         .baseUrl("http://localhost:8080/ch14")
         .configureMessageConverters(cb -> {
          var conv = new JacksonJsonHttpMessageConverter();
          conv.setSupportedMediaTypes(List.of(MediaType.parseMediaType("application/json;charset=UTF-8")));
          cb.addCustomConverter(conv);
         })
         .build();

 @Test
 public void testFindAll() {
  LOGGER.info("--> Testing retrieve all singers");
  var singers = restClient.get().uri("/singers").retrieve().body(Singer[].class);
  assertNotNull(singers);
  assertTrue( singers.length > 0);
  Arrays.stream(singers).forEach(s -> LOGGER.info(s.toString()));
 }

@Test
 public void testFindAllWithMethod() {
  LOGGER.info("--> Testing retrieve all singers");
  var singers = restClient.method(HttpMethod.GET).uri("/singers").exchange((clientRequest, clientResponse) -> {
   LOGGER.debug("Request submitted {}", clientRequest);
   Assertions.assertEquals(HttpStatus.OK, clientResponse.getStatusCode());
   return new String(clientResponse.getBody().readAllBytes());
  });

  LOGGER.info("Response: {}" , singers);
 }


 @Test
 public void testFindById() {
  LOGGER.info("--> Testing retrieve a singer by id : 1");
  var singer = restClient.get().uri("/singers/1").retrieve().toEntity(Singer.class);
  assertNotNull(singer);
  LOGGER.info(singer.toString());
 }


 @Test
 public void testCreate() {
  LOGGER.info("--> Testing create singer");
  Singer singerNew = new Singer();
  singerNew.setFirstName("TEST");
  singerNew.setLastName("Singer");
  singerNew.setBirthDate(LocalDate.now());
  singerNew = restClient.post().uri("/singers/").body(singerNew).retrieve().body(Singer.class);

  LOGGER.info("Singer created successfully: {}" , singerNew);
 }


 @Test
 public void testCreateWithMethod() {
  LOGGER.info("--> Testing create singer");
  Singer singerNew = new Singer();
  singerNew.setFirstName("TEST2");
  singerNew.setLastName("Singer2");
  singerNew.setBirthDate(LocalDate.now());
  ResponseEntity<Singer> created = restClient.method(HttpMethod.POST).uri("/singers/")
          .body(singerNew).retrieve().toEntity(Singer.class);
  assertEquals(HttpStatus.CREATED, created.getStatusCode());

  var singerCreated = created.getBody();
  assertNotNull(singerCreated);

  LOGGER.info("Singer created successfully: {}", singerCreated);
 }


 @Test
 public void testDelete() {
  LOGGER.info("--> Testing delete singer by id : 57"); // TODO check your database and select an ID from there
  var singers = restClient.get().uri("/singers").retrieve().body(Singer[].class);
  assertNotNull(singers);
  var initialCount = singers.length;

  restClient.delete().uri("/singers/57").retrieve().toBodilessEntity();

  singers = restClient.get().uri("/singers").retrieve().body(Singer[].class);
  assertNotNull(singers);
  var afterDeleteCount = singers.length;
  assertEquals(1, (initialCount - afterDeleteCount));
 }

 @Test
 public void testUpdate() {
  LOGGER.info("--> Testing update singer by id : 1");
  var singer = restClient.get().uri("/singers/1").retrieve().body(Singer.class);
  assertNotNull(singer);
  singer.setFirstName("John Clayton");

  restClient.put().uri("/singers/1").body(singer).retrieve().body(Singer.class);
  LOGGER.info("Singer update successfully: " + singer);
 }
}
