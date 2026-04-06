package com.yuckar.infra.base.logger;

import org.slf4j.Logger;

public interface IKLoggerFactory {

	Logger getLogger(String name);

	default Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

}
