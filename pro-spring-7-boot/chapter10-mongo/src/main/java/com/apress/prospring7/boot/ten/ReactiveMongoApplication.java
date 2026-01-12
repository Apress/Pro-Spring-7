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
package com.apress.prospring7.boot.ten;

import com.apress.prospring7.boot.ten.repos.SingerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

///
/// @author iulianacosmina on 03/01/2026
///
@EnableReactiveMongoRepositories(basePackages = "com.apress.prospring7.boot.ten.repos")
@SpringBootApplication
public class ReactiveMongoApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveMongoApplication.class);

    static void main(String... args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");
        try (final var ctx = SpringApplication.run(ReactiveMongoApplication.class, args)){
            var singerRepo = ctx.getBean(SingerRepository.class);

            LOGGER.info(" ---- Listing singers Spring Data R2DC Repo:");
            singerRepo.findAll(). doOnNext(s -> LOGGER.info("{}", s))
                    // using this here for demonstrative purposes, in a real application a reactive client will consume this Flux
                    .blockLast();
        }
    }
}
