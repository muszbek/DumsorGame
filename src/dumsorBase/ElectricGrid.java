package dumsorBase;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.*;
import javax.swing.*;
import dumsorPanels.*;

public class ElectricGrid extends JPanel implements ResizeablePanel {
	private int panelWidth;
	private int panelHeight;
	private static ArrayList<Node> nodes;
	private static Node selectedNode;
	private static LineNetwork lineNetwork;
	private static LineVisual lineVisual;
	private static Time time;
	private static boolean needToShed = false;
	private static ArrayList<PowerLabelPanel> labelList;
	private static HashSet<LineConstruction> lineConstSet;
	private static boolean allElectPopupped = false;
	private static boolean winPopupped = false;
	
	public ElectricGrid(int width, int height) {
		sizePanel(width, height);
		this.setOpaque(false);
		this.setLayout(null);
		nodes = new ArrayList<Node>();
		labelList = new ArrayList<PowerLabelPanel>();
		lineConstSet = new HashSet<LineConstruction>();
		initNodes();
		lineNetwork = new LineNetwork(nodes);
		lineVisual = new LineVisual();
		assignNetworkToVisual();
		time = new Time(nodes);
	}

	@Override
	public void sizePanel(int width, int height) {
		panelWidth = width;
		panelHeight = height;
		setBounds(0, 0, panelWidth, panelHeight);
	}
	
	@Override
	public void resizePanel(double widthRate, double heigthRate) {
		int newWidth = (int)(widthRate * panelWidth);
		int newHeight = (int)(heigthRate * panelHeight);
		setBounds(0, 0, newWidth, newHeight);
		for (Node aNode : nodes) {
			aNode.rescale(widthRate, heigthRate);
		}
	}
	
	public static Node getSelected() {
		return selectedNode;
	}
	
	public static Node getNode(String name) {	//happinessPanel needs it
		Node returned = null;
		for (Node aNode : nodes) {
			if (aNode.getName().equals(name)) {
				returned = aNode;
			}
		}
		return returned;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Paint background
		lineVisual.paintComponent(g);
		for (Node paintnode : nodes) {
			paintnode.paintComponent(g);
		}
		for (LineConstruction lineConst : lineConstSet) {
			lineConst.paintComponent(g);
		}
		repaint();
	}
	
	public static void updateLineConst() {	//updating under construction lines
		if (lineConstSet.size() != 0) {
			HashSet<LineConstruction> removeables = new HashSet<LineConstruction>();
			for (LineConstruction lineConst : lineConstSet) {
				lineConst.update();
				if (lineConst.getCompleted() == true) {
					removeables.add(lineConst);
					//lineConstSet.remove(lineConst);
				}
			}
			for (LineConstruction removeConst : removeables) {
				if (lineConstSet.contains(removeConst) == true) {
					lineConstSet.remove(removeConst);
				}
			}
		}
	}
	
	public static void disconnect(Node selected) {
		int id = nodes.indexOf(selected);
		lineNetwork.disconnect(id);
		assignNetworkToVisual();
	}
	
	public static void reconnect(Node selected) {
		int id = nodes.indexOf(selected);
		lineNetwork.reconnect(id);
		assignNetworkToVisual();
	}
	
	public static void connect(Node selected1, Node selected2) {
		lineNetwork.connect(nodes.indexOf(selected1), nodes.indexOf(selected2));
		assignNetworkToVisual();
	}
	
	public static void updateImage() {	//called in controlPanel, to access selectedNode
		ImagePanel.updateImage(selectedNode);
	}

	public static void selectNode(Node selected) {
		//here comes the change of controlpanels
		boolean tryToConnect = false; 
		if (selectedNode != null) {
			tryToConnect = secondarySelect(selected);
			selectedNode.setNodeSelected(false);
		}
		selectedNode = null;
		if (tryToConnect == false) {
			selectedNode = selected;
		}
		if (selectedNode != null) {
			selectedNode.setNodeSelected(true);
		}
		LoadSheddingPanel.setButton(selectedNode);
		update();
	}
	
	public static boolean secondarySelect(Node secSelected) {
		boolean isConnecting = false;
		if (DumsorRoot.getLineConstruction()==true) {
			if (secSelected != null) {
				//connect(selectedNode, secSelected);
				int id1 = nodes.indexOf(selectedNode);
				int id2 = nodes.indexOf(secSelected);
				if (lineNetwork.getConnected(id1, id2) == false) {
					int answer = Popups.buildLinePopup(selectedNode, secSelected);
					if (answer == JOptionPane.YES_OPTION) {
						if (MoneyPanel.spendMoney(
								MoneyPanel.priceLine(selectedNode, secSelected)) == true) {
							lineNetwork.startConnConstruction(id1, id2);
							lineConstSet.add(new LineConstruction(selectedNode, secSelected));
						}
					}
				}
			}
			isConnecting = true;
		}
		return isConnecting;
	}
	

