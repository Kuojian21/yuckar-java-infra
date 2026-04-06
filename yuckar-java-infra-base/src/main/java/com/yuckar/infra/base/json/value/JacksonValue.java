package com.yuckar.infra.base.json.value;

import java.lang.reflect.Type;

import com.yuckar.infra.base.json.JsonUtils;

public class JacksonValue implements Value<Type> {

	@Override
	public <V> V value(Object json, Type type) throws Exception {
		return JsonUtils.value(json, type);
	}

}
