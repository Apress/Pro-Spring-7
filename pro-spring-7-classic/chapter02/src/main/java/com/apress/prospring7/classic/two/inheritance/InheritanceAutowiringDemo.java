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
package com.apress.prospring7.classic.two.inheritance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

import java.util.UUID;

/**
 * @author iuliana.cosmina on 01/04/2025
 * Listing 2-66
 */
public class InheritanceAutowiringDemo {
    private static final Logger logger = LoggerFactory.getLogger(InheritanceAutowiringDemo.class);

    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext(AutowiringCfg.class);
        var target = ctx.getBean(TrickyTarget.class);
        logger.info("target: Created target? {}" , target != null);
        logger.info("target: Injected bar? {}" , target.bar != null);
        logger.info("target: Injected fooOne? {}" , target.fooOne != null ? target.fooOne.toString(): "");
        logger.info("target: Injected fooTwo? {}" , target.fooTwo != null ? target.fooTwo.toString() : "");
    }
}

interface Foo {
// empty interface, used as a marker interface
}

class FooImplOne implements Foo {
    String id = "one:" + UUID.randomUUID().toString().replace("-","").substring(0,8);

    @Override
    public String toString() {
        return "FooImplOne{" +
                "id='" + id + '\'' +
                '}';
    }
}

class FooImplTwo implements Foo {
    String id = "two:" + UUID.randomUUID().toString().replace("-","").substring(0,8);

    @Override
    public String toString() {
        return "FooImplTwo{" +
                "id='" + id + '\'' +
                '}';
    }
}

class Bar { }

@Configuration
@ComponentScan
/**
 * Listing 2-67
 */
class AutowiringCfg {

    @Bean
    // @Primary // use this to prioritize this bean for injection
    // @Fallback // use this to prioritize this fooImplTwo for injection
    Foo fooImplOne() { return new FooImplOne(); }

    @Bean
    Foo fooImplTwo() { return new FooImplTwo(); }

    @Bean
    Bar bar() { return new Bar(); }

    @Bean
    TrickyTarget trickyTarget() { return new TrickyTarget(); }
}

/**
 * Listing 2-68
 */
class TrickyTarget {
    private static Logger logger = LoggerFactory.getLogger(TrickyTarget.class);

    Foo fooOne;
    Foo fooTwo;
    Bar bar;

    public TrickyTarget() {
        logger.info(" --> TrickyTarget() called");
    }

    public TrickyTarget( Foo foo) {
        this.fooOne = foo;
        logger.info(" --> TrickyTarget(Foo) called");
    }

    public TrickyTarget(Foo foo, Bar bar) {
        this.fooOne = foo;
        this.bar = bar;
        logger.info(" --> TrickyTarget(Foo, Bar) called");
    }

    // comment @Qualifier annotation to cause NoUniqueBeanDefinitionException being thrown at runtime
    @Autowired
    //@Qualifier("fooImplOne")
    public void setFooOne(Foo fooOne) {
        this.fooOne = fooOne;
        logger.info(" --> Property fooOne set");
    }

    // comment @Qualifier annotation to cause NoUniqueBeanDefinitionException being thrown at runtime
    // and make sure for @Primary in FooImpl to be commented as well
    @Autowired
    //@Qualifier("fooImplTwo")
    public void setFooTwo(Foo foo) {
        this.fooTwo = foo;
        logger.info(" --> Property fooTwo set");
    }

    @Autowired
    public void setBar(Bar bar) {
        this.bar = bar;
        logger.info(" --> Property bar set");
    }
}
