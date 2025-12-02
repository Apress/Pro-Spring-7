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
package com.apress.prospring7.classic.eight;

import com.apress.prospring7.classic.eight.config.TransactionCfg;
import com.apress.prospring7.classic.eight.services.AllService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author iulianacosmina on 23/11/2025
 */
public class TransactionsDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsDemo.class);

    static void main() {
        try (var ctx = new AnnotationConfigApplicationContext(TransactionCfg.class)) {
            var service = ctx.getBean(AllService.class);

            LOGGER.info(" ---- Listing singers ------");
            service.findAllWithAlbums().forEach(s -> {
                LOGGER.info(s.toString());
                if (s.getAlbums() != null) {
                    s.getAlbums().forEach(a -> LOGGER.info("\tAlbum:{}", a.toString()));
                }
            });

            var mayer = service.findByIdWithAlbums(1L).orElse(null);
            mayer.setFirstName("John Clayton");

            var album = mayer.getAlbums().stream().filter(
                    a -> a.getTitle().equals("The Search For Everything")).findFirst().orElse(null);

            mayer.getAlbums().remove(album);

            service.update(mayer);

            LOGGER.info("---------- After update ----------");
            service.findAllWithAlbums().forEach(s -> {
                LOGGER.info(s.toString());
                if (s.getAlbums() != null) {
                    s.getAlbums().forEach(a -> LOGGER.info("\tAlbum:{}", a.toString()));
                }
            });

        }
    }
}
