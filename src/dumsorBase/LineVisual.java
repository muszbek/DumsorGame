package dumsorBase;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import javax.swing.*;

public class LineVisual {
	private HashSet<Point[]> connectedLines;
	private HashSet<Point[]> disconnectedLines;
	private boolean setsAssigned = false;	//i don't want to assign memory for new sets
	//sets are going to be referred to lineNetwork, once it is done this boolean is true
	
	public LineVisual() {
		//this.setBounds(0, 0, 1000, 1000);
	}
	
	public void paintComponent(Graphics g) {
		if (setsAssigned == true) {
			g.setColor(Color.green);
			drawLinesFromSet(g, connectedLines);
			g.setColor(Color.red);
			drawLinesFromSet(g, disconnectedLines);
		}
		//System.out.println(setsAssigned);
	}
	
	private void drawLinesFromSet (Graphics myGraphics, HashSet<Point[]> inputHashArray) {
		for (Point[] lineLocs : inputHashArray) {
			try {
				int x1 = lineLocs[0].x;
				int y1 = lineLocs[0].y;
				int x2 = lineLocs[1].x;
				int y2 = lineLocs[1].y;
				myGraphics.drawLine(x1, y1, x2, y2);
			} catch (IndexOutOfBoundsException e) {
				System.out.println("did not manage to paint a line, array out of bounds");
			}
		}
	}
	
	public void assignSets (HashSet<Point[]>[] pairOfSets) {
		try {
			connectedLines = pairOfSets[0];
			disconnectedLines = pairOfSets[1];
			setsAssigned = true;
		} catch (IndexOutOfBoundsException e) {
			System.out.println("did not manage to assign sets, array out of bounds");
		}
	}
	
}
