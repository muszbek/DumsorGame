package dumsorBase;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import dumsorPanels.*;

public abstract class Node extends JComponent implements MouseListener {
	protected String name;
	private int locX; // nominal coordinates on map
	private int locY;
	private int posX; // actual coordinates, based on scaling by resizing the window
	private int posY;
	private int cx;		//corner locations, for image paint
	private int cy;
	protected String icon; // the display icon
	private boolean electrified;
	private boolean enabled;
	private boolean selected;
	private boolean isSource;
	protected double producedPower;
	protected double consumedPower;
	private int picSize;
	protected BufferedImage iconImage;
	public NodeLabel nodeLabel; // public because ElectricGrid needs to add it as component
	private short nodeID; // may come in handy
	private PowerLabelPanel powerLabels;	//overViewPanel will add this
	protected boolean fullyElectrified = true;

	public Node(String inName, int x, int y) {
		name = inName;
		locX = x;
		locY = y;
		repos(x, y);
		enabled = true;
		electrified = false;
		selected = false;
		setSource(false);
		setSize(20);
		nodeLabel = new NodeLabel(name, posX, posY);
		powerLabels = new PowerLabelPanel();
		addMouseListener(this);
	}
	
	private void repos(int x, int y) {
		posX = x;
		posY = y;
		updateBounds();
	}

	public void rescale(double scaleX, double scaleY) {
		double newX = scaleX * locX;
		double newY = scaleY * locY;
		repos((int) (newX), (int) (newY));
		nodeLabel.repos((int) (newX), (int) (newY));
	}

	protected void electrify(boolean isEnabled) {	//argument decides whether electrified node
		//is enabled, diconnected nodes can be connected too
		if (electrified == false) {
			electrified = true;
			setNodeEnabled(isEnabled);
		}
	}

	public void setNodeSelected(boolean inSelected) {
		selected = inSelected;
		if (selected == true) {
			setSize(24);
		} else {
			setSize(20);
		}
		nodeLabel.setNodeFont(inSelected);
		repaint();
	}

	public void setNodeEnabled(boolean inEnabled) {
		enabled = inEnabled;
		nodeLabel.setColorEnabled(inEnabled);
	}

	public void setSource(boolean source) {
		isSource = source;
		if (source == true) {
			setNodeEnabled(true);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getSource() {
		return isSource;
	}
	
	public boolean getElectrified() {
		return electrified;
	}
	
	public boolean getNodeEnabled() {
		return enabled;
	}
	
	public Point getPos() {
		return new Point(posX, posY);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(iconImage, cx, cy, picSize, picSize, this);
	}

	private void setSize(int size) {
		picSize = size;
		updateBounds();
	}
	
	private void updateBounds() {
		cx = posX - picSize / 2;
		cy = posY - picSize / 2;
		this.setBounds(cx, cy, picSize, picSize);
	}
	
	protected double getProduction() {
		//System.out.println("node produce");
		return producedPower;
	}
	
	protected double getConsumption() {
		//System.out.println("node consume");
		return consumedPower;
	}
	
	protected void produce() {
		//must override
	}
	
	protected void consume() {
		//must override
	}
	
	public void updateLabels() {
		powerLabels.updateText(producedPower, consumedPower);
	}
	
	public String getNodeName() {
		return name;
	}
	
	public PowerLabelPanel getPowerLabels() {
		return powerLabels;
	}
	
	public boolean getFullyElect() {	//only makes sense for districs, but easier to override
		return fullyElectrified;		//then to exclude substation from the queries
	}
	
	public boolean getFullyMaxed() {	//override in districs
		return true;
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (selected != true) {
			ElectricGrid.selectNode(this);
		}
		else {
			ElectricGrid.selectNode(null);
		}
		//System.out.println("enabled " + enabled);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		setSize(24);
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (selected == false)
			setSize(20);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

}
