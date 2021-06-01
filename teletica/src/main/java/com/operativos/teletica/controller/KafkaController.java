package com.operativos.teletica.controller;

import com.operativos.teletica.consumer.topicConsumer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private KafkaTemplate<String, String> template;
    private topicConsumer topicConsumer;

    public KafkaController(KafkaTemplate<String, String> template, topicConsumer topicConsumer) {
        this.template = template;
        this.topicConsumer = topicConsumer;
    }

    @GetMapping("/kafka/produce")
    public void produce(@RequestParam String message) {
        template.send("myTopic", message);
    }
}
