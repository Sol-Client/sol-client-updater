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

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.*;

public final class Release {

	private final String url;
	private final SemVer version;

	public Release(String url, SemVer version) {
		this.url = url;
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public SemVer getVersion() {
		return version;
	}

	@SuppressWarnings("unchecked")
	public static Release latest(String repo) throws IOException {
		URL url = new URL(String.format("https://api.github.com/repos/%s/releases/latest", repo));
		try (Reader reader = new InputStreamReader(Util.getHttpConnection(url).getInputStream(),
				StandardCharsets.UTF_8)) {
			JsonObject response = new JsonParser().parse(reader).getAsJsonObject();
			String downloadUrl = response.getAsJsonArray("assets").get(0).getAsJsonObject().get("browser_download_url")
					.getAsString();
			return new Release(downloadUrl, SemVer.tryParse(response.get("name").getAsString())
					.orElseGet(() -> new SemVer(response.get("tag_name").getAsString())));
		}
	}

}
