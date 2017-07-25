package com.gsonkeno.hot.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by gaosong on 2017-05-13.
 */
public class KafkaClient {

    private String bootStrapServer;

    private Producer<String,String> producer;

    private ConsumerFactory factory = new ConsumerFactory();

    public void sendMessage(ProducerRecordWrapper recordWrapper) throws ExecutionException, InterruptedException {

        Future<RecordMetadata> send = producer.send(recordWrapper.getRecord());
        producer.close();

    }

    /**
     * 消费主题
     * @param topicName 消费主题
     * @param groupId  组id
     * @param partitions 消费分组
     * @param handler 消息处理器
     * @param isBlock 此方法是否阻塞
     * @throws InterruptedException
     */
    public void consumeMessage(String topicName, String groupId,int[] partitions, IMessageHandler handler,boolean isBlock) throws InterruptedException {
        String key = topicName + "-" + groupId;
        if (factory.getConsumerBulk().containsKey(key)) return;

        ConsumerRunner consumerRunner = new ConsumerRunner(bootStrapServer, groupId,topicName,partitions,handler);

        new Thread(consumerRunner).start();

        factory.getConsumerBulk().put(key,consumerRunner);

        if (isBlock) new CountDownLatch(1).await();
    }

    /**
     * 移除消费者
     * @param topicName 消费主题
     * @param groupId   消费组id
     */
    public void removeConsumer(String topicName, String groupId){

        factory.removeConsumer(topicName,groupId);
    }




    public KafkaClient(String bootstrapServer){

        this.bootStrapServer = bootstrapServer;
        ProducerConfig producerConfig = new ProducerConfig(bootstrapServer);
        this.producer = new KafkaProducer<String, String>(producerConfig.getProps());
    }
}
