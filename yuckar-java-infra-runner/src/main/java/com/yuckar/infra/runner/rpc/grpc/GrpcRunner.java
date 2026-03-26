package com.yuckar.infra.runner.rpc.grpc;

import java.util.List;

import com.yuckar.infra.runner.rpc.RpcRunner;

import io.grpc.BindableService;

public interface GrpcRunner extends RpcRunner {

	List<BindableService> services();

}
