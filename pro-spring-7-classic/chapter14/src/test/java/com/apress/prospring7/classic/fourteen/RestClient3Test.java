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

import com.apress.prospring7.classic.fourteen.deprecated.RestClientTest;
import com.apress.prospring7.classic.fourteen.entities.Singer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

///
/// @author iulianacosmina on 31/01/2026
///
@Disabled("Start Apache Tomcat and deploy the app first, then comment this line," +
        " then run each test manually and notice the result!")
public class RestClient3Test {
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
    public void testPositiveFindById() {
        LOGGER.info("--> Testing retrieve a singer by id : 1");

        ResponseEntity<Singer> response = restClient.method(HttpMethod.GET).uri("/singers3/1").retrieve().toEntity(Singer.class);
        assertAll("testPositiveFindById",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(Singer.class, response.getBody().getClass())
        );

    }

    @Test
    public void testNegativeFindById() throws URISyntaxException {
        LOGGER.info("--> Testing retrieve a singer by id : 99");

        assertThrowsExactly(HttpClientErrorException.NotFound.class, () -> restClient.method(HttpMethod.GET).uri("/singers3/99").retrieve().toEntity(Singer.class));
    }

    @Test
    public void testNegativeCreate() {
        LOGGER.info("--> Testing create singer");
        Singer singerNew = new Singer();
        singerNew.setFirstName("Ben");
        singerNew.setLastName("Barnes");
        singerNew.setBirthDate(LocalDate.now());

        assertThrowsExactly(HttpClientErrorException.BadRequest.class, () -> restClient.post().uri("/singers3/").body(singerNew).retrieve().body(Singer.class));
    }

    @Test
    public void testNegativeUpdate() {
        LOGGER.info("--> Testing update singer");
        Singer singerNew = new Singer();
        singerNew.setFirstName("Mimi");
        singerNew.setBirthDate(LocalDate.now());

        assertThrowsExactly(HttpClientErrorException.BadRequest.class, () -> restClient.put().uri("/singers3/7").body(singerNew).retrieve().body(Singer.class));
    }
}
