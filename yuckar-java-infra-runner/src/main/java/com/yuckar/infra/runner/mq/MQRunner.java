package com.yuckar.infra.runner.mq;

import com.yuckar.infra.runner.Runner;

public interface MQRunner extends Runner {

	ITopic topic();

	IGroup group();

	default String ID() {
		return this.topic().topic();
	}

}
