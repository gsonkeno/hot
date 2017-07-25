package com.gsonkeno.hot.kafka;

import java.util.Properties;

/**
 * Created by gaosong on 2017-05-14.
 */
public class ProducerConfig {
    Properties props = new Properties();


    ProducerConfig(String bootStrapServer ){
        props.put("bootstrap.servers", bootStrapServer);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    public Properties getProps() {
        return props;
    }
}
