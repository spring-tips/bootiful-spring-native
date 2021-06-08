package com.example.hints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.ResourceHint;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/*
	* (Reflective) Types
	* Demonstrates reflectively creating and using an object (CustomerService)
	*/
@TypeHint(typeNames = "com.example.hints.SimpleCustomerService", access = AccessBits.ALL)

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
@SerializationHint(types = Customer.class)

@SpringBootApplication
public class HintsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HintsApplication.class, args);
	}

	@Bean
	ApplicationRunner resourceRunner() {
		return args -> {
			var a = "data/airline";
			var b = "safety.csv";
			var url = new ClassPathResource(a + "-" + b);
			try (var in = new InputStreamReader(url.getInputStream())) {
				var contents = FileCopyUtils.copyToString(in);
				var lines = contents.split("\r");
				System.out.println("there are " + lines.length + " lines");
			}
		};
	}

	@Bean
	ApplicationRunner serializationRunner(
		@Value("file:///${user.home}/output") Resource resource) {
		return args -> {
			var written = resource.getFile();


			try (var out = new ObjectOutputStream(new FileOutputStream(written))) {
				var customer = new Customer(1, "Andy");
				out.writeObject(customer);
				System.out.println("wrote: " + customer);
			}

			try (var in = new ObjectInputStream(new FileInputStream(written))) {
				var customer = (Customer) in.readObject();
				System.out.println("read: " + customer);
			}
		};
	}

	@Bean
	ApplicationRunner reflectionRunner() {
		return args -> {
			var clazzName = "com.example" + "." + "hints.SimpleCustomerService";
			var clazz = Class.forName(clazzName);
			var instance = clazz.getDeclaredConstructors()[0].newInstance();
			var methodForFindById = clazz.getMethod("findById", int.class);
			var result = methodForFindById.invoke(instance, 2);
			System.out.println("reflective result: " + result);
		};
	}

	@Bean
	ApplicationRunner jdkProxiesRunner() {
		return args -> {

			var cl = ClassLoader.getSystemClassLoader();
			var ih = new InvocationHandler() {

				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

					if (method.getName().equals("cancelOrder")) {
						System.out.println("You want order #" + args[0] + " cancelled? Too bad! Not doing it!");
						return null;
					}

					return method.invoke(proxy, args);
				}
			};
			var proxy = (OrderService) Proxy.newProxyInstance(cl,
				new Class<?>[]{OrderService.class}, ih);
			proxy.cancelOrder(2);

		};
	}
}

// this is still grey!
class SimpleCustomerService {

	public Customer findById(int id) {
		return new Customer(id, "Jorge");
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer implements Serializable {
	private int id;
	private String name;
}


class SimpleOrderService implements OrderService {

	@Override
	public void cancelOrder(int orderId) {
		System.out.println("cancelling #" + orderId + "!");
	}
}

interface OrderService {
	void cancelOrder(int orderId);
}

// need to demonstrate jdk proxies
// need to demonstrate spring proxies
