package com.yuckar.infra.network.ftp;

import org.apache.commons.net.ftp.FTPClient;

public class KftpClient extends KftpBase<FTPClient, KftpClientInfo> {

	public KftpClient(KftpClientInfo info) {
		super(info);
	}

	@Override
	protected FTPClient create() throws Exception {
		return KftpUtils.ftpClient(info());
	}

}
