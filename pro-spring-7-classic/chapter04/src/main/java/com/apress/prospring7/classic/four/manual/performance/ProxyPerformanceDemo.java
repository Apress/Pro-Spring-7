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
package com.apress.prospring7.classic.four.manual.performance;

import com.apress.prospring7.classic.four.advice.NoOpBeforeAdvice;
import com.apress.prospring7.classic.four.common.SimpleTarget;
import com.apress.prospring7.classic.four.common.TestTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

import java.util.stream.IntStream;

/**
 * @author iulianacosmina on 06/08/2025
 */
public class ProxyPerformanceDemo {

 private static final Logger LOGGER = LoggerFactory.getLogger(ProxyPerformanceDemo.class);

 public static void main(String... args) {
  final var target = new TestTarget();

  final var advisor = new NameMatchMethodPointcutAdvisor(new NoOpBeforeAdvice());
  advisor.setMappedName("foo");

 LOGGER.info("Starting tests ...");
  runCglibTests(advisor, target);
  runCglibFrozenTests(advisor, target);
  runJdkTests(advisor, target);
 }

 private static void runCglibTests(Advisor advisor, TestTarget target) {
  final var pf = new ProxyFactory();
  pf.setProxyTargetClass(true);
  pf.setTarget(target);
  pf.addAdvisor(advisor);
  final var proxy = (TestTarget)pf.getProxy();
  final var testResults = test(proxy);
  LOGGER.info(" --- CGLIB (Standard) Test results ---\n {} ", testResults);
 }

 private static void runCglibFrozenTests(Advisor advisor, TestTarget target) {
  final var pf = new ProxyFactory();
  pf.setProxyTargetClass(true);
  pf.setTarget(target);
  pf.addAdvisor(advisor);
  pf.setFrozen(true);
  final var proxy = (TestTarget) pf.getProxy();
  final var testResults = test(proxy);
  LOGGER.info(" --- CGLIB (Frozen) Test results ---\n {} ", testResults);
 }

 private static void runJdkTests(Advisor advisor, TestTarget target) {
  ProxyFactory pf = new ProxyFactory(target);
  pf.addAdvisor(advisor);
  final var proxy = (SimpleTarget)pf.getProxy();
  var testResults = test(proxy);
  LOGGER.info(" --- JDK Test results ---\n {} ", testResults);
 }

 private static TestResults test(final SimpleTarget proxy) {
  final var testResults = new TestResults();
  long before = System.currentTimeMillis();
  IntStream.range(0, 500_000).forEach(_ -> proxy.advised());
  long after = System.currentTimeMillis();
  testResults.advisedMethodTime = after - before;
  //-----

  before = System.currentTimeMillis();
  IntStream.range(0, 500_000).forEach(_ -> proxy.unadvised());
  after = System.currentTimeMillis();
  testResults.unadvisedMethodTime = after - before;
  //-----

  before = System.currentTimeMillis();
  IntStream.range(0, 500_000).forEach(_ -> proxy.equals(proxy));
  after = System.currentTimeMillis();
  testResults.equalsTime = after - before;
  // ----

  before = System.currentTimeMillis();
  IntStream.range(0, 500_000).forEach(_ -> proxy.hashCode());
  after = System.currentTimeMillis();
  testResults.hashCodeTime = after - before;
  // -----

  before = System.currentTimeMillis();
  IntStream.range(0, 500_000).forEach(_ -> ((Advised)proxy).getTargetClass());
  after = System.currentTimeMillis();
  testResults.proxyTargetTime = after - before;
  return testResults;
 }

}
