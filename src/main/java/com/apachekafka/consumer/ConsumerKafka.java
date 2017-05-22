package com.apachekafka.consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.internals.SubscriptionState;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerKafka {
    
    /**
	 *  We pass this properties to the Kafka Consumer. 
	 * 
	 *  We need this to provide Broker address against this property. 
	 *  Kafka Consumer works differently from Kafka-Producers. Kafka Consumers 
	 *  first interrect with the zookeeper and get the list of Broker address
	 *  and then connect it on properly Topic.
	 * 
	 *  We need this to provide Broker address against this property.
	 *  Kafka Consumer works differently from Kafka-Producers. 
	 *  Kafka Consumers first interrect with the zookeeper
	 *  and get the list of Broker address and then connect it 
	 *  on properly Topic.
	 *  
	 *  This property instructs how to deserializer the key that was published from the producer. 
	 *  This needs to match with the one in the producer.(now we pass deserializer - in Producer case we pass serializer).
	 * 
	 *  This is similar to key.deserializer. But the actual payload will be read from this one.
	 *  This needs to match with the one with the producer.
	 *  
	 *  Group id is a unique string that identifies the consumer group this consumer belongs to. 
	 *  This is a concept which is use for scaling consumers.
	 *  
	 *  KafkaConsumer class is class which actually make connection to the Kafka Broker.This calss is required
	 *  and we have the pass the mandatory properties/attributes to be available to connect on it.
	 *  
	 *  Provide the topic from which we want to read the message.
	 *  
	 *  Pass the Topic list to the subscribe API. We can subscribe to any number of topics.
	 *  
	 *  Consumer records is returned from the poll call  -- Poll call actually starts the whole consumer flow.
	 *  Meaning this is a call which polls the records from the kafka topic.
	 *  
	 *  ConsumerRecords is object which hold.
	 *  ConsumerRecord is object which Hold the list of consumer record.
	 *    
	 */
	public static void main(String[] args) {
		
		Properties properties = new Properties();                      
		properties.put("bootstrap.servers", "localhost:9092");         
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");   
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");   
        properties.put("group.id", "test1");                         
        
        KafkaConsumer< String, String> consumer = new KafkaConsumer<String, String>(properties);      
        
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("my-demo-topic");                                
        
        consumer.subscribe(topics);                                  
        
        try {
        	
        while(true){
				
        	
				ConsumerRecords<String, String> records = consumer.poll(1000);       
				
				for(ConsumerRecord<String, String> record : records){                 
					
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
    
}
