package dumsorBase;

import java.awt.Graphics;
import javax.swing.JLayeredPane;
import dumsorPanels.*;

public class MapPane extends JLayeredPane implements ResizeablePanel{
	private MapDrawerPanel mapDrawerPanel;
	private ElectricGrid electricGrid;
	private int xOffset;
	private int paneWidth;
	private int paneHeight;

	public MapPane(int x) { // i have to set the starting location with regard
							// to the control panel
		mapDrawerPanel = new MapDrawerPanel();
		xOffset = x;
		paneWidth = mapDrawerPanel.getWidth();
		paneHeight = mapDrawerPanel.getHeight();
		electricGrid = new ElectricGrid(paneWidth, paneHeight);
		this.setBounds(xOffset, 0, paneWidth, paneHeight);
		this.add(mapDrawerPanel, JLayeredPane.DEFAULT_LAYER);
		this.add(electricGrid, JLayeredPane.PALETTE_LAYER);
	}

	@Override
	public void sizePanel(int width, int height) {
		
	}

	@Override
	public void resizePanel(double widthRate, double heigthRate) {
		int newX = (int)(widthRate * xOffset);
		int newWidth = (int)(widthRate * paneWidth);
		int newHeight = (int)(heigthRate * paneHeight);
		setBounds(newX, 0, newWidth, newHeight);
		mapDrawerPanel.resizePanel(widthRate, heigthRate);
		electricGrid.resizePanel(widthRate, heigthRate);
	}

}
