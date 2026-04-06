package com.yuckar.infra.spring.beaninfo;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.support.DefaultConversionService;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.yuckar.infra.base.function.Functions;

public class SpringBeanInfoHelper {

	public static Map<String, PropertyDescriptor> descriptorMap(Class<?> clazz) {
		return Stream.of(BeanUtils.getPropertyDescriptors(clazz))
				.collect(Collectors.toMap(PropertyDescriptor::getName, Functions.identity()));
	}

	public static <T> T value(Class<T> clazz, Map<String, Object> valueMap) {
		T obj = BeanUtils.instantiateClass(clazz);
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		wrapper.setConversionService(DefaultConversionService.getSharedInstance());
		valueMap.forEach((key, val) -> {
			wrapper.setPropertyValue(key, val);
		});
		return obj;
	}

}
