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
package com.apress.prospring7.classic.four.manual.dy;

import com.apress.prospring7.classic.four.advice.LogAroundAdvice;
import com.apress.prospring7.classic.four.common.FooTarget;
import com.apress.prospring7.classic.four.common.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;

import java.lang.reflect.Method;

///
/// @author iulianacosmina on 24/07/2025
/// Listing 4-9
///
public class DynamicPointcutDemo {

 public static void main(String... args) {
  // Listing 4-11
  final var pf = new ProxyFactory(new FooTarget());
  pf.addAdvisor(new DefaultPointcutAdvisor(new SimpleDynamicPointcut(), new LogAroundAdvice()));

  /*final var target = new FooTarget();
  final var pf = new ProxyFactory();
  pf.setTarget(target);
  pf.addAdvisor(new DefaultPointcutAdvisor(new SimpleDynamicPointcut(), new LogAroundAdvice()));*/

  final var  proxy = (Target)pf.getProxy();

  proxy.foo("one");
  proxy.foo("two");
  proxy.foo("three");
  System.out.println("--------------------------");
  proxy.foo();
 }
}

///
/// Listing 4-8
///
class SimpleDynamicPointcut extends DynamicMethodMatcherPointcut {
 private static Logger LOGGER = LoggerFactory.getLogger(SimpleDynamicPointcut.class);

 @Override
 public ClassFilter getClassFilter() {
  return cls -> (cls == FooTarget.class);
 }

 @Override
 public boolean matches(Method method, Class<?> targetClass) {
  LOGGER.debug("Static check for {}", method.getName());
  return ("foo".equals(method.getName()));
 }

 @Override
 public boolean matches(Method method, Class<?> targetClass, Object... args) {
  LOGGER.debug("Dynamic check for {}", method.getName());

  if(args.length == 0) {
   return false;
  }
  var bar = (String) args[0];
  return bar.equalsIgnoreCase("two");
 }
}
