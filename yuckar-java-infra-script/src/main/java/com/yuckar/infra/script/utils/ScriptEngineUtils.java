package com.yuckar.infra.script.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.slf4j.Logger;

import com.yuckar.infra.base.logger.LoggerUtils;

public class ScriptEngineUtils {

	private static final Logger logger = LoggerUtils.logger(ScriptEngineUtils.class);
	private static final ScriptEngineManager MANAGER = new ScriptEngineManager();

	static {
		MANAGER.getEngineFactories().forEach(factory -> {
			logger.info("factory:{} supported-engine:{}", factory.getEngineName(), factory.getNames());
		});
	}

	public static ScriptEngine engine(String name) {
		return MANAGER.getEngineByName(name);
	}

	public static void main(String[] args) {

	}

}
