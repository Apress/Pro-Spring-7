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
package com.apress.prospring7.classic.four;

import com.apress.prospring7.classic.four.advice.LogAroundAdvice;
import com.apress.prospring7.classic.four.common.Documentarist;
import com.apress.prospring7.classic.four.common.GrammyGuitarist;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AopConfig implements BeanFactoryAware {

 private BeanFactory beanFactory;

 @Override
 public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
  this.beanFactory = beanFactory;
 }

 @Bean
 public GrammyGuitarist johnMayer() { return new GrammyGuitarist(); }

 @Bean
 public Advice advice() { return new LogAroundAdvice(); }

 @Bean
 public GrammyGuitarist proxyOne() {
  final var pfb = new ProxyFactoryBean();
  pfb.setProxyTargetClass(true);
  pfb.setTarget(this.johnMayer());
  pfb.setInterceptorNames("advice");
  pfb.setBeanFactory(this.beanFactory);
  pfb.setFrozen(true);
  return (GrammyGuitarist)pfb.getObject();
 }

 @Bean
 public Documentarist documentaristOne() {
  final var documentarist = new Documentarist();
  documentarist.setDep(this.proxyOne());
  return documentarist;
 }

 @Bean
 public GrammyGuitarist proxyTwo() {
  final var pfb = new ProxyFactoryBean();
  pfb.setProxyTargetClass(true);
  pfb.setTarget(this.johnMayer());
  pfb.setInterceptorNames("advisor");
  pfb.setBeanFactory(this.beanFactory);
  pfb.setFrozen(true);
  return (GrammyGuitarist)pfb.getObject();
 }

 @Bean
 public Documentarist documentaristTwo() {
  final var documentarist = new Documentarist();
  documentarist.setDep(this.proxyTwo());
  return documentarist;
 }

 @Bean
 public DefaultPointcutAdvisor advisor() {
  final var advisor = new DefaultPointcutAdvisor();
  advisor.setAdvice(this.advice());
  final var pc = new AspectJExpressionPointcut();
  pc.setExpression("execution(* sing*(..))");
  advisor.setPointcut(pc);
  return advisor;
 }
}

/**
 * @author iulianacosmina on 07/08/2025
 */
public class ProxyFactoryBeanDemo {
 private static final Logger LOGGER = LoggerFactory.getLogger(ProxyFactoryBeanDemo.class);

 public static void main(String... args) {
  var ctx = new AnnotationConfigApplicationContext(AopConfig.class);

  Documentarist documentaristOne =
          ctx.getBean("documentaristOne", Documentarist.class);
  Documentarist documentaristTwo =
          ctx.getBean("documentaristTwo", Documentarist.class);

  LOGGER.info("Documentarist One >>");
  documentaristOne.execute();

  LOGGER.info("Documentarist Two >> ");
  documentaristTwo.execute();
 }
}
