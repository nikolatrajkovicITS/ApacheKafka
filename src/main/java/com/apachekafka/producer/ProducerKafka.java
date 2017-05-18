package com.apachekafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerKafka {

	/**
	 *  
	 *  This 3 attributes are required be available to connect to the Kafka-Broker:
	 * 
	 *  @bootstrap.servers You need to provide the Broker address against this property. 
	 *  -Now Kafka Producer knows on which Broker needs to connect to.  
	 *  
	 *  @key.serializer The key is the one which decides which partition this particular
	 *  messages going to be allocated. This property instructs how to 
	 *  turn the key objects the user provides with their ProducerRecord into bytes.
	 *  <b>-What they mean by Producer-Records? - We need to mention which serializer 
	 *  is used in this Producer, so that Consumer can create a properly deserializer object
	 *  and Consumer end to this serializer</b>
	 *  
	 *  @value.serializer This is similar as key.serializer. But the actual payload 
	 *  will be send in this one. 
	 *  @param args
	 */
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");    
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");                        
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		/**
		 * Kafka-Producer instance - This is the instance to which we will pass the properties.
		 * Properties are the once which holds all the Broker details and the key and value serializer.
		 * Now when we craete Kafka-Producer instance and connection available to the Kafka-Broker
		 */
		KafkaProducer<String, String> myProducer = new KafkaProducer<String, String>(properties);
		
		try {
	
			for(int i = 50; i <= 75; i++) {
				myProducer.send(new ProducerRecord<String, String>("my-demo-topic", "Message Value : " + Integer.toString(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			myProducer.close();
		}
	}
}
