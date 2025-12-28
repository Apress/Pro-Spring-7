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
package com.apress.prospring7.classic.two.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

///
/// @author iuliana.cosmina on 30/03/2025
/// Listing 2-47
///
public class BeanNamingDemo {

    private static final Logger logger = LoggerFactory.getLogger(BeanNamingDemo.class);

    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext(BeanNamingCfg.class);
        Arrays.stream(ctx.getBeanDefinitionNames()).forEach(logger::debug);

        logger.info(" ---- Attempting to get bean by class: {}  --- ", SimpleBean.class.getSimpleName());
        try {
            ctx.getBean(SimpleBean.class);
        } catch (Exception e) {
            logger.debug("More beans than expected found. ", e);
        }

        logger.info(" ---- All beans of type: {}  --- ", SimpleBean.class.getSimpleName());
        var simpleBeans = ctx.getBeansOfType(SimpleBean.class);
        simpleBeans.forEach((key, _) -> System.out.println(key));

        logger.info(" ---- Listing aliases for beans of type: {}  --- ", SimpleBean.class.getSimpleName());
        simpleBeans.forEach((k, _) -> {
            var aliases = ctx.getAliases(k);
            if(aliases.length > 0) {
                logger.debug("Aliases for bean {} ", k);
                Arrays.stream(aliases).forEach(a -> logger.debug("\t > {}", a));
            }
        });
    }
}
@Configuration
@ComponentScan
///
/// Listing 2-48, 2-49, Listing 2-51
///
class BeanNamingCfg {

    @Bean
    public SimpleBean  anotherSimpleBean(){
        return new SimpleBean();
    }

    //@Bean(name="simpleBeanTwo")
    //@Bean(value= "simpleBeanTwo")
    @Bean("simpleBeanTwo")
    public SimpleBean simpleBean2(){
        return new SimpleBean();
    }

    //@Bean(name= {"simpleBeanThree", "three", "numero_tres"})
    //@Bean(value= {"simpleBeanThree", "three", "numero_tres"})
    @Bean({"simpleBeanThree", "three", "numero_tres"})
    public SimpleBean simpleBean3(){
        return new SimpleBean();
    }
}

// swap these two annotations to test explicit naming
// @Component(value = "simpleBeanOne")  // Listing 2-50
//@Component

///
/// Listing 2-46
///
class SimpleBean { }
