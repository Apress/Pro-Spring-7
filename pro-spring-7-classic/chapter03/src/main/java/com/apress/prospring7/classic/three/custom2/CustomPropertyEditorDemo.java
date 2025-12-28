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
package com.apress.prospring7.classic.three.custom2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Map;

///
/// @author iuliana.cosmina on 17/04/2025
/// Listing 3-24, Listing 3-25, Listing 3-26
///
public class CustomPropertyEditorDemo {
    private static final Logger logger = LoggerFactory.getLogger(CustomPropertyEditorDemo.class);
    public static void main(String... args) {

        try(var ctx = new AnnotationConfigApplicationContext(CustomPropertyEditorCfg.class)) {
            var person = ctx.getBean(Person.class, "person");
            logger.info("Person full name = {}" , person.getName());
        }
    }
}

/*
@Configuration
@ComponentScan
class CustomPropertyEditorCfg {
    @Bean
    static CustomEditorConfigurer customEditorConfigurer(){
        var cust = new CustomEditorConfigurer();
        cust.setCustomEditors(Map.of(FullName.class, FullNamePropertyEditor.class));
        return cust;
    }
}
*/

/*
@Configuration
@ComponentScan
class CustomPropertyEditorCfg implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerCustomEditor(FullName.class, FullNamePropertyEditor.class);
    }
}
*/

/*
@Configuration
@ComponentScan
class CustomPropertyEditorCfg implements BeanFactoryPostProcessor {
    @Bean
    PropertyEditorRegistrar customPropertyEditorRegistrar(){
        return new CustomPropertyEditorRegistrar();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.addPropertyEditorRegistrar(customPropertyEditorRegistrar());
    }
}
*/


@Configuration
@ComponentScan
///
/// Listing 3-27
///
class CustomPropertyEditorCfg {
    @Bean
    static PropertyEditorRegistrar customPropertyEditorRegistrar(){
        return new CustomPropertyEditorRegistrar();
    }

    @Bean
    static CustomEditorConfigurer customEditorConfigurer(){
        var cust = new CustomEditorConfigurer();
        PropertyEditorRegistrar[] propertyEditorRegistrars = new PropertyEditorRegistrar[1];
        propertyEditorRegistrars[0] = customPropertyEditorRegistrar();
        cust.setPropertyEditorRegistrars(propertyEditorRegistrars);
        return cust;
    }
}

final class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(FullName.class, new FullNamePropertyEditor());
    }
}


@Component
class Person {
    private FullName name;

    @Value("John Mayer")
    public void setName(FullName name) {
        this.name = name;
    }

    public FullName getName() {
        return name;
    }
}

class FullNamePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String[] name = text.split("\\s");
        setValue(new FullName(name[0], name[1]));
    }
}

record FullName (String firstName, String lastName) { }
