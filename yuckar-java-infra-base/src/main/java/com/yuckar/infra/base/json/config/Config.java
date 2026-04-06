package com.yuckar.infra.base.json.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.common.collect.Maps;
import com.yuckar.infra.base.json.JsonUtils;
import com.yuckar.infra.base.logger.LoggerUtils;

public interface Config {

	Logger logger = LoggerUtils.logger(Config.class);

	<T> T config(T obj, Map<String, Object> jsons);

	@SuppressWarnings("unchecked")
	default <T> T config(T obj, Object jsons) {
		if (obj == null || jsons == null) {
			return obj;
		}
		if (jsons instanceof List) {
			return config(obj, Stream.of((List<Map<String, Object>>) jsons).flatMap(Stream::of)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Maps::newLinkedHashMap)));
		} else if (jsons instanceof Map) {
			return config(obj, (Map<String, Object>) jsons);
		} else {
			logger.error("error config values:{}", JsonUtils.toPrettyJson(jsons));
		}
		return obj;
	}

}
