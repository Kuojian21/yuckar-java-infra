package com.yuckar.infra.runner.sch.quatz;

import com.yuckar.infra.runner.sch.SchRunner;

public interface QuatzRunner extends SchRunner {

	void run() throws Exception;

	String cron();

	default String[] crons() {
		return new String[] { cron() };
	}

}
