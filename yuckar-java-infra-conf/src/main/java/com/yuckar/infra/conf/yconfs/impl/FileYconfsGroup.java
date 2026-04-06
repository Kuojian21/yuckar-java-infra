package com.yuckar.infra.conf.yconfs.impl;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import com.annimon.stream.Stream;
import com.yuckar.infra.base.file.utils.FileUtils;
import com.yuckar.infra.conf.yconfs.AbstractYconfsGroup;
import com.yuckar.infra.conf.yconfs.Yconfs;
import com.yuckar.infra.conf.yconfs.YconfsListener;
import com.yuckar.infra.conf.yconfs.utils.YconfsFileUtils;

public class FileYconfsGroup<V, I> extends AbstractYconfsGroup<V, I> {

	private final Yconfs<V> v_yconfs;
	private final Yconfs<I> c_yconfs;
	private final String workspace;

	public FileYconfsGroup(Class<V> vclazz, Class<I> clazz) {
		this(System.getProperty("user.dir") + File.separator + "confs", vclazz, clazz);
	}

	public FileYconfsGroup(String workspace, Class<V> vclazz, Class<I> iclazz) {
		this.workspace = workspace;
		this.v_yconfs = new FileYconfs<>(workspace, vclazz);
		this.c_yconfs = new FileYconfs<>(workspace, iclazz, key -> {
			String pkey = key.substring(0, key.lastIndexOf("/"));
			return FileYconfsGroup.this.cgetData(pkey).clisteners();
		});
	}

	@Override
	protected void init(String path) {
		File file = new File(YconfsFileUtils.toFile(this.workspace, path));
		FileUtils.createDirIfNoExists(file);
		YconfsFileUtils.monitor(file.getAbsolutePath(), new FileAlterationListenerAdaptor() {
			/*
			 * bugfix
			 */
//			java.lang.NullPointerException: Cannot invoke "com.yuckar.infra.cluster.instance.InstanceInfo.getName()" because "tInfo" is null
//	        		at com.yuckar.infra.cluster.Cluster.lambda$add$8(Cluster.java:89)
//	        		at com.yuckar.infra.common.lazy.LazySupplier.get(LazySupplier.java:32)
//	        		at com.yuckar.infra.cluster.selector.RandomSelector.select(RandomSelector.java:19)
//	        		at com.yuckar.infra.cluster.Cluster.getResource(Cluster.java:76)
//			@Override
//			public void onDirectoryCreate(final File dir) {
//				fireCreate(path, path + "/" + dir.getName());
//			}
//
//			@Override
//			public void onDirectoryDelete(final File dir) {
//				fireRemove(path, path + "/" + dir.getName());
//			}

			@Override
			public void onFileCreate(final File ifile) {
				Matcher matcher = Pattern
						.compile("^" + file.getAbsolutePath().replace(File.separator, "/") + "/(.+)/main.json")
						.matcher(ifile.getAbsolutePath().replace(File.separator, "/"));
				if (matcher.find()) {
					fireCreate(path, path + "/" + matcher.group(1));
				}
			}

			@Override
			public void onFileDelete(final File ifile) {
				Matcher matcher = Pattern
						.compile("^" + file.getAbsolutePath().replace(File.separator, "/") + "/(.+)/main.json")
						.matcher(ifile.getAbsolutePath().replace(File.separator, "/"));
				if (matcher.find()) {
					fireRemove(path, path + "/" + matcher.group(1));
				}
			}
		});
	}

	@Override
	protected List<String> keys(String path) {
		return Stream.of(new File(YconfsFileUtils.toFile(this.workspace, path)).listFiles())
				.filter(dir -> dir.isDirectory()
						&& new File(dir.getAbsolutePath() + File.separator + "main.json").exists())
				.map(dir -> dir.getName()).map(c -> path + "/" + c).toList();
	}

	@Override
	public void cadd(String pkey, I value) {
		this.cset(pkey + "/" + ProcessHandle.current().pid(), value);
	}

	@Override
	public void set(String key, V value) {
		this.v_yconfs.set(key, value);
	}

	@Override
	public V get(String key) {
		return this.v_yconfs.get(key);
	}

	@Override
	public void addListener(String key, YconfsListener<V> listener) {
		this.v_yconfs.addListener(key, listener);
	}

	@Override
	protected Yconfs<I> c_yconfs() {
		return this.c_yconfs;
	}

}
