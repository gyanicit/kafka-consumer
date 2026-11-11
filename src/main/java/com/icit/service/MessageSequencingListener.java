package com.icit.service;

import com.icit.dto.OrderEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MessageSequencingListener {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    Logger logger = LoggerFactory.getLogger(MessageSequencingListener.class);

    @KafkaListener(topics = "orderTopic", groupId = "order-consumer-group")
    public void consumerOrder(OrderEvent orderEvent, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.OFFSET) int offset){
        logger.info("OrderEvent Consumer Consume the messages: {} from: {}, offset: {}", orderEvent.toString(), topic, offset);
    }

    @KafkaListener(topics = "orderTopic", groupId = "order-consumer-group-bulk", containerFactory = "batchKafkaListenerContainerFactory")
    public void consumerOrderBulkNonSequence(List<ConsumerRecord<String, OrderEvent>> records){
        for (ConsumerRecord<String, OrderEvent> record: records){
            executor.submit(()->{
                OrderEvent orderEvent = record.value();
                logger.info("OrderEvent Consumer (Parallel) Consume the messages: {} Topic: {}, partition: {}, offset: {}", orderEvent.toString(), record.topic(), record.partition(), record.offset());
            });

        }
    }

    @KafkaListener(topics = "orderTopic", groupId = "order-consumer-group-bulk-sequence", containerFactory = "batchKafkaListenerContainerFactory")
    public void consumerOrderBulkSequence(List<ConsumerRecord<String, OrderEvent>> records){
        Map<Integer,ExecutorService> executorServiceMap = new ConcurrentHashMap<>();
        for (ConsumerRecord<String, OrderEvent> record: records){
            executorServiceMap.computeIfAbsent(record.partition(), p-> Executors.newSingleThreadExecutor())
                    .submit(()->{
                        OrderEvent orderEvent = record.value();
                        logger.info("OrderEvent Consumer (Parallel-Sequence) Consume the messages: {} Topic: {}, partition: {}, offset: {}", orderEvent.toString(), record.topic(), record.partition(), record.offset());
                    });

        }
    }
}
