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
package com.apress.prospring7.classic.three.intf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author iuliana.cosmina on 08/04/2025
 */
public class InitializingBeanDemo {
    public static void main(String... args) {
        new AnnotationConfigApplicationContext(SingerConfiguration.class);
    }
}

@Configuration
class SingerConfiguration {

    @Bean
    Singer singerOne(){
        Singer singer = new Singer();
        singer.setName("John Mayer");
        singer.setAge(43);
        return singer;
    }

    @Bean
    Singer singerTwo(){
        Singer singer = new Singer();
        singer.setAge(42);
        return singer;
    }

    @Bean
    Singer singerThree(){
        Singer singer = new Singer();
        singer.setName("John Butler");
        return singer;
    }
}

class Singer implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(Singer.class);

    private static final String DEFAULT_NAME = "No Name";
    private String name;
    private int age = Integer.MIN_VALUE;

    public Singer() {
        logger.info("Invoking constructor for bean of type {}.", Singer.class);
    }


    public void setName(String name) {
        logger.info("Calling setName for bean of type {}.", Singer.class);
        this.name = name;
    }

    public void setAge(int age) {
        logger.info("Calling setAge for bean of type {}.", Singer.class);
        this.age = age;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Initializing bean using 'afterPropertiesSet()'");
        if (name == null) {
            logger.info("Using default name");
            name = DEFAULT_NAME;
        }
        if (age == Integer.MIN_VALUE) {
            throw new IllegalArgumentException(
                    "You must set the age property of any beans of type " + Singer.class);
        }
    }
}

