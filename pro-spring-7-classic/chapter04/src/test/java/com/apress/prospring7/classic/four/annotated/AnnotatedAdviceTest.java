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

import com.apress.prospring7.classic.four.advice.*;
import com.apress.prospring7.classic.four.common.Guitar;
import com.apress.prospring7.classic.four.common.RejectedInstrumentException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

///
/// @author iulianacosmina on 10/08/2025
/// Listing 4-42
///
public class AnnotatedAdviceTest {
 private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedAdviceTest.class);

 @Test
 void testBeforeAdviceV1(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, BeforeAdviceV1.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV1"));

   final var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
   documentarist.execute();
  }
 }

 @Test
 void testBeforeAdviceV2(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, BeforeAdviceV2.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV2"));

   final var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
   documentarist.execute();
  }
 }

 @Test
 void testBeforeAdviceV3(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, BeforeAdviceV3.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV3"));

   final var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
   documentarist.execute();
  }
 }

 @Test
 void testBeforeAdviceV4(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, BeforeAdviceV4.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV4"));

   final var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
   documentarist.execute();
  }
 }

 @Test
 void testBeforeAdviceV5(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, BeforeAdviceV5.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV5"));

   final var johnMayer = ctx.getBean("johnMayer", GrammyGuitarist.class);
   johnMayer.sing(new Guitar());

   final var pretentiousGuitarist = ctx.getBean("agustin", PretentiousGuitarist.class);
   pretentiousGuitarist.sing(new Guitar());
  }
 }

 @Test
 void testBeforeAdviceV6(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, BeforeAdviceV6.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV6"));

   final var johnMayer = ctx.getBean("johnMayer", GrammyGuitarist.class);
   johnMayer.sing(new Guitar());

   final var pretentiousGuitarist = ctx.getBean("agustin", PretentiousGuitarist.class);
   pretentiousGuitarist.sing(new Guitar());
  }
 }

 @Test
 void testBeforeAdviceV7(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, BeforeAdviceV7.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("beforeAdviceV7"));

   final var johnMayer = ctx.getBean("johnMayer", GrammyGuitarist.class);
   johnMayer.sing(new Guitar());

   final var pretentiousGuitarist = ctx.getBean("agustin", PretentiousGuitarist.class);
   pretentiousGuitarist.sing(new Guitar());
  }
 }

 @Test
 void testAroundAdviceV1(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, AroundAdviceV1.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("aroundAdviceV1"));

   final var documentarist = ctx.getBean("documentarist", NewDocumentarist.class);
   documentarist.execute();
  }
 }

 @Test
 void testAroundAdviceV2(){
  try(final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, CommandingDocumentarist.class, AroundAdviceV2.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("aroundAdviceV2"));

   final var documentarist = ctx.getBean("commandingDocumentarist", CommandingDocumentarist.class);
   documentarist.execute();
  }
 }

 @Test
 void testAfterAdviceV1() {
  try (final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, AfterAdviceV1.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("afterAdviceV1"));

   final var guitar = new Guitar();
   final var guitarist = ctx.getBean("agustin", PretentiousGuitarist.class);
   guitarist.sing(guitar);
   LOGGER.info("-------------------");
   guitar.setBrand("Musicman");

   assertThrows(IllegalArgumentException.class, () -> guitarist.sing(guitar), "Unacceptable guitar!");
  }
 }

 @Test
 void testAfterReturningAdviceV1(){
  try (final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, AfterReturningAdviceV1.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("afterReturningAdviceV1"));

   final var guitar = new Guitar();
   final var guitarist = ctx.getBean("agustin", PretentiousGuitarist.class);
   guitarist.sing(guitar);
   LOGGER.info("-------------------");
   guitar.setBrand("Musicman");

   assertThrows(IllegalArgumentException.class, () -> guitarist.sing(guitar), "Unacceptable guitar!");
  }
 }

 @Test
 void testAfterThrowingAdviceV1(){
  try (final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, AfterThrowingAdviceV1.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("afterThrowingAdviceV1"));

   final var guitar = new Guitar();
   final var guitarist = ctx.getBean("agustin", PretentiousGuitarist.class);
   guitarist.sing(guitar);
   LOGGER.info("-------------------");
   guitar.setBrand("Musicman");

   assertThrows(IllegalArgumentException.class, () -> guitarist.sing(guitar), "Unacceptable guitar!");
  }
 }

 @Test
 void testAfterThrowingAdviceV2(){
  try (final var ctx = new AnnotationConfigApplicationContext()) {
   ctx.register(AspectJAopConfig.class, AfterThrowingAdviceV2.class);
   ctx.refresh();
   assertTrue(Arrays.asList(ctx.getBeanDefinitionNames()).contains("afterThrowingAdviceV2"));

   final var guitar = new Guitar();
   final var guitarist = ctx.getBean("agustin", PretentiousGuitarist.class);
   guitarist.sing(guitar);
   LOGGER.info("-------------------");
   guitar.setBrand("Musicman");

   assertThrows(RejectedInstrumentException.class, () -> guitarist.sing(guitar), "Unacceptable guitar!");
  }
 }

}
