package io.github.solclient.updater;

import java.text.DecimalFormat;

public final class DownloadProgress {

	private static final DecimalFormat FORMAT = new DecimalFormat("0.0");

	public int value, max;
	private int speed, lastValue;
	private long lastUpdate = System.currentTimeMillis();

	public int getSpeed() {
		if (System.currentTimeMillis() - lastUpdate > 1000) {
			lastUpdate = System.currentTimeMillis();
			speed = value - lastValue;
			lastValue = value;
		}
		return speed;
	}

	public double percent() {
		if (max == 0)
			return 0;

		return (double) value / max;
	}

	@Override
	public String toString() {
		if (max == 0)
			return "Waiting...";
		else
			return FORMAT.format(value / 1024F / 1024F) + " / " + FORMAT.format(max / 1024F / 1024F) + " MB ("
					+ FORMAT.format(getSpeed() / 1024F / 1024F) + " MB/s)";
	}

}
