package com.yuckar.infra.network.ftp;

import org.apache.commons.net.ftp.FTPSClient;

public class KftpsClient extends KftpBase<FTPSClient, KftpsClientInfo> {

	public KftpsClient(KftpsClientInfo info) {
		super(info);
	}

	@Override
	protected FTPSClient create() throws Exception {
		return KftpUtils.ftpsClient(info());
	}

}
