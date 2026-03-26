package com.yuckar.infra.server.monitor;

import com.yuckar.infra.monitor.startup.Monitor;
import com.yuckar.infra.server.startup.Startable;

public class MonitorStartable implements Startable {

	@Override
	public void startup() throws Exception {
		Monitor.start();
	}

}
