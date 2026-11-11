package com.icit.service;

import com.icit.dto.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {

    Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);


    @KafkaListener(topics = "testTopic", groupId = "my-group-1")
    public void consumer1(String message){
        logger.info("consumer1 Consume the messages: {}", message);
    }

    @KafkaListener(topics = "testTopic", groupId = "my-group-1")
    public void consumer2(String message){
        logger.info("consumer2 Consume the messages: {}", message);
    }

    @KafkaListener(topics = "testTopic", groupId = "my-group-1")
    public void consumer3(String message){
        logger.info("consumer3 Consume the messages: {}", message);
    }

    @RetryableTopic(attempts = "4", numPartitions = "5", backOff = @BackOff(delay = 3000, multiplier = 1.5, maxDelay = 15000), exclude = {NullPointerException.class}) //N-1
    @KafkaListener(topics = "studentTopic", groupId = "student-consumer-group")
    public void consumerStudent(Student student, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) int offset){
        logger.info("Student Consumer Consume the messages: {} from: {}, offset: {}", student.toString(), topic, offset);
        if(student.getRollNumber()==0){
            throw new RuntimeException("Roll number 0 is not valid");
        }
    }

    @DltHandler
    public void listenDLT(Student student, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) int offset){
        logger.info("DLT Received the messages: {} from: {}, offset: {}", student.toString(), topic, offset);
    }
}