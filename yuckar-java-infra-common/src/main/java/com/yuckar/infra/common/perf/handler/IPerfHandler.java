package com.yuckar.infra.common.perf.handler;

import java.util.List;

import com.yuckar.infra.common.perf.model.PerfLogHolder;
import com.yuckar.infra.common.spi.PkgSpi;

public interface IPerfHandler extends PkgSpi {

	void handle(List<PerfLogHolder> perfs);

}
