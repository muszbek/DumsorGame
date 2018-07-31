package dumsorPanels;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import dumsorBase.ResLoader;

public class MapDrawerPanel extends JPanel implements ResizeablePanel {
	private BufferedImage mapImage;
	// private MediaTracker tracker;
	private int imgHeight;
	private int imgWidth;
	private int drawHeight;
	private int drawWidth;

	public MapDrawerPanel() {

		try {
			this.mapImage = ImageIO.read(ResLoader.loadStr("pics/sunyani_map.jpg"));
		} catch (IOException e) {
			System.out.println("map image not found");
		}
		// later may come in handy, resizing the map
		imgHeight = mapImage.getHeight();
		imgWidth = mapImage.getWidth();
		sizePanel(imgWidth, imgHeight);
		this.setBounds(0, 0, drawWidth, drawHeight);

	}

	@Override
	public void sizePanel(int width, int height) {
		drawWidth = width;
		drawHeight = height;
		this.setBounds(0, 0, drawWidth, drawHeight);
	}
	
	@Override
	public void resizePanel(double widthRate, double heigthRate) {
		int newWidth = (int)(widthRate * imgWidth);
		int newHeight = (int)(heigthRate * imgHeight);
		sizePanel(newWidth, newHeight);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Paint background

		g.drawImage(mapImage, 0, 0, drawWidth, drawHeight, this);
	}

}
