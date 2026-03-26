package com.yuckar.infra.register.group.impl;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import com.annimon.stream.Stream;
import com.yuckar.infra.common.file.utils.FileUtils;
import com.yuckar.infra.register.Register;
import com.yuckar.infra.register.RegisterListener;
import com.yuckar.infra.register.group.AbstractGroupReigster;
import com.yuckar.infra.register.impl.FileRegister;
import com.yuckar.infra.register.utils.RegisterUtils;

public class FileGroupRegister<V, I> extends AbstractGroupReigster<V, I> {

	private final Register<V> vregister;
	private final String workspace;

	public FileGroupRegister(Class<V> vclazz, Class<I> clazz) {
		this(System.getProperty("user.dir") + File.separator + "register", vclazz, clazz);
	}

	public FileGroupRegister(String workspace, Class<V> vclazz, Class<I> clazz) {
		super(new FileRegister<I>(workspace, clazz));
		this.workspace = workspace;
		this.vregister = new FileRegister<>(this.workspace, vclazz);
	}

	@Override
	protected void init(String path) {
		File file = new File(RegisterUtils.toFile(this.workspace, path));
		FileUtils.createDirIfNoExists(file);
		RegisterUtils.monitor(file.getAbsolutePath(), new FileAlterationListenerAdaptor() {
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
		return Stream.of(new File(RegisterUtils.toFile(this.workspace, path)).listFiles())
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
		this.vregister.set(key, value);
	}

	@Override
	public V get(String key) {
		return this.vregister.get(key);
	}

	@Override
	public void addListener(String key, RegisterListener<V> listener) {
		this.vregister.addListener(key, listener);
	}

}
