package io.github.solclient.updater;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.JPanel;

public final class UpdatePane extends JPanel {

	private static final DecimalFormat FORMAT = new DecimalFormat("0.0");

	private static final long serialVersionUID = 1L;
	private static final String TEXT = "Downloading Sol Client...";
	public static final Color BG = new Color(0xFF1F1F1F), FG = new Color(0xFFFFFFFF), BUTTON = new Color(0xFF2A2A2A),
			ACCENT = new Color(0xFFFFB400);

	private static final int PROGRESS_WIDTH = 330;
	private static final int PROGRESS_HEIGHT = 10;

	private final DownloadProgress progress;

	public UpdatePane(DownloadProgress progress) {
		this.progress = progress;
	}

	@Override
	public void paint(Graphics baseGraphics) {
		super.paint(baseGraphics);

		Graphics2D graphics = (Graphics2D) baseGraphics;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		graphics.setBackground(BG);
		graphics.clearRect(0, 0, getWidth(), getHeight());

		graphics.setColor(FG);
		graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 20));
		graphics.drawString(TEXT, getWidth() / 2 - graphics.getFontMetrics().stringWidth(TEXT) / 2, 45);

		int x = getWidth() / 2 - PROGRESS_WIDTH / 2;
		int y = getHeight() - PROGRESS_HEIGHT - 25;

		graphics.setColor(BUTTON);
		graphics.fillRoundRect(x, y, PROGRESS_WIDTH,
				PROGRESS_HEIGHT, PROGRESS_HEIGHT, PROGRESS_HEIGHT);

		graphics.setColor(ACCENT);
		graphics.fillRoundRect(x, y,
				(int) (PROGRESS_WIDTH * progress.percent()), PROGRESS_HEIGHT, PROGRESS_HEIGHT, PROGRESS_HEIGHT);

		graphics.setColor(FG);
		graphics.setFont(graphics.getFont().deriveFont(Font.PLAIN, 10));

		String progressString;
		if (progress.max == 0)
			progressString = "Waiting...";
		else
			progressString = FORMAT.format(progress.value / 1024F / 1024F) + " / " + FORMAT.format(progress.max / 1024F / 1024F) + " MB";

		graphics.drawString(progressString, x + PROGRESS_WIDTH - graphics.getFontMetrics().stringWidth(progressString) - 2, y - 5);

		repaint(1000 / 60);
	}

}
