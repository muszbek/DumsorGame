package dumsorBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import dumsorPanels.*;

public class DumsorRoot extends JPanel {
	private static ControlPanel controlPanel;
	private static MapPane mapPane;
	

	public static enum GameMode {
		OVERVIEW, CONSTRUCTION, LOAD_SHEDDING
	};

	private static GameMode mode;
	private static boolean lineConstruction = false;

	private static final int controlPanelWidth = 400;

	public DumsorRoot() {		
		mapPane = new MapPane(controlPanelWidth);
		controlPanel = new ControlPanel(controlPanelWidth, mapPane.getHeight());

//		this.setPreferredSize(new Dimension(mapPane.getWidth() + controlPanelWidth, mapPane
//						.getHeight()));
		this.setBounds(0, 0, mapPane.getWidth() + controlPanelWidth, mapPane
						.getHeight());

		this.setLayout(null);
		add(mapPane);
		add(controlPanel);
		this.addComponentListener(new Resizer(this));
		mode = GameMode.OVERVIEW;
	}
	
	public static void setGameMode (GameMode thisMode) {
		mode = thisMode;
	}
	
	public static GameMode getMode() {
		return mode;
	}
	
	public static void setLineConstruction (boolean isConstructing) {
		lineConstruction = isConstructing;
	}
	
	public static boolean getLineConstruction () {
		return lineConstruction;
	}
	
	public static void selectionUpdate(Node selected, ArrayList<PowerLabelPanel> labels) {
		if (controlPanel != null) {
			controlPanel.selectionUpdate(selected, labels);
		}
	}
	
	public static ControlPanel getControlPanel() {
		return controlPanel;
	}
	
	public static Component getMapPane() {
		return mapPane;
	}

}
