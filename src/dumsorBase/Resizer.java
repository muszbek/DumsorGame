package dumsorBase;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.*;

public class Resizer implements ComponentListener {

	private Container sizeable;
	private double originalWidth;
	private double originalHeigth;
	
	public Resizer(Container cont) {
		sizeable = cont;
		originalWidth = sizeable.getWidth();
		originalHeigth = sizeable.getHeight();
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		double widthRatio = (double)sizeable.getWidth() / originalWidth;
		double heigthRatio = (double)sizeable.getHeight() / originalHeigth;
		//System.out.println(sizeable.getWidth() + " " + sizeable.getHeight());
		DumsorRoot.getControlPanel().resizePanel(widthRatio, heigthRatio);
		MapPane localPane = (MapPane)DumsorRoot.getMapPane();
		localPane.resizePanel(widthRatio, heigthRatio);
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

}
