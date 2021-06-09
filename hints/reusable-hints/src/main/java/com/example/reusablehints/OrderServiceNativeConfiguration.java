package com.example.reusablehints;

import org.springframework.nativex.domain.proxies.JdkProxyDescriptor;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.type.AccessDescriptor;
import org.springframework.nativex.type.HintDeclaration;
import org.springframework.nativex.type.NativeConfiguration;
import org.springframework.nativex.type.TypeSystem;

import java.util.List;


/**
	* Registers hints that we registered before.
	*/
public class OrderServiceNativeConfiguration implements NativeConfiguration {

	@Override
	public List<HintDeclaration> computeHints(TypeSystem typeSystem) {

		var hd = new HintDeclaration();
		var orderServiceClassName = "com.example.hints.OrderService";
		hd.addProxyDescriptor(new JdkProxyDescriptor(List.of(orderServiceClassName)));
		hd.addDependantType(orderServiceClassName, new AccessDescriptor(AccessBits.ALL));
		System.out.println("adding reflective and proxy hints for " + orderServiceClassName + '.');
		return List.of(hd);
	}
}
