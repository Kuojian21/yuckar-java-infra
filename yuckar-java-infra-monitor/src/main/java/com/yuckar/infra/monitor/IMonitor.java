package com.yuckar.infra.monitor;

import org.slf4j.Logger;

import com.yuckar.infra.base.logger.LoggerUtils;

public interface IMonitor {

	Logger logger = LoggerUtils.logger(IMonitor.class);

	void monitor();

}
