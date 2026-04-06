package com.yuckar.infra.network.mail;

import javax.mail.Store;

import com.sun.mail.imap.IMAPStore;
import com.yuckar.infra.base.executor.PoolExecutor;
import com.google.common.collect.ImmutableMap;

public class MailReceiver extends PoolExecutor<Store, MailReceiverInfo> {

	public MailReceiver(MailReceiverInfo info) {
		super(info);
	}

	@Override
	protected Store create() throws Exception {
		Store store = MailUtils.session(info()).getStore();
		store.connect();
		if (store instanceof IMAPStore) {
			((IMAPStore) store).id(ImmutableMap.of("name", "yuckar"));
		}
		return store;
	}

	@Override
	protected String tag() {
		String username = this.info().getAuth().getUsername();
		return username.substring(username.indexOf('@') + 1);
	}

}
