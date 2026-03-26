package com.yuckar.infra.runner.sch;

import com.yuckar.infra.runner.Runner;

public interface SchRunner extends Runner {

	default boolean isConcurrentRunning() {
		return false;
	}

}
