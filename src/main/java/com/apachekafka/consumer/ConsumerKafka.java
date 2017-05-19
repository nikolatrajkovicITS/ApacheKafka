package com.apachekafka.consumer;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerKafka {

	public static void main(String[] args) {
		
		Properties properties = new Properties();                    // We pass this properties to the Kafka Consumer   
		properties.put("bootstrap.servers", "localhost:9092");       // We need this to provide Broker address against this property. Kafka Consumer works differently from Kafka-Producers. Kafka Consumers first interrect with the zookeeper and get the list of Broker address and then connect it on properly Topic 
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  // This property instructs how to deserialize the key that was published from the producer. This needs to match with the one in the producer.(now we pass deserializer - in Producer case we pass serializer)
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  // This is similar to key.deserializer. But the actual payload will be read from this one. This needs to match with the one with the producer.
        properties.put("group.id", "test1");     // Gruop id is a unique string that identifies the consumer group this consumer belongs to. This is a concept which is use for scaling consumers.
        
        KafkaConsumer< String, String> consumer = new KafkaConsumer<String, String>(properties);     // KafkaConsumer class is class which actually make connection to the Kafka Broker.This calss is required and we have the pass the mandatory properties/attributes da bi mogli da napravimo konekciju  
        
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("my-demo-topic");    // Provide the topic from which we want to read the messeage
        
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
}
