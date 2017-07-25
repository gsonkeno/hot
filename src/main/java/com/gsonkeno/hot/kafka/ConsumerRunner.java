package com.gsonkeno.hot.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gaosong on 2017-05-14.
 */
public class ConsumerRunner implements Runnable {


    private KafkaConsumer<String,String > consumer;

    private String topicName;

    private IMessageHandler handler;

    private boolean isConsuming; //default value is false

    private int[] partitions;

    ConsumerRunner(String bootstrapServers, String groupId, String topicName,int[] partitions, IMessageHandler handler){

        ConsumerConfig config = new ConsumerConfig(bootstrapServers,groupId);
        consumer = new KafkaConsumer<String, String>(config.getProps());
        this.topicName = topicName;
        this.handler = handler;
        this.partitions = partitions;
    }

    void stopConsume(){
        isConsuming = false;
        consumer.unsubscribe();
    }



    @Override
    public void run() {
        isConsuming = true;
        if (partitions != null && partitions.length >0){
            List<TopicPartition> partitionList = new ArrayList<>();
            for (int partition: partitions) {
                TopicPartition topicPartition = new TopicPartition(topicName, partition);
                partitionList.add(topicPartition);

            }
            consumer.assign(partitionList);
        }else{
            consumer.subscribe(Arrays.asList(topicName));
        }

        while (isConsuming){
            ConsumerRecords<String, String> records = consumer.poll(1000);
            handler.handleMessage(records);
        }
    }
}
