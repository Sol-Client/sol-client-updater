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

import java.util.Optional;

public class SemVer {

	private final int major, minor, patch;

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getPatch() {
		return patch;
	}

	public static Optional<SemVer> tryParse(String string) {
		try {
			return Optional.of(new SemVer(string));
		} catch (IllegalArgumentException ignored) {
			return Optional.empty();
		}
	}

	public SemVer(String string) {
		String initial = string;

		if (string.charAt(0) == 'v' || string.charAt(0) == 'V' /* ew */)
			string = string.substring(1);

		if (string.indexOf('.') == -1) {
			throw new IllegalArgumentException(initial);
		}

		String majorString = string.substring(0, string.indexOf('.'));
		string = string.substring(string.indexOf('.') + 1);

		if (string.indexOf('.') == -1) {
			throw new IllegalArgumentException(initial);
		}

		String minorString = string.substring(0, string.indexOf('.'));
		string = string.substring(string.indexOf('.') + 1);

		if (string.indexOf('.') != -1) {
			throw new IllegalArgumentException(initial);
		}

		String patchString = string;

		if (string.isEmpty()) {
			throw new IllegalArgumentException(initial);
		}

		try {
			major = Integer.parseInt(majorString);
			minor = Integer.parseInt(minorString);
			patch = Integer.parseInt(patchString);
		} catch (NumberFormatException error) {
			throw new NumberFormatException(error.getMessage() + ", full string: \"" + initial + "\"");
		}
	}

	public boolean isNewerThan(SemVer version) {
		if (version == null)
			return false;

		if (major > version.major)
			return true;
		else if (major < version.major)
			return false;

		if (minor > version.minor)
			return true;
		else if (minor < version.minor)
			return false;

		return patch > version.patch;
	}

	@Override
	public String toString() {
		return "" + major + '.' + minor + '.' + patch;
	}

}
