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
package com.apress.prospring7.classic.two.autowiring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

///
/// @author iuliana.cosmina on 31/03/2025
/// Listing 2-59
///
public class AutowiringDemo {
    private static final Logger logger = LoggerFactory.getLogger(AutowiringDemo.class);

    public static void main(String... args) {
        final var ctx = new AnnotationConfigApplicationContext(AutowiringCfg.class);

        logger.info(" ---- Autowiring: constructor  --- ");
        var target = ctx.getBean(Target.class);
        logger.info("Created target? {}" , target != null);
        logger.info("Injected bar? {}" , target.bar != null);
        logger.info("Injected fooOne? {}" , target.fooOne != null ? target.fooOne.id: "nope");
        logger.info("Injected fooTwo? {}" , target.fooTwo != null ? target.fooTwo.id : "nope");

        logger.info(" ---- Autowiring: byType (setter) --- ");
        var anotherTarget = ctx.getBean(AnotherTarget.class);
        logger.info("anotherTarget: Created anotherTarget? {}" , anotherTarget != null);
        logger.info("anotherTarget: Injected bar? {}" , anotherTarget.bar != null);
        logger.info("anotherTarget: Injected fooOne? {}" , anotherTarget.fooOne != null ? anotherTarget.fooOne.id: "");
        logger.info("anotherTarget: Injected fooTwo? {}" , anotherTarget.fooTwo != null ? anotherTarget.fooTwo.id : "");

        logger.info(" ---- Autowiring: byType (field) --- ");
        var fieldTarget = ctx.getBean(FieldTarget.class);
        logger.info("fieldTarget: Created fieldTarget? {}" , fieldTarget != null);
        logger.info("fieldTarget: Injected bar? {}" , fieldTarget.bar != null);
        logger.info("fieldTarget: Injected fooOne? {}" , fieldTarget.fooOne != null ? fieldTarget.fooOne.id: "");
        logger.info("fieldTarget: Injected fooTwo? {}" , fieldTarget.fooTwo != null ? fieldTarget.fooTwo.id : "");
    }
}

@Component
@Lazy
///
/// Listing 2-61, Listing 2-65
///
class FieldTarget {

    @Autowired /*@Qualifier("foo")*/ Foo fooOne;
    @Autowired /*@Qualifier("anotherFoo")*/ Foo fooTwo;
    @Autowired Bar bar;

}

@Configuration
@ComponentScan
class AutowiringCfg {

    /*@Bean
    Foo anotherFoo() {
        return new Foo();
    }*/
}

@Component
class Foo {
    String id = UUID.randomUUID().toString().replace("-","").substring(0,8);
}

@Component
class Bar {}

@Component
@Lazy
class Target {
    private static final Logger logger = LoggerFactory.getLogger(Target.class);

    Foo fooOne;
    Foo fooTwo;
    Bar bar;

    public Target() {
        logger.info(" --> Target() called");
    }

    public Target(Foo foo) {
        this.fooOne = foo;
        logger.info(" --> Target(Foo) called");
    }

    public Target(Foo foo, Bar bar) {
        this.fooOne = foo;
        this.bar = bar;
        logger.info(" --> Target(Foo, Bar) called");
    }
}

///
/// Listing 2-60, Listing 2-62,  Listing 2-63, Listing 2-64
///
@Component
@Lazy
class AnotherTarget {

    private static final Logger logger = LoggerFactory.getLogger(AnotherTarget.class);
    Foo fooOne;
    Foo fooTwo;
    Bar bar;

    @Autowired
    public void setFooOne(/*@Qualifier("foo")*/Foo fooOne) {
        logger.info(" --> AnotherTarget#setFooOne(Foo) called");
        this.fooOne = fooOne;
    }

    @Autowired
    public void setFooTwo(/*@Qualifier("anotherFoo")*/Foo fooTwo) {
        logger.info(" --> AnotherTarget#setFooTwo(Foo) called");
        this.fooTwo = fooTwo;
    }

    @Autowired
    public void setBar(Bar bar) {
        logger.info(" --> AnotherTarget#setBar(Bar) called");
        this.bar = bar;
    }
}
