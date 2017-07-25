package com.gsonkeno.hot.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * Created by gaosong on 2017-05-14.
 */
public interface IMessageHandler {
    void handleMessage(ConsumerRecords<String, String> record);
}
