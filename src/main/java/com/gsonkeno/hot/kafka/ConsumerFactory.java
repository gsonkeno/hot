package com.gsonkeno.hot.kafka;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaosong on 2017-05-14.
 */
public class ConsumerFactory {
    private Map<String,ConsumerRunner> consumerBulk = new HashMap<>();

    void addConsumer(String topicName, String groupId, ConsumerRunner consumerRunner){
        String key = topicName + "-" + groupId;

        consumerBulk.put(key,consumerRunner);
    }

    void removeConsumer(String topicName, String groupId){
        String key = topicName + "-" + groupId;

        if (consumerBulk.containsKey(key)){
            ConsumerRunner consumer = consumerBulk.get(key);
            consumer.stopConsume();
            consumerBulk.remove(key);
        }

    }

    Map<String, ConsumerRunner> getConsumerBulk() {

        return consumerBulk;
    }
}
