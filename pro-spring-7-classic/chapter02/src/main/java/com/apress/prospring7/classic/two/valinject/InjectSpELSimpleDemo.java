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
package com.apress.prospring7.classic.two.valinject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

///
/// @author iuliana.cosmina on 23/03/2025
///
@Service("injectSimpleSpEL")
///
/// Listing 2-33
///
class InjectSpELSimpleDemo {
    //@Value("#{injectValues.name.toUpperCase()}")
    @Value("#{(injectValues.name ?: 'Unknown').toUpperCase()}")
    private String name;

    @Value("#{injectValues.age + 1}")
    private int age;

    @Value("#{injectValues.height}")
    private float height;

    @Value("#{injectValues.developer}")
    private boolean developer;

    @Value("#{injectValues.ageInSeconds}")
    private Long ageInSeconds;

    @Value("#{injectValues.songs?.?[#this.length > 10]}")
    private List<String> songs;

    public static void main(String... args) {
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(InjectValues.class, InjectSpELSimpleDemo.class);
        ctx.refresh();

        var simple = (InjectSpELSimpleDemo) ctx.getBean("injectSimpleSpEL");
        out.println(simple);
    }

    @Override
    public String toString() {
        return "InjectSpELSimpleDemo {" +
                "\n name='" + name + '\'' +
                "\n age=" + age +
                "\n height=" + height +
                "\n developer=" + developer +
                "\n ageInSeconds=" + ageInSeconds +
                "\n songs=" + songs +
                '}';
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }
}

@Component("injectValues")
///
/// Listing 2-32
///
class InjectValues {
    private String name = "John Mayer";
    // test SpEL support for null values
    //private String name = null;
    private int age = 40;
    private float height = 1.92f;
    private boolean developer = false;
    private Long ageInSeconds = 1_241_401_112L;

    // test SpEL support for Optional
    //private Optional<List<String>> songs = Optional.empty();
    //private Optional<List<String>> songs = Optional.ofNullable(null);
    private Optional<List<String>> songs = Optional.of(List.of(
            "Helpless",  "Waiting on the World to Change", "Vultures", "Clarity", "Edge of Desire", "Love on the Weekend"
    ));

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isDeveloper() {
        return developer;
    }

    public void setDeveloper(boolean developer) {
        this.developer = developer;
    }

    public Long getAgeInSeconds() {
        return ageInSeconds;
    }

    public void setAgeInSeconds(Long ageInSeconds) {
        this.ageInSeconds = ageInSeconds;
    }

    public Optional<List<String>> getSongs() {
        return songs;
    }

    public void setSongs(Optional<List<String>> songs) {
        this.songs = songs;
    }
}
