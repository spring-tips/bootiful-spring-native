package another.framework;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashSet;

///



class PoliteProxyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory clbf) throws BeansException {

		var cpcs = new ClassPathScanningCandidateComponentProvider(false,
			clbf.getBean(Environment.class));
		cpcs.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
			var clazzName = metadataReader.getClassMetadata().getClassName();
			var isInterface =
				metadataReader.getClassMetadata().isInterface();
			var hasPoliteProxyAnnotation = metadataReader
				.getAnnotationMetadata().hasAnnotation(PoliteProxy.class.getName());
			if (isInterface)	{
				System.out.println(
					"interface? " + isInterface + " " +
						"hasPoliteProxyAnnotation? " +hasPoliteProxyAnnotation	 + " " +
						"class: " + clazzName );
			}
			return false;
		});
		var packages = AutoConfigurationPackages.get(clbf);
		System.out.println("-------------");
		System.out.println(packages);
		System.out.println("-------------");

		var bds = new HashSet<BeanDefinition>();
		for (var pkg : packages) {
			var components = cpcs.findCandidateComponents(pkg);
			bds.addAll(components);
		}
		for (var bd : bds) {
			System.out.println("bd: " + bd.toString());
		}


	}
}