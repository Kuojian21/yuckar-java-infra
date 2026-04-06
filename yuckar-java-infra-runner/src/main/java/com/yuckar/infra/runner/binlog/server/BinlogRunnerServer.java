package com.yuckar.infra.runner.binlog.server;

import java.io.IOException;
import java.util.concurrent.ThreadFactory;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yuckar.infra.base.lazy.LazySupplier;
import com.yuckar.infra.base.term.TermHelper;
import com.yuckar.infra.base.utils.RunUtils;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.runner.binlog.BinlogRunner;
import com.yuckar.infra.runner.binlog.holder.BinlogRunnerHolder;
import com.yuckar.infra.runner.binlog.info.BinlogLoginInfo;
import com.yuckar.infra.runner.binlog.info.BinlogStatusInfo;
import com.yuckar.infra.runner.binlog.listener.BinlogEventListener;
import com.yuckar.infra.runner.binlog.listener.BinlogLifecycleListener;
import com.yuckar.infra.runner.server.AbstractRunnerServer;

public class BinlogRunnerServer extends AbstractRunnerServer<BinlogRunner> {

	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("binlog-connect-%d")
			.setDaemon(false).build();

	@Override
	protected void doRun(BinlogRunner runner) throws IllegalStateException, IOException {
		BinlogRunnerHolder holder = BinlogRunnerHolder.of(runner);
		String path = YconfsNamespaceUtils.binlog(runner.ID() + "/login");
		Yconfs<BinlogLoginInfo> yconfs = holder.context().getYconfs(BinlogLoginInfo.class);

		LazySupplier<BinaryLogClient> client_supplier = LazySupplier.wrap(() -> {
			BinlogLoginInfo loginInfo = yconfs.get(path);
			BinaryLogClient client = new BinaryLogClient(loginInfo.getHostname(), loginInfo.getPort(),
					loginInfo.getSchema(), loginInfo.getUsername(), loginInfo.getPassword());
			client.registerLifecycleListener(new BinlogLifecycleListener(holder));
			client.registerEventListener(new BinlogEventListener(holder));

			BinlogStatusInfo statusInfo = holder.status();
			if (statusInfo != null) {
				client.setBinlogFilename(statusInfo.getBinlogFilename());
				client.setBinlogPosition(statusInfo.getBinlogPosition());
				client.setGtidSet(statusInfo.getGtidSet());
			}
			THREAD_FACTORY.newThread(() -> RunUtils.catching(() -> client.connect())).start();
			return client;
		});
		yconfs.addListener(path, event -> {
			client_supplier.refresh(client -> client.disconnect());
			client_supplier.get();
		});
		TermHelper.addTerm(runner.module(), holder::close);
		client_supplier.get();
	}

	@Override
	protected boolean nlock() {
		return true;
	}

}
