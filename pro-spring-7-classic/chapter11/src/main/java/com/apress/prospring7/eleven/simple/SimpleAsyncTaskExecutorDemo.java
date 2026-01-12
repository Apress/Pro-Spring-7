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
package com.apress.prospring7.eleven.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

///
/// @author iulianacosmina on 12/01/2026
///
public class SimpleAsyncTaskExecutorDemo {
 static void main() throws IOException {
  try (final var ctx =
               new AnnotationConfigApplicationContext(AppConfig.class, RandomStringPrinter.class)) {
   var printer = ctx.getBean(RandomStringPrinter.class);
   printer.executeTask();

   System.in.read();
  }
 }
}

@Configuration
@EnableAsync
class AppConfig {
 @Bean
 TaskExecutor taskExecutor() {
  return new SimpleAsyncTaskExecutor();
 }
}

@Component
class RandomStringPrinter {

 private final Logger LOGGER = LoggerFactory.getLogger(RandomStringPrinter.class);

 private final TaskExecutor taskExecutor;

 public RandomStringPrinter(TaskExecutor taskExecutor) {
  this.taskExecutor = taskExecutor;
 }

 public void executeTask() {
  for(int i=0; i < 10; ++ i) {
   final int index = i;
   taskExecutor.execute(() -> LOGGER.info("{}: {}", index , UUID.randomUUID().toString().substring(0, 8)));
  }
 }
}
