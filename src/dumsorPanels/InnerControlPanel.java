package dumsorPanels;

import imported.WrapLayout;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

//this is for the main game mode control panels, they implement it
abstract class InnerControlPanel extends JPanel implements ResizeablePanel {
	protected int panelWidth;
	protected int panelHeight;
	private int locX;
	private int locY;
	protected JScrollPane scrollPane;
	protected JPanel contentPanel;
	
	public InnerControlPanel(int x, int y, int width, int height) {
		locX=x;
		locY=y;
		sizePanel(width, height);
		this.setOpaque(false);
		
		contentPanel = new JPanel(new WrapLayout());
		contentPanel.setOpaque(false);
		
		scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, panelWidth, panelHeight);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		this.add(scrollPane);
	}

	@Override
	public void sizePanel(int width, int height) {
		panelWidth = width;
		panelHeight = height;
		this.setBounds(locX, locY, panelWidth, panelHeight);
	}
	
	@Override
	public void resizePanel(double widthRate, double heightRate) {
		double newWidth = widthRate * panelWidth;
		double newHeight = heightRate * panelHeight;
		double newX = widthRate * locX;
		double newY = heightRate * locY;
		setBounds((int)newX, (int)newY, (int)newWidth, (int)newHeight);
		scrollPane.setBounds(0, 0, (int)newWidth, (int)newHeight);
	}
	
}
