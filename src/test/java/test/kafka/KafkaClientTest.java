package test.kafka;


import com.gsonkeno.hot.kafka.IMessageHandler;
import com.gsonkeno.hot.kafka.KafkaClient;
import com.gsonkeno.hot.kafka.ProducerRecordWrapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * Created by gaosong on 2017-05-13.
 */
public class KafkaClientTest {

    @Test
    public void sendMessage() throws ExecutionException, InterruptedException {
        int i = 1;
        while (true){
            ProducerRecordWrapper wrapper = ProducerRecordWrapper.builder().topicName("test").key("key" + i).value("value" + i).build();

            KafkaClient kafkaClient = new KafkaClient("192.168.10.1:9092");
            kafkaClient.sendMessage(wrapper);
            i++;
        }

    }

    @Test
    public void consumeMessage() throws InterruptedException {
        KafkaClient kafkaClient = new KafkaClient("192.168.10.1:9092");
        int[] partitions = {1};
        kafkaClient.consumeMessage("test", "first",partitions, new IMessageHandler() {
            @Override
            public void handleMessage(ConsumerRecords<String, String> records) {
                for (ConsumerRecord<String,String> record:records) {

                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                }
            }
        },true);

    }

    @Test
    public void calculateOffset(){
        System.out.println("test-consumer-group".hashCode()%50);
    }


}
