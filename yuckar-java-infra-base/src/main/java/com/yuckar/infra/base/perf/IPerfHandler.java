package com.yuckar.infra.base.perf;

import java.util.List;

import com.yuckar.infra.base.spi.PkgSpi;

public interface IPerfHandler extends PkgSpi {

	void handle(List<PerfLogHolder> perfs);

}
