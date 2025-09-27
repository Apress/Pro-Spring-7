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
package com.apress.prospring7.classic.three.all;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author iuliana.cosmina on 13/04/2025
 * Listing 3-8
 */
public class AllInitMethodsDemo {

    public static void main(String... args) {
        new AnnotationConfigApplicationContext(MultiInitConfiguration.class);
    }
}

@Configuration
class MultiInitConfiguration {

    @Bean
    Dependency dependency (){
        return new Dependency();
    }

    @Bean(initMethod = "initMe")
    MultiInit multiInitBean(){
        return new MultiInit();
    }
}

class Dependency {
}

class MultiInit implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MultiInit.class);
    private Dependency dependency;

    MultiInit() {
        logger.info("1. Calling constructor for bean of type {}.", MultiInit.class);
    }

    public Dependency getDependency() {
        return dependency;
    }

    @Autowired
    public void setDependency(Dependency dependency) {
        logger.info("2. Calling setDependency for bean of type {}.", MultiInit.class);
        this.dependency = dependency;
    }

    @PostConstruct
    private void postConstruct() {
        logger.info("3. Calling postConstruct() for bean of type {}.", MultiInit.class);
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("4. Calling afterPropertiesSet() for bean of type {}.", MultiInit.class);
    }

    private void initMe() {
        logger.info("5. Calling initMethod() for bean of type {}.", MultiInit.class);
    }
}
