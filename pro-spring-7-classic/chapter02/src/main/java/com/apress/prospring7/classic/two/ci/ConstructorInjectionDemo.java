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
package com.apress.prospring7.classic.two.ci;

import com.apress.prospring7.classic.two.decoupled.MessageProvider;
import com.apress.prospring7.classic.two.decoupled.MessageRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static java.lang.System.out;

///
/// @author iuliana.cosmina on 19/02/2025
/// I made the decision to put all classes related to this example in the same file.
/// It is easier to navigate the project, also you have all the components in the same file,
/// so there is no doubt where the bean definitions are coming from.
///
public class ConstructorInjectionDemo {

    public static void main(String... args) {
        var ctx =
                new AnnotationConfigApplicationContext(HelloWorldConfiguration.class); // <1>
        MessageRenderer mr = ctx.getBean("renderer", MessageRenderer.class);
        mr.render();
    }
}

// --- Java configuration class  ---
@Configuration
@ComponentScan
class HelloWorldConfiguration {
}

//  --- bean definitions using @Component ---
//simple bean
@Component("provider")
class HelloWorldMessageProvider implements MessageProvider {

    @Override
    public String getMessage() {
        return "Hello World!";
    }
}

///
/// Complex bean requiring a dependency
/// Listing 2-15, 2-26
///
@Component("renderer")
class StandardOutMessageRenderer implements MessageRenderer {
    private MessageProvider messageProvider;

    @Autowired
    StandardOutMessageRenderer(MessageProvider messageProvider) {
        out.println(" ~~ Injecting dependency using constructor ~~");
        this.messageProvider = messageProvider;
    }

    ///
    /// Listing 2-16
    ///
    @Override
    public void setMessageProvider(MessageProvider provider) {
        this.messageProvider = provider;
    }

    @Override
    public MessageProvider getMessageProvider() {
        return this.messageProvider;
    }

    @Override
    public void render() {
        if (messageProvider == null) {
            throw new RuntimeException("A message provider is required!");
        }
        out.println(messageProvider.getMessage());
    }
}
