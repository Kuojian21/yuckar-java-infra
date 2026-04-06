package com.yuckar.infra.base.spi;

public interface PkgSpi {

	String pkg();

	default String[] pkgs() {
		return new String[] { pkg() };
	}

}
