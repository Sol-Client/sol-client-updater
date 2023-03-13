package io.github.solclient.updater;

public final class DownloadProgress {

	public int value, max;

	public double percent() {
		if (max == 0)
			return 0;

		return (double) value / max;
	}

}
