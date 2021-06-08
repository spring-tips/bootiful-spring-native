package com.example.hints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.nativex.hint.SerializationHint;

import java.io.*;


/*
	* Resources:
	* I couldn't figure out how to need this hint in the first place, at first!
	* the library on which this code depends, and in which this custom resource lives,
	* concatenates two strings with data in a folder to finally break so that we can use this!
	*/
@ResourceHint(patterns = "data/airline-safety.csv")

/*
	* Serialization:
	* this demonstrates how to contribute a serialization hint on the odd occasion you want
	* to, em, serialize a Java object. Hey, stranger things have happened! Quartz requires this!
	*/
@SerializationHint (types = Customer.class)

@SpringBootApplication
public class HintsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HintsApplication.class, args);
	}

	@Bean
	ApplicationRunner runner() {
		return event -> System.out.println("hello, world!");
	}

	@Bean
	ApplicationRunner serialization(
		@Value("file:///${user.home}/output") Resource resource) {
		return args -> {
			var written = resource.getFile();
			var customer = new Customer(1, "Andy");

			try (var out = new ObjectOutputStream(new FileOutputStream(written))) {
				out.writeObject(customer);
			}

			try (var in = new ObjectInputStream( new FileInputStream( written))) {
				var readCustomer = (Customer) in.readObject() ;
				System.out.println("read: " + readCustomer);
			}
		};
	}


}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer implements Serializable {


	private int id;
	private String name;

}

// need to demonstrate serialization
// need to demonstrate jdk proxies
// need to demonstrate spring proxies
// need to demonstrate