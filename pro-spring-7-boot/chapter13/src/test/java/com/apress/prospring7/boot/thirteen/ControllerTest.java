/*
Freeware License, some rights reserved

Copyright (c) 2026 Iuliana Cosmina

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
package com.apress.prospring7.boot.thirteen;

import com.apress.prospring7.boot.thirteen.controllers.HomeController;
import com.apress.prospring7.boot.thirteen.controllers.SingersController;
import com.apress.prospring7.boot.thirteen.entities.Singer;
import com.apress.prospring7.boot.thirteen.services.SingerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

///
/// @author iulianacosmina on 17/01/2026
///
@WebMvcTest(controllers = { HomeController.class, SingersController.class })
public class ControllerTest {
    public static List<Singer> singers;

    @BeforeAll
    static void setUp(){
        singers = new ArrayList<>();
        var nick = new Singer();
        nick.setId(1L);
        nick.setFirstName("Nick");
        nick.setLastName("Drake");
        nick.setBirthDate(LocalDate.of(1948,6, 19));
        singers.add(nick);

        var john = new Singer();
        john.setId(2L);
        john.setFirstName("John");
        john.setLastName("Mayer");
        john.setBirthDate(LocalDate.of(1977,10, 16));
        singers.add(john);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SingerService service;

    @Test
    void testHome() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Spring Boot Thymeleaf Example!!")));
    }

    @Test
    void testList() throws Exception {
        when(service.findAll()).thenReturn(singers);

        mockMvc.perform(MockMvcRequestBuilders.get("/singers/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("singers/list"));
    }

    @Test
    public void testUnsupportedUrl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/aaaa"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
