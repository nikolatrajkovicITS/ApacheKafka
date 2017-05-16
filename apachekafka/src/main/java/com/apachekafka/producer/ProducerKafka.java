package com.apachekafka.producer;

import java.util.Properties;

public class ProducerKafka {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("bootstrap.servers", "localhost:9092");
	}
}
