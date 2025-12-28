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
package com.apress.prospring7.classic.four.manual;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

import static java.time.Duration.ofMillis;

///
/// @author iulianacosmina on 15/05/2025
/// Listing 4-3
///
public class ManualAdviceDemo {

 public static void main(String... args) {

  var pf = new ProxyFactory(new Concert());
  pf.addAdvice(new SimpleBeforeAdvice());
  pf.addAdvice(new SimpleAfterAdvice());
  pf.addAdvice(new SimpleAroundAdvice());

  var proxy = (Performance) pf.getProxy();

  proxy.execute();
 }

}

///
/// Listing 4-2
///
interface Performance {
 void execute();
}


///
/// Listing 4-2
///
class Concert  implements Performance {
 private static Logger LOGGER = LoggerFactory.getLogger(Concert.class);
 @Override
 public void execute() {
  LOGGER.info(" ... La la la la laaaa ...");
  try {
   Thread.sleep(ofMillis(2000).toMillis());
  } catch (InterruptedException e) {}
 }
}


// -- AOP custom stuff--
///
/// Listing 4-1
///
class SimpleBeforeAdvice implements MethodBeforeAdvice {
 private static Logger LOGGER = LoggerFactory.getLogger(SimpleBeforeAdvice.class);

 @Override
 public void before(@NonNull Method method, Object[] args, Object target) throws Throwable {
  LOGGER.info("Before: set up concert hall.");
 }
}
///
/// Listing 4-1
///
class SimpleAfterAdvice implements AfterReturningAdvice {
 private static Logger LOGGER = LoggerFactory.getLogger(SimpleAfterAdvice.class);

 @Override
 public void afterReturning(Object returnValue, @NonNull Method method, Object[] args, Object target) throws Throwable {
  LOGGER.info("After: offer standing ovation.");
 }
}
///
/// Listing 4-1
///
class SimpleAroundAdvice implements MethodInterceptor {
 private static Logger LOGGER = LoggerFactory.getLogger(SimpleAroundAdvice.class);

 @Override
 public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
  LOGGER.info("Around: starting timer");
  StopWatch sw = new StopWatch();
  sw.start(invocation.getMethod().getName());
  Object returnValue = invocation.proceed();
  sw.stop();
  LOGGER.info("Around: concert duration = {}", sw.getTotalTimeMillis());
  return returnValue;
 }
}
