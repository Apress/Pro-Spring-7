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
package com.apress.prospring7.classic.thirteen;

import com.apress.prospring7.classic.thirteen.entities.Singer;
import com.apress.prospring7.classic.thirteen.handlers.HomeHandler;
import com.apress.prospring7.classic.thirteen.handlers.SingerHandler;
import com.apress.prospring7.classic.thirteen.util.CriteriaDto;
import jakarta.servlet.ServletException;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.web.servlet.function.RouterFunctions.resources;
import static org.springframework.web.servlet.function.RouterFunctions.route;

///
/// @author iulianacosmina on 23/01/2026
///
@Configuration
public class RoutesConfig {
    final static Logger LOGGER = LoggerFactory.getLogger(RoutesConfig.class);

    @Bean
    public RouterFunction<ServerResponse> staticRouter() {
        return resources("/images/**", new ClassPathResource("static/images/"))
                .and(resources("/styles/**", new ClassPathResource("static/styles/")))
                .and(resources("/js/**", new ClassPathResource("static/js/")));
    }

    @Bean
    public RouterFunction<ServerResponse> homeRoutes(HomeHandler homeHandler) {
        return route()
                .GET("/", homeHandler::home) // returns home view template
                .GET("/home", homeHandler::home) // returns home view template
                .GET("/beans", homeHandler::beans)
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> singerRoutes(SingerHandler singerHandler) {
        return route()
                .path("/singers" , builder -> builder
                        .GET("", singerHandler.list)
                        .GET("/search", singerHandler.searchForm)
                        .GET("/create", singerHandler.createForm)
                )

                .GET("/singer/{id}", singerHandler.findById)
                .GET("/singer/{id}/photo", singerHandler::downloadPhoto)
                .POST("/singer/{id}/photo", RequestPredicates.contentType(MediaType.MULTIPART_FORM_DATA), singerHandler::uploadPhoto)
                .DELETE("/singer/{id}", singerHandler.deleteById)
                .GET("/singer/{id}/edit", singerHandler.editForm)
                .GET("/singer/{id}/upload", singerHandler.uploadPhotoForm)
                .build()
                .and(
                        route().GET("/singers/go", singerHandler::go).before( req ->
                                        ServerRequest.from(req).attribute("criteriaDto", criteriaFromParam(req)).build())
                        .build()
                ).and(
                        route()
                                .POST("/singers", RequestPredicates.contentType(MediaType.MULTIPART_FORM_DATA), singerHandler::create)
                                .PUT("/singer/{id}", singerHandler::edit)
                                .before( req -> ServerRequest.from(req).attribute("singer", singerFromParam(req)).build())
                        .build()
                ).filter((request, next) -> {
                    LOGGER.info("Before handler invocation: {}" , request.path());
                    return next.handle(request);
                });

        /*
        route()
                // singer
                .GET("/singers", singerHandler.list)
                .GET("/singer/{id}", singerHandler.findById)
                .GET("/singer/{id}/photo", singerHandler::downloadPhoto)
                .POST("/singer/{id}/photo", RequestPredicates.contentType(MediaType.MULTIPART_FORM_DATA), singerHandler::uploadPhoto)
                .DELETE("/singer/{id}", singerHandler.deleteById)
                .GET("/singers/search", singerHandler.searchForm)
                .GET("/singers/create", singerHandler.createForm)
                .GET("/singer/{id}/edit", singerHandler.editForm)
                .GET("/singer/{id}/upload", singerHandler.uploadPhotoForm)
                .build()
                .and(
                        route().GET("/singers/go", singerHandler::go).before( req ->
                                        ServerRequest.from(req).attribute("criteriaDto", criteriaFromParam(req)).build())
                        .build()
                ).and(
                        route()
                                .POST("/singers", RequestPredicates.contentType(MediaType.MULTIPART_FORM_DATA), singerHandler::create)
                                .PUT("/singer/{id}", singerHandler::edit)
                                .before( req -> ServerRequest.from(req).attribute("singer", singerFromParam(req)).build())
                        .build()
                ).filter((request, next) -> {
                    LOGGER.info("Before handler invocation: {}" , request.path());
                    return next.handle(request);
                });
         */
    }

    private static @NonNull Singer singerFromParam(ServerRequest req) {
        var singer = new Singer();
        singer.setFirstName(req.param("firstName").orElse(null));
        singer.setLastName(req.param("lastName").orElse(null));
        var bdStr = req.param("birthDate").orElse("");
        if (StringUtils.hasLength(bdStr))
            singer.setBirthDate(StringUtils.hasLength(bdStr)? LocalDate.parse(bdStr) : null);

        try {
            if(req.multipartData().getFirst("photo") != null) {
                Objects.requireNonNull(req.multipartData().getFirst("photo")).getInputStream();
                var input = Objects.requireNonNull(req.multipartData().getFirst("photo")).getInputStream();
                var fileContent = IOUtils.toByteArray(input);
                singer.setPhoto(fileContent);
            }
        } catch (IOException | ServletException e) {
           // throw new RuntimeException(e);
            // keep it simple, omit the photo issues
        }
        return singer;
    }

    private static @NonNull CriteriaDto criteriaFromParam(ServerRequest req) {
        var criteria = new CriteriaDto();
        criteria.setFieldName(req.param("fieldName").orElse(null));
        criteria.setFieldValue(req.param("fieldValue").orElse(null));
        criteria.setExactMatch(req.param("exactMatch").map(Boolean::valueOf).orElse(false));
        return criteria;
    }
}
