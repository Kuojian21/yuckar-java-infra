package com.yuckar.infra.runner.rpc.grpc.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.yuckar.infra.base.term.TermHelper;
import com.yuckar.infra.conf.yconfs.context.YconfsGroupFactory;
import com.yuckar.infra.conf.yconfs.utils.YconfsNamespaceUtils;
import com.yuckar.infra.runner.rpc.grpc.GrpcRunner;
import com.yuckar.infra.runner.rpc.grpc.info.GrpcInfo;
import com.yuckar.infra.runner.rpc.grpc.info.GrpcItemInfo;
import com.yuckar.infra.runner.server.AbstractRunnerServer;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcRunnerServer extends AbstractRunnerServer<GrpcRunner> {

	private final AtomicReference<ExecutorService> executor = new AtomicReference<>();

	@Override
	protected void init() {
		executor.set(Executors
				.newFixedThreadPool(Integer.valueOf(super.commandLine.get().getOptionValue("grpcWorkers", "10"))));
		executor.get().execute(() -> {
			logger.info("grpc thread pool start!!!");
		});
	}

	@Override
	protected void doRun(GrpcRunner runner) throws IOException {
		ServerBuilder<?> builder = ServerBuilder.forPort(0).executor(executor.get());
		runner.services().forEach(builder::addService);
		Server server = builder.build().start();
		GrpcItemInfo address = Stream.of(server.getListenSockets()).map(socket -> (InetSocketAddress) socket)
				.map(socket -> GrpcItemInfo.address(socket.getHostName(), socket.getPort())).toList().get(0);
		YconfsGroupFactory.getContext(runner.getClass()).getYconfsGroup(GrpcInfo.class, GrpcItemInfo.class)
				.cadd(YconfsNamespaceUtils.grpc(Optional.ofNullable(runner.ID())
						.orElseGet(() -> runner.getClass().getName().replace("$", "_"))), address);
		TermHelper.addTerm("grpc", () -> {
			server.shutdown();
			server.awaitTermination();
		});
	}

	@Override
	protected boolean nlock() {
		return false;
	}

	public Options args_options() {
		Options options = new Options();
		options.addOption(Option.builder().option(null).longOpt("grpcWorkers").hasArg(true).required(false).build());
		return options;
	}

}
