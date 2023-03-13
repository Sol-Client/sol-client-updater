package io.github.solclient.updater.util;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.Path;
import java.util.jar.JarFile;

public final class ClasspathUtil {

	private static Instrumentation activeInst;

	public static void premain(String args, Instrumentation inst) {
		activeInst = inst;
	}

	public static void addJar(Path jar) throws IOException {
		if (activeInst != null)
			activeInst.appendToSystemClassLoaderSearch(new JarFile(jar.toFile()));

		ClassLoader loader = ClasspathUtil.class.getClassLoader();
		if (!(loader instanceof URLClassLoader))
			throw new UnsupportedOperationException(ClasspathUtil.class + " was not added as an agent and " + loader + " is not an URLClassLoader");

		try {
			Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			addURL.setAccessible(true);
			addURL.invoke(loader, jar.toUri().toURL());
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException error) {
			throw new UnsupportedOperationException("Cannot add URL to " + loader, error);
		}
	}

}
