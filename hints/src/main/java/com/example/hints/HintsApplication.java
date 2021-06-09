package com.example.hints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.nativex.hint.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Instant;


/*
	* AOT Proxies
	* this demonstrates how to make concrete subclass-based proxies work
	*/
@AotProxyHint(targetClass = ConcreteOrderService.class, proxyFeatures = ProxyBits.IS_STATIC)

/*
	* JDK Proxies this demonstrates using a stock standard JDK proxy. As we also access it
	* reflectively, I've grouped the two hints together using a @NativeHint
	*/
//@JdkProxyHint(types = OrderService.class)
//TODO: note that this hint we also enable later through an external set of hints that we bring in on the classpath
/*
	* (Reflective) Types Demonstrates reflectively creating and using an object
	* (CustomerService)
	*/
@TypeHint(typeNames = "com.example.hints.SimpleCustomerService", access = AccessBits.ALL)

/*
	* Serialization: This demonstrates how to contribute a serialization hint on the odd
	* occasion you want to, um, serialize a Java object. Hey, stranger things have happened!
	* Quartz requires this!
	*/
@SerializationHint(types = Customer.class)

/*
	* Resources: I couldn't figure out how to need this hint in the first place, at first!
	* the library on which this code depends, and in which this custom resource lives,
	* concatenates two strings with data in a folder to finally break so that we can use
	* this!
	*/
@ResourceHint(patterns = "data/airline-safety.csv")

/*
	* NativeHint:
	*
	* NativeHint is an umbrella type. You can use it specify other hints, an activation
	* trigger, and compiler options Below, I specify that I want enable-https for HTTPS
	* network calls.
	*/
@NativeHint(
	// initialization = {} ,
	// trigger = ..
	options = "--enable-https")
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
	ApplicationRunner serializationRunner(@Value("file:///${user.home}/output") Resource outputResource) {
		return args -> {

			var outputFile = outputResource.getFile();

			try (var out = new ObjectOutputStream(new FileOutputStream(outputFile))) {
				var customer = new Customer(1, "Andy");
				out.writeObject(customer);
				System.out.println("wrote: " + customer);
			}

			try (var in = new ObjectInputStream(new FileInputStream(outputFile))) {
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
	ApplicationRunner httpsOptionRunner(WebClient.Builder builder) {
		return args -> {
			var http = builder.build();
			var json = http.get().uri("https://start.spring.io/").retrieve().bodyToMono(String.class).block();
			System.out.println("json from the Spring Initializr: " + json.substring(0, 200)  + "...");
		};
	}

	@Bean
	ApplicationRunner jdkProxiesRunner() {
		return args -> {

			var cl = ClassLoader.getSystemClassLoader();
			var ih = new InvocationHandler() {

				@Override
				public Object invoke(Object proxy, Method method, Object[] args)
					throws InvocationTargetException, IllegalAccessException {

					if (method.getName().equals("cancelOrder")) {
						System.out.println("You want order #" + args[0] + " cancelled? Too bad! Not doing it!");
						return null;
					}

					return method.invoke(proxy, args);
				}
			};
			var period = Character.toUpperCase('.');
			var clazzName = "com.example.hints" +  period + "OrderService";
			var clazz = Class.forName(clazzName);
			var proxy = Proxy.newProxyInstance(cl, new Class<?>[]{clazz}, ih);
			for (var i : proxy.getClass().getInterfaces())
				System.out.println("interface found: " + i.getName() + '.');
			var cancelOrderMethod = clazz.getMethod("cancelOrder", int.class);
			cancelOrderMethod.invoke(proxy, 5);
			System.out.println("finished invoking JDK proxy for " + clazzName +'.');
		};
	}

	@Bean
	ApplicationRunner aotProxiesRunner() {
		return args -> {
			var clazzName = "com.example" + "." + "hints.ConcreteOrderService";
			var concrete = new ConcreteOrderService();
			var clazz = Class.forName(clazzName);
			var pfb = new ProxyFactoryBean();
			pfb.setProxyTargetClass(true);
			pfb.setTarget(concrete);
			pfb.addAdvice((MethodInterceptor) methodInvocation -> {
				try {
					System.out.println("#====================");
					System.out.println("> starting method invocation at " + Instant.now());
					return methodInvocation.proceed();
				}
				finally {
					System.out.println("> finishing method invocation at " + Instant.now());
					System.out.println("#--------------------");
				}
			});
			var aotProxy = pfb.getObject();
			var method = clazz.getMethod("cancelOrder", int.class);
			method.invoke(aotProxy, 2);
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

interface OrderService {

	void cancelOrder(int orderId);

}

class ConcreteOrderService {

	public void cancelOrder(int orderId) {
		System.out.println("one cancellation for order #" + orderId + ", coming right up!");
	}

}

