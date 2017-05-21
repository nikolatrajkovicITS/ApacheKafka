package com.apachekafka.consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerKafka {
	
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private static final long NO_CURRENT_THREAD = -1L;
    
	 // currentThread holds the threadId of the current thread accessing KafkaConsumer
    // and is used to prevent multi-threaded access
    private final AtomicLong currentThread = new AtomicLong(NO_CURRENT_THREAD);
    // refcount is used to allow reentrant access by the thread who has acquired currentThread
    private final AtomicInteger refcount = new AtomicInteger(0);

    
	public static void main(String[] args) {
		
		Properties properties = new Properties();                    // We pass this properties to the Kafka Consumer   
		properties.put("bootstrap.servers", "localhost:9092");       // We need this to provide Broker address against this property. Kafka Consumer works differently from Kafka-Producers. Kafka Consumers first interrect with the zookeeper and get the list of Broker address and then connect it on properly Topic 
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  // This property instructs how to deserialize the key that was published from the producer. This needs to match with the one in the producer.(now we pass deserializer - in Producer case we pass serializer)
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  // This is similar to key.deserializer. But the actual payload will be read from this one. This needs to match with the one with the producer.
        properties.put("group.id", "test1");     // Group id is a unique string that identifies the consumer group this consumer belongs to. This is a concept which is use for scaling consumers.
        
        KafkaConsumer< String, String> consumer = new KafkaConsumer<String, String>(properties);     // KafkaConsumer class is class which actually make connection to the Kafka Broker.This calss is required and we have the pass the mandatory properties/attributes da bi mogli da napravimo konekciju  
        
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("my-demo-topic");    // Provide the topic from which we want to read the message
        
        consumer.subscribe(topics);  // Pass the Topic list to the subscribe API. We can subscribe to any number of topics.
        
        try {
        	
        while(true){
				
        	    // Consumer records is returned from the poll call  -- Poll call actually starts the whole consumer flow. Meanning this is a call which polls the records from the kafka topic 
				ConsumerRecords<String, String> records = consumer.poll(1000);        // ConsumerRecords is object which hold 
				
				for(ConsumerRecord<String, String> record : records){               // ConsumerRecord is object which Hold the list of consumer record
					
					System.out.println("Record read in KafkaConsumerApp : " +  record.toString());
					
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Inside exception loop : ");
			e.printStackTrace();
		}finally{
			consumer.close();
		}
	}
	
	
	/**
     * Get the set of partitions currently assigned to this consumer. If subscription happened by directly assigning
     * partitions using {@link #assign(Collection)} then this will simply return the same partitions that
     * were assigned. If topic subscription was used, then this will give the set of topic partitions currently assigned
     * to the consumer (which may be none if the assignment hasn't happened yet, or the partitions are in the
     * process of getting reassigned).
     * @return The set of partitions currently assigned to this consumer
     */
    public Set<TopicPartition> assignment() {
        acquire();
        try {
            return Collections.unmodifiableSet(new HashSet<>(this.subscriptions.assignedPartitions()));
        } finally {
            release();
        }
    }
    
    /**
     * Acquire the light lock protecting this consumer from multi-threaded access. Instead of blocking
     * when the lock is not available, however, we just throw an exception (since multi-threaded usage is not
     * supported).
     * @throws IllegalStateException if the consumer has been closed
     * @throws ConcurrentModificationException if another thread already has the lock
     */
    private void acquire() {
        ensureNotClosed();
        long threadId = Thread.currentThread().getId();
        if (threadId != currentThread.get() && !currentThread.compareAndSet(NO_CURRENT_THREAD, threadId))
            throw new ConcurrentModificationException("KafkaConsumer is not safe for multi-threaded access");
        refcount.incrementAndGet();
    }
    
    /**
     * Release the light lock protecting the consumer from multi-threaded access.
     */
    private void release() {
        if (refcount.decrementAndGet() == 0)
            currentThread.set(NO_CURRENT_THREAD);
    }
}
