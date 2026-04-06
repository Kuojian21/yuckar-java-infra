package com.yuckar.infra.monitor.mxbean;

import org.slf4j.Logger;

import com.yuckar.infra.base.logger.LoggerUtils;
import com.yuckar.infra.base.spi.ParamSpi;
import com.yuckar.infra.monitor.IMonitor;

public interface IMxbeanHandler<D extends IMxbeanHolder> extends ParamSpi<D> {

	Logger logger = LoggerUtils.logger(IMonitor.class);

	void handle(D bean);

}
