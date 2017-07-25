package com.gsonkeno.hot.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;

/**生产者生产记录(消息)包装器
 * Created by gaosong on 2017-05-13.
 */
public class ProducerRecordWrapper {

    //ProducerRecord构造器五大要素

    private String topicName;

    private Integer partition;

    private Long timestamp;

    private String key;

    private String value;

    private ProducerRecordWrapper(Builder builder) {
        this.topicName = builder.topName;
        this.partition = builder.partition;
        this.timestamp = builder.timestamp;
        this.key = builder.key;
        this.value = builder.value;
    }

    public ProducerRecord getRecord(){
        return new ProducerRecord(topicName,partition,timestamp,key,value);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String topName;
        private Integer partition;
        private Long timestamp;
        private String key;
        private String value;

        private Builder() {
        }

        public ProducerRecordWrapper build() {
            return new ProducerRecordWrapper(this);
        }

        public Builder topicName(String topName) {
            this.topName = topName;
            return this;
        }

        public Builder partition(int partition) {
            this.partition = partition;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }
    }
}
