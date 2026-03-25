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
package com.apress.prospring7.boot.nineteen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.Optional;

///
/// @author iulianacosmina on 16/03/2026
///
@SpringBootApplication
public class SpringAiApplication {

    static void main(String[] args) {
        SpringApplication.run(SpringAiApplication.class, args);
    }

}

record PromptHolder(String prompt) {}

@RestController
class ChatController {

    @GetMapping(path = {"/" , "/home"}, produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return "{\"message\", \"Spring AI application with Ollama!!\"}";
    }

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @PostMapping(path="/")
    String rnd(@RequestBody PromptHolder input){
        final var proptTemplate = new PromptTemplate(input.prompt());

        return this.chatClient.prompt(proptTemplate.create())
                .call()
                .content();
    }

}

@RestController
@RequestMapping("/images")
class ImageController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final ChatClient chatClient;

    public ImageController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/describe/{image}")
    String[] describeImage(@PathVariable String image) {
        Media media = Media.builder()
                .id(image)
                .mimeType(MimeTypeUtils.IMAGE_PNG)
                .data(new ClassPathResource("images/" + image + ".jpg"))
                .build();

        UserMessage um = UserMessage.builder().text("""
        List all items you see on the image and define their category.
        Return items inside the JSON array as strings.
        """).media(media).build();

        String[] result = this.chatClient.prompt(new Prompt(um))
                .call()
                .entity(String[].class);
        LOGGER.info("Result: {} ",  String.join(",", result));
        return result;
    }
}
