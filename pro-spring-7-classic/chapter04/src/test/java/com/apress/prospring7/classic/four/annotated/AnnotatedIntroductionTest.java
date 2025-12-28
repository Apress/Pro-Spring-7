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
package com.apress.prospring7.classic.four.annotated;

import com.apress.prospring7.classic.four.common.Guitar;
import com.apress.prospring7.classic.four.common.Performer;
import com.apress.prospring7.classic.four.common.Singer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

///
/// @author iulianacosmina on 10/08/2025
///
public class AnnotatedIntroductionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedIntroductionTest.class);

    @Test
    void testAnnotatedIntroduction() {
        try(final var ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(AspectJAopConfig.class, AnnotatedIntroduction.class);
            ctx.refresh();
            assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("annotatedIntroduction"));

            final var guitar = new Guitar();
            final var guitarist = ctx.getBean("agustin", PretentiousGuitarist.class);

            assertTrue(guitarist instanceof Singer);
            guitarist.sing(guitar);

            LOGGER.info("Proxy type: {} ", guitarist.getClass().getName());

            assertTrue(guitarist instanceof Performer);
            final var  performer = (Performer) guitarist;
            performer.perform();
        }
    }
}
