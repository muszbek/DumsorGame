package dumsorBase;

import java.awt.*;
import javax.swing.*;

public class NodeLabel extends JLabel {

	private int posX;
	private int posY; // center position of district nodes, for reference
	private Color fontColor;
	private Font font;
	private static final int virtualWidth = 150; // bounds of JLabel have to be specified for repaint
	private static final int virtualHeight = 50;
	private static final int stringDisplacement = 20;
	//private static final Color green = new Color(0, 150, 0);

	NodeLabel(String name, int nomX, int nomY) {
		super.setText(name);
		super.setHorizontalAlignment(JLabel.CENTER);
		super.setVerticalAlignment(JLabel.CENTER);
		setOpaque(false);
		posX = nomX;
		posY = nomY;
		repos(nomX, nomY);
		fontColor = Color.DARK_GRAY;
		super.setForeground(fontColor);
		setNodeFont(false);

	}

	public void repos(int inX, int inY) {
		int x = inX - virtualWidth / 2;
		int y = inY - virtualHeight / 2 - stringDisplacement;
		this.setBounds(x, y, virtualWidth, virtualHeight);
	}

	public void setColorEnabled(boolean colorEnabled) {
		if (colorEnabled == true) {
			fontColor = Color.GREEN;
		}
		else {
			fontColor = Color.RED;
		}
		super.setForeground(fontColor);
	}

	public void setNodeFont(boolean selected) {
		int fontSize = 12;
		if (selected == true) {
			fontSize = 15;
		}
		font = new Font("Default", Font.BOLD, fontSize);
		this.setFont(font);
	}

}
