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
package com.apress.prospring7.classic.two.methodinject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import static java.lang.System.out;

/**
 * @author iuliana.cosmina on 26/03/2025
 */
public class MethodInjectDemo {

    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext(LookupConfig.class);

        var abstractLockOpener = ctx.getBean("capableLockOpener", LockOpener.class);
        var standardLockOpener = ctx.getBean("standardLockOpener", LockOpener.class);

        displayInfo("capableLockOpener", abstractLockOpener);
        displayInfo("standardLockOpener", standardLockOpener);
    }

    public static void displayInfo(final String beanName, final LockOpener lockOpener) {
        var keyHelperOne = lockOpener.getMyKeyOpener();
        var keyHelperTwo = lockOpener.getMyKeyOpener();

        out.println("[" + beanName + "]: KeyHelper Instances the Same?  " + (keyHelperOne == keyHelperTwo));

        var stopWatch = new StopWatch();
        stopWatch.start("lookupDemo");

        for (var x = 0; x < 100_000; x++) {
            var keyHelper = lockOpener.getMyKeyOpener();
            keyHelper.open();
        }
        stopWatch.stop();
        out.println("100000 gets took " + stopWatch.getTotalTimeMillis() + " ms");
    }
}

@Configuration
@ComponentScan
class LookupConfig {}

/**
 * Listing 2-42
 */
interface LockOpener {
    KeyHelper getMyKeyOpener();
    void openLock();
}

@Component("standardLockOpener")
/**
 * Listing 2-43
 */
class StandardLockOpener implements LockOpener {

    private KeyHelper keyHelper;

    @Autowired
    @Qualifier("keyHelper")
    public void setKeyHelper(KeyHelper keyHelper) {
        this.keyHelper = keyHelper;
    }

    @Override
    public KeyHelper getMyKeyOpener() {
        return keyHelper;
    }

    @Override
    public void openLock() {
        keyHelper.open();
    }
}

@Component("capableLockOpener")
/**
 * Listing 2-44
 */
abstract class CapableLockOpener implements LockOpener {

    @Lookup("keyHelper")
    @Override
    public abstract KeyHelper getMyKeyOpener() ;

    @Override
    public void openLock() {
        getMyKeyOpener().open();
    }
}

@Component("keyHelper")
@Scope("prototype")
/**
 * Listing 2-41
 */
class KeyHelper {
    public void open(){
    }
}
