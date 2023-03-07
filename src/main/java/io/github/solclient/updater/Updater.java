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

package io.github.solclient.updater;

import java.io.*;
import java.lang.invoke.*;
import java.net.*;
import java.nio.file.*;

import javax.swing.JFrame;

import io.github.solclient.updater.util.*;

public final class Updater {

	private static final String MAIN_CLASS = "io.github.solclient.wrapper.Launcher";
	private static final MethodType MAIN_METHOD = MethodType.methodType(void.class, String[].class);

	public static void main(String[] args) throws Throwable {
		run(update(args), args);
	}

	private static Path update(String[] args) throws IOException {
		Path folder = resolveFolder(args);
		Path jar = folder.resolve("sol-client.jar");

		if (!Files.isDirectory(folder))
			Files.createDirectories(folder);

		Release latestRelease = Release
				.latest(System.getProperty("io.github.solclient.updater.repo", "Sol-Client/client"));

		DownloadProgress progress = new DownloadProgress();

		URL url = new URL(latestRelease.getUrl());
		JFrame ui = new JFrame();
		ui.setContentPane(new UpdatePane(progress));
		ui.setTitle("Downloading Sol Client...");
		ui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ui.setUndecorated(true);
		ui.setResizable(false);
		ui.setSize(380, 170);
		ui.setLocationRelativeTo(null);
		ui.setVisible(true);

		HttpURLConnection connection = Util.getHttpConnection(url);

		progress.max = Integer.parseInt(connection.getHeaderField("content-length"));

		try (InputStream in = connection.getInputStream(); OutputStream out = Files.newOutputStream(jar)) {
			byte[] buffer = new byte[8192];
			int read;
			while ((read = in.read(buffer)) >= 0) {
				if (!ui.isVisible()) {
					Files.delete(jar);
					System.exit(1);
					break;
				}

				out.write(buffer);
				progress.value += read;
			}
		}

		ui.setVisible(false);
		ui.dispose();

		return jar;
	}

	private static Path resolveFolder(String[] args) {
		Path gameDir;

		int gameDirIndex = 0;
		for (String arg : args) {
			if (arg.equals("--gameDir"))
				break;
			gameDirIndex++;
		}

		if (gameDirIndex < args.length - 1 && gameDirIndex > 0)
			gameDir = Paths.get(args[gameDirIndex + 1]);
		else
			gameDir = Paths.get(".");

		return gameDir.resolve(".sol-client-launch");
	}

	private static void run(Path path, String[] args) throws Throwable {
		try (URLClassLoader loader = new URLClassLoader(new URL[] { path.toUri().toURL() })) {
			// @formatter:off
			MethodHandle mainMethod = MethodHandles.lookup().findStatic(
					loader.loadClass(MAIN_CLASS),
					"main",
					MAIN_METHOD
			);
			// @formatter:on
			mainMethod.invokeExact(args);
		}
	}

}
