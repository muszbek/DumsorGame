package dumsorBase;

import java.awt.*;

import dumsorPanels.MaintenancePanel;

public class LineConstruction {
	
	private Node nodeFrom;
	private Node nodeTo;
	private Point posFrom;
	private Point posTo;
	private double distance;
	private double unitX;
	private double unitY;
	private Point posNew;
	private int step = 0;
	private boolean completed = false;
	
	private final double stepSize = 1;	//speed of line construction

	public LineConstruction(Node selected1, Node selected2) {
		//created in ElectricGrid when connecting
		nodeFrom = selected1;
		nodeTo = selected2;
		posFrom = selected1.getPos();
		posTo = selected2.getPos();
		posNew = posFrom;
		distance = posFrom.distance(posTo);
		unitX = (posTo.getX() - posFrom.getX()) / distance;
		unitY = (posTo.getY() - posFrom.getY()) / distance;
	}
	
	public void update() {	//called in each timestep
		if (completed == false) {
			step++;
			double newX = unitX * step * stepSize + posFrom.getX();
			double newY = unitY * step * stepSize + posFrom.getY();
			posNew = new Point((int)newX, (int)newY);
			if (posNew.distance(posFrom) >= distance) {
				construct();
			}
//			System.out.println(posNew.x + " " + posNew.y);
//			System.out.println(posNew.distance(posFrom));
//			System.out.println(distance);
		}
	}
	
	private void construct() {	//upon finishing the line
		completed = true;
		ElectricGrid.connect(nodeFrom, nodeTo);
		MaintenancePanel.addMaintCost((int)distance * MaintenancePanel.lineUnitMaint);
		Popups.lineFinishPopup(60);
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public void paintComponent(Graphics g) {
		if (completed == false) {
			g.setColor(Color.yellow);
			g.drawLine(posFrom.x, posFrom.y, posNew.x, posNew.y);
		}
		//System.out.println(setsAssigned);
	}

}
