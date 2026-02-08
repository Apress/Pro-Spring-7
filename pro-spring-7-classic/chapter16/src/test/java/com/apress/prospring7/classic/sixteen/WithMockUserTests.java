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
package com.apress.prospring7.classic.sixteen;

import com.apress.prospring7.classic.sixteen.ex.NotFoundException;
import com.apress.prospring7.classic.sixteen.services.SingerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

///
/// @author iulianacosmina on 07/02/2026
///
@Disabled("This test need the database container to be up and running.")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
public class WithMockUserTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private SingerService singerService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // (1)
                .build();
    }

    @Test
    void testAttemptServiceDeleteUnauthenticated() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> singerService.delete(1L));
    }

    @Test
    @WithUserDetails("john")
    void testAttemptServiceDeleteNonAdmin() {
        assertThrows(AuthorizationDeniedException.class, () -> singerService.delete(1L));
    }

    ///  Run this once then change the id
    @Test
    @WithUserDetails("admin")
    void testAttemptServiceDeleteAdmin() {
        singerService.delete(13L);
        assertThrows(NotFoundException.class, () -> singerService.findById(13L));
    }


    @Test
    void accessMainPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        .with(user("john").password("doe"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Spring MVC Thymeleaf Example!!")));
    }

    @Test
    @WithMockUser(username = "john")
    void accessMainPage1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                        //.with(user("john").password("doe"))
                 )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Spring MVC Thymeleaf Example!!")));
    }

    @Test
    @WithUserDetails("john")
    void accessMainPage2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Spring MVC Thymeleaf Example!!")));
    }

    @Test
    @WithAnonymousUser
    void accessMainPage3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andDo(print())
                .andExpect(status().isFound());
    }


    @Test
    void deleteSinger() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/singer/1")
                        .with(user("john").password("doe"))
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(view().name("error"));
    }


}
