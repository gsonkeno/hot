package com.gsonkeno.hot.kafka;

import java.util.Properties;

/**
 * Created by gaosong on 2017-05-14.
 */
public class ConsumerConfig {

    Properties props = new Properties();

    /**
     *  订阅-消费模式下，消息只能被消费组内的一个消费者消费，但是消息可以传递进多个不同的消费组
     */

    ConsumerConfig(String bootstrapServers,String groupId){
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }

    public Properties getProps() {
        return props;
    }
}
