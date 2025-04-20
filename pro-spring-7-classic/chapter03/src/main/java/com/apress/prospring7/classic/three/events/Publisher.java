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
package com.apress.prospring7.classic.three.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author iuliana.cosmina on 18/04/2025
 */
@Component
public class Publisher implements ApplicationContextAware {
    private ApplicationContext ctx;

    @Override  // @Implements {@link ApplicationContextAware#setApplicationContext(ApplicationContext)} }
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void publish(String message) {
        ctx.publishEvent(new MessageEvent(this, message));
    }

    public static void main(String... args) {
        try(var ctx = new AnnotationConfigApplicationContext(EventsConfig.class)) {

            Publisher pub = (Publisher) ctx.getBean("publisher");
            pub.publish("I send an SOS to the world... ");
            pub.publish("... I hope that someone gets my...");
            pub.publish("... Message in a bottle");
        }
    }
}

@Configuration
@ComponentScan
class EventsConfig{ }

class MessageEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private final String msg;

    public MessageEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}

@Component
class MessageEventListener implements ApplicationListener<MessageEvent> {
    private static final Logger logger = LoggerFactory.getLogger(MessageEventListener.class);
    @Override
    public void onApplicationEvent(MessageEvent event) {
        logger.info("Received: {}" , event.getMessage());
    }
}