	private void initNodes() {

		nodes.add(new Substation("Substation", 290, 410));
		nodes.add(new District("Kumasi-Sunyani", 360, 430, 100));
		nodes.add(new District("Kontrajeso", 390, 520, 125));
		nodes.add(new District("Abesem", 500, 570, 135));
		nodes.add(new District("Tonsuom", 210, 400, 120));
		nodes.add(new District("Sunyani-Mim", 170, 340, 115));
		nodes.add(new District("Nwawansua", 80, 370, 135));
		nodes.add(new District("Sunyani", 250, 330, 140));
		nodes.add(new District("Sunyani-Wenchi", 270, 270, 140));
		nodes.add(new District("New-Dormaa", 350, 300, 145));
		nodes.add(new District("Asuakwa", 400, 230, 150));
		nodes.add(new District("Kotokrom", 560, 270, 127));
		nodes.add(new District("Sunyani-Berekum", 170, 250, 80));
		nodes.add(new District("Berlin", 70, 250, 120));
		nodes.add(new District("Fiapre", 90, 150, 175));
		nodes.add(new District("Penkwase", 240, 200, 140));
		nodes.add(new District("Odumase", 280, 120, 155));

		for (Node paintnode : nodes) {
			this.add(paintnode);
			this.add(paintnode.nodeLabel);
			paintnode.repaint();
		}
	}
	
	private static void assignNetworkToVisual() {	//this is for passing the lines for painting
		lineVisual.assignSets(lineNetwork.getLineSets());
	}
	
	public static void pauseTime(boolean doPause) {
		if (doPause == true) {
			time.pause();
		}
		else {
			time.unpause();
		}
	}
	
	public static void changeTimeStep(int newStep) {	//this is for the time speed buttons
		time.changeTimeStep(newStep);
	}
	
	public static void setSource(boolean isSource, Node myNode) {
		lineNetwork.setSource(isSource, myNode);
	}
	
	public static boolean sourceConnected(Node myNode) {
		return lineNetwork.sourceConnected(myNode);
	}
	
	public static Time getTime() {
		return time;
	}
	
	public static LineVisual getLineVisual() {
		return lineVisual;
	}
	
	public static ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public static void update() {	//for updating the overViewPanel
		DumsorRoot.selectionUpdate(selectedNode, labelList);
	}
	
	public static void getPowers() {	//called in each time step
		HashMap<HashSet<Node>, double[]> myMap = lineNetwork.getPowers();
		//myMap becomes a microgrid->powers-array relation
		
		boolean changeLList = false;	//better decide here and empty the list here
		//adding new labelpanels in the for cycle
		if (labelList.size() != myMap.keySet().size()) {
			changeLList = true;
			labelList.clear();
			//System.out.println("changing the list");
		}
		
		boolean tempShed = false;	//cannot switch the needToShed in a for cycle
		int index = 0;	//this is for reaching the arrayed labelPanels in the for cycle
		for (HashSet<Node> microgrid : myMap.keySet()) {
			double balance = myMap.get(microgrid)[2];
			if (balance < 0) {
				System.out.println("negative power balance");
				if (needToShed == false) {	//first time just pause for warning
					TimePanel.stopSelected();
					tempShed = true;
					Popups.shedWarnPopup(50);
				}
				else {	//second time shut down the negative balanced microgrid
					for (Node discNode : microgrid) {
						disconnect(discNode);
					}
					needToShed = false;	//if there are more negatives in the cycle,
					//stop again and warn
					Popups.shedCollapsePopup(50);
				}				
			}
			else {
				//System.out.println("positive power balance");
			}
			
			if (changeLList == true) {
				labelList.add(new PowerLabelPanel());
				//System.out.println("adding labels");
			}
			labelList.get(index).updateText(myMap.get(microgrid)[0], myMap.get(microgrid)[1]);
			index++;
			//System.out.println("all power");
		}		
		if (tempShed == true) {
			needToShed = true;
		}
		else {
			needToShed = false;
		}
		
		if (allElectPopupped == false) {	//popupping close-to-win condition
			boolean isAllElect = true;
			for (Node aNode : nodes) {
				if (aNode.getFullyElect() == false) {
					isAllElect = false;
				}
			}
			if (isAllElect == true) {
				allElectPopupped = true;
				Popups.allElectPopup(50);
			}
		}
		else {	//popupping win condition
			if (winPopupped == false) {
				boolean isAllMax = true;
				for (Node aNode : nodes) {
					if (aNode.getFullyMaxed() == false) {
						isAllMax = false;
					}
				}
				if (isAllMax == true) {
					winPopupped = true;
					Popups.winPopup(50);
				}
			}
		}
	}
	
	public static void removeRandomStuff() {	//if maintenance is not paid
		//Random rand = new Random();
		Popups.eraseSentences();
		if (lineNetwork.randomDestroyLines() == true) {	//action in condition
			assignNetworkToVisual();
		}
		for (Node aNode : nodes) {
			if (aNode instanceof District) {
				((District) aNode).destroyRandomPV();
				((District) aNode).destroyRandomBatt();
			}
		}
		Popups.termPVSent();
		Popups.termBattSent();
		Popups.maintDamagePopup();
	}
	
}
