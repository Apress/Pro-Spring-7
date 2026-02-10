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
package com.apress.prospring7.boot.seventeen;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

///
/// @author iulianacosmina on 09/02/2026
///
@Service
public class LetterReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(LetterReader.class);
    /*
    @Value("#{kafkaApplication.receivingTopic}")
    private String receivingTopicName; // who is receiving the letter

    @KafkaListener(topics = "#{kafkaApplication.receivingTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "json",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload Letter letter) {
        log.info("<<<< [{}] Reading letter -> {}", receivingTopicName, letter);
    }*/

    @KafkaListener(topics = "#{kafkaApplication.receivingTopic}", // who is receiving the letter
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "json",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, Letter> cr) {
        LOGGER.info("<<<< Receiving message at -> {}", cr.timestamp());
        LOGGER.info("<<<< Receiving message on topic -> {}", cr.topic());
        LOGGER.info("<<<< Receiving message on partition -> {}", cr.partition());
        LOGGER.info("<<<< Receiving message with headers -> {}", cr.headers());
        LOGGER.info("<<<< Receiving message with key -> {}", cr.key());
        LOGGER.info("<<<< Receiving message with value -> {}", cr.value());
    }
}
