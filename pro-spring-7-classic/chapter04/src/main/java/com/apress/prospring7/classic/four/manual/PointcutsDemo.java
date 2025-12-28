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

import com.apress.prospring7.classic.four.advice.LogAroundAdvice;
import com.apress.prospring7.classic.four.common.GooTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.Pointcuts;

import static com.apress.prospring7.classic.four.manual.ComposablePointcutDemo.testInvoke;

///
/// @author iulianacosmina on 06/08/2025
///
public class PointcutsDemo {
 private static final Logger LOGGER = LoggerFactory.getLogger(PointcutsDemo.class);

 public static void main(String... args) {
  final var target = new GooTarget();

  final var pc =  new ComposablePointcut(ClassFilter.TRUE, new FooMethodMatcher());

  LOGGER.info("Test 1 >> ");
  GooTarget proxy = getProxy(pc, target);
  testInvoke(proxy);

  LOGGER.info("Test 2 >> ");
  proxy = getProxy(Pointcuts.union(pc, new ComposablePointcut(ClassFilter.TRUE, new BazMethodMatcher())), target);
  testInvoke(proxy);

  LOGGER.info("Test 3 >> ");
  proxy = getProxy(Pointcuts.intersection(pc, new ComposablePointcut(ClassFilter.TRUE, new QuxMethodMatcher())), target);
  testInvoke(proxy);
 }

 private static GooTarget getProxy(final Pointcut pc, final GooTarget target) {
  final var advisor = new DefaultPointcutAdvisor(pc, new LogAroundAdvice());

  final var pf = new ProxyFactory();
  pf.setTarget(target);
  pf.addAdvisor(advisor);
  return (GooTarget) pf.getProxy();
 }
}
