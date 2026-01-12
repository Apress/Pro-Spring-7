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
package com.apress.prospring7.classic.twelve;

import com.apress.prospring7.classic.twelve.converter.bean.ConverterCfg;
import com.apress.prospring7.classic.twelve.domain.Blogger;
import com.apress.prospring7.classic.twelve.domain.SimpleBlogger;
import com.apress.prospring7.classic.twelve.property.editor.CustomEditorCfg;
import com.apress.prospring7.classic.twelve.property.editor.CustomRegistrarCfg;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.ConversionService;

import static com.apress.prospring7.classic.twelve.CommonTestBase.checkBloggerBeans;

///
/// @author iulianacosmina on 12/01/2026
///
public class ConvertersTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertersTest.class);

    @Test // the old way
    public void testCustomPropertyEditorRegistrar() {
        try (final var ctx = new AnnotationConfigApplicationContext(AppConfig.class, CustomRegistrarCfg.class)) {
            checkBloggerBeans(ctx);
        }
    }

    @Test // also the old way
    public void testLocalDateEditor() {
        try (final var ctx = new AnnotationConfigApplicationContext(AppConfig.class, CustomEditorCfg.class)) {
            checkBloggerBeans(ctx);
        }
    }

    @Test
    public void testFormattingConverterBean() {
        try (final var ctx = new AnnotationConfigApplicationContext(AppConfig.class, ConverterCfg.class)) {
            checkBloggerBeans(ctx);
        }
    }

    @Test
    public void testConvertingToSimpleBlogger() {
        try (final var ctx = new AnnotationConfigApplicationContext(AppConfig.class, ConverterCfg.class)) {
            var springBlogger = ctx.getBean("springBlogger", Blogger.class);
            LOGGER.info("SpringBlogger info: {}" , springBlogger);

            var conversionService = ctx.getBean(ConversionService.class);
            var simpleBlogger = conversionService.convert(springBlogger, SimpleBlogger.class);
            LOGGER.info("simpleBlogger info: {}" , simpleBlogger);
        }
    }

}
