package com.yuckar.infra.common.json;

import static com.fasterxml.jackson.core.JsonFactory.Feature.INTERN_FIELD_NAMES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.type.TypeFactory.defaultInstance;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.yuckar.infra.common.logger.LoggerUtils;
import com.yuckar.infra.common.scanner.Scanner;
import com.yuckar.infra.common.thread.utils.ThreadHelper;

@SuppressWarnings("deprecation")
public class JsonUtils {
	private static final Logger LOGGER = LoggerUtils.getLogger(JsonUtils.class);
	private static final ObjectMapper MAPPER;
	static {
		MAPPER = new ObjectMapper(new JsonFactory().disable(INTERN_FIELD_NAMES)) //
				.disable(FAIL_ON_UNKNOWN_PROPERTIES) //
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS) //
				.enable(ALLOW_UNQUOTED_CONTROL_CHARS) //
				.enable(ALLOW_COMMENTS) //
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		Scanner.of(str -> {
			String cls = str.replace("/", ".");
			try {
				Class<?> clazz = Class.forName(cls);
				if (Module.class.isAssignableFrom(clazz) && Module.class != clazz) {
					registerModule((Module) clazz.getDeclaredConstructor(new Class<?>[0]).newInstance(new Object[0]));
					LOGGER.info("module:{} succ", cls);
				}
			} catch (Throwable e) {
				LOGGER.info("module:" + cls + " fail");
				LOGGER.debug("module:" + cls + " fail", e);
			}
		}, ThreadHelper.getContextClassLoader()) //
				.scan("com/fasterxml/jackson", "(com/fasterxml/jackson.+Module)\\.class$")
				.scan("com/hubspot/jackson/datatype/protobuf",
						"(com/hubspot/jackson/datatype/protobuf.+Module)\\.class$");
	}

	public static void registerModule(Module module) {
		MAPPER.registerModule(module);
	}

	public static String toJson(@Nullable Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			return MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toPrettyJson(@Nullable Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(@Nullable String json, Class<T> valueType) {
		if (json == null) {
			return null;
		}
		try {
			return MAPPER.readValue(json, valueType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(@Nullable byte[] bytes, Class<T> valueType) {
		if (bytes == null) {
			return null;
		}
		try {
			return MAPPER.readValue(bytes, valueType);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJson(InputStream inputStream, Class<T> type) {
		if (inputStream == null) {
			return null;
		}
		try {
			return MAPPER.readValue(inputStream, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <E> List<E> fromListJson(String json, Class<E> valueType) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		try {
			return MAPPER.readValue(json, defaultInstance().constructCollectionType(List.class, valueType));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <E> List<E> fromListJson(byte[] bytes, Class<E> valueType) {
		if (bytes == null) {
			return null;
		}
		try {
			return MAPPER.readValue(bytes, defaultInstance().constructCollectionType(List.class, valueType));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <E> List<E> fromListJson(InputStream inputStream, Class<E> valueType) {
		if (inputStream == null) {
			return null;
		}
		try {
			return MAPPER.readValue(inputStream, defaultInstance().constructCollectionType(List.class, valueType));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <K, V> Map<K, V> fromMapJson(String json, Class<K> keyType, Class<V> valueType) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		try {
			return MAPPER.readValue(json, defaultInstance().constructMapType(Map.class, keyType, valueType));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <K, V> Map<K, V> fromMapJson(byte[] bytes, Class<K> keyType, Class<V> valueType) {
		if (bytes == null) {
			return null;
		}
		try {
			return MAPPER.readValue(bytes, defaultInstance().constructMapType(Map.class, keyType, valueType));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static <K, V> Map<K, V> fromMapJson(InputStream inputStream, Class<K> keyType, Class<V> valueType) {
		if (inputStream == null) {
			return null;
		}
		try {
			return MAPPER.readValue(inputStream, defaultInstance().constructMapType(Map.class, keyType, valueType));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T value(Object rawValue, Class<T> type) {
		if (rawValue == null || type == null) {
			return null;
		}
		if (type.isAssignableFrom(rawValue.getClass())) {
			return (T) rawValue;
		}
		return MAPPER.convertValue(rawValue, type);
	}

	public static <T> T value(Object rawValue, TypeReference<T> type) {
		if (rawValue == null || type == null) {
			return null;
		}
		return MAPPER.convertValue(rawValue, type);
	}

	public static <T> T value(Object rawValue, JavaType type) {
		if (rawValue == null || type == null) {
			return null;
		}
		return MAPPER.convertValue(rawValue, type);
	}

	@SuppressWarnings("unchecked")
	public static <T> T value(Object rawValue, Type type) {
		if (rawValue == null || type == null) {
			return null;
		}
		if (type instanceof Class<?>) {
			return value(rawValue, (Class<T>) type);
		}
		return MAPPER.convertValue(rawValue, MAPPER.constructType(type));
	}
}
