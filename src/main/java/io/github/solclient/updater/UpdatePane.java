package io.github.solclient.updater;

import java.awt.*;

import javax.swing.JPanel;

public final class UpdatePane extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String TEXT = "Downloading Sol Client...";
	private static final Color BG = new Color(0xFF1F1F1F), FG = new Color(0xFFFFFFFF), BUTTON = new Color(0xFF2A2A2A),
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
		graphics.drawString(TEXT, getWidth() / 2 - graphics.getFontMetrics().stringWidth(TEXT) / 2, 80);

		graphics.setColor(BUTTON);
		graphics.fillRoundRect(getWidth() / 2 - PROGRESS_WIDTH / 2, getHeight() - PROGRESS_HEIGHT - 35, PROGRESS_WIDTH,
				PROGRESS_HEIGHT, PROGRESS_HEIGHT, PROGRESS_HEIGHT);

		graphics.setColor(ACCENT);
		graphics.fillRoundRect(getWidth() / 2 - PROGRESS_WIDTH / 2, getHeight() - PROGRESS_HEIGHT - 35,
				(int) (PROGRESS_WIDTH * progress.percent()), PROGRESS_HEIGHT, PROGRESS_HEIGHT, PROGRESS_HEIGHT);

		repaint(1000 / 60);
	}

}
