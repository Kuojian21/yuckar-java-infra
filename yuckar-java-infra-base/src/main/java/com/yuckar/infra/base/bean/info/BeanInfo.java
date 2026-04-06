package com.yuckar.infra.base.bean.info;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.yuckar.infra.base.function.Functions;
import com.yuckar.infra.base.lazy.LazySupplier;

public class BeanInfo {

	public static BeanInfo of(Class<?> clazz) throws IntrospectionException {
		return new BeanInfo(clazz);
	}

	private final Class<?> clazz;
	private final PropertyDescriptor[] descriptors;
	private final Map<String, PropertyDescriptor> descriptorMap;
	private final LazySupplier<Map<String, PropertyDescriptor>> columnDescriptorMap;
	private final LazySupplier<Field[]> fields;
	private final LazySupplier<Map<String, Field>> fieldMap;

	private BeanInfo(Class<?> clazz) throws IntrospectionException {
		this.clazz = clazz;
		this.descriptors = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
		this.descriptorMap = Stream.of(descriptors)
				.collect(Collectors.toMap(PropertyDescriptor::getName, Functions.identity()));
		this.columnDescriptorMap = LazySupplier.wrap(() -> Stream.of(descriptors)
				.collect(Collectors.toMap(
						p -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, p.getName()).toLowerCase(),
						Functions.identity())));
		this.fields = LazySupplier.wrap(() -> fields(clazz, Lists.newArrayList()).toArray(i -> new Field[i]));
		this.fieldMap = LazySupplier
				.wrap(() -> Stream.of(fields.get()).collect(Collectors.toMap(Field::getName, f -> f, (v1, v2) -> v2)));
	}

	public Class<?> clazz() {
		return this.clazz;
	}

	public Field field(String name) {
		return this.fieldMap.get().get(name);
	}

	public Field[] fields() {
		return this.fields.get();
	}

	public Map<String, Field> fieldMap() {
		return this.fieldMap.get();
	}

	public PropertyDescriptor descriptor(String name) {
		return this.descriptorMap.get(name);
	}

	public PropertyDescriptor[] descriptors() {
		return this.descriptors;
	}

	public Map<String, PropertyDescriptor> descriptorMap() {
		return this.descriptorMap;
	}

	public Map<String, PropertyDescriptor> columnDescriptorMap() {
		return this.columnDescriptorMap.get();
	}

	static List<Field> fields(Class<?> clazz, List<Field> fields) {
		if (Object.class.equals(clazz)) {
			return fields;
		}
		fields(clazz.getSuperclass(), fields);
		Stream.of(clazz.getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers())).forEach(fields::add);
		return fields;
	}

}