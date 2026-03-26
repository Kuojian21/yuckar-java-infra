package com.yuckar.infra.perf.handler;

import java.util.List;

import com.yuckar.infra.common.spi.PkgSpi;
import com.yuckar.infra.perf.model.PerfLogHolder;

public interface IPerfHandler extends PkgSpi {

	void handle(List<PerfLogHolder> perfs);

}
