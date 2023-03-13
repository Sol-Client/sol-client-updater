/*
 * Sol Client Updater - updater for Sol Client
 * Copyright (C) 2023  TheKodeToad and Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
