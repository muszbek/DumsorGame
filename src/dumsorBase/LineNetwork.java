package dumsorBase;

import java.awt.Point;
import java.util.*;
import dumsorPanels.*;

public class LineNetwork {
	private short[][] connectionMatrix; // matrix containing the node connections
	private int nodeAmount;
	private ArrayList<Node> nodes;
	private HashSet<HashSet<Node>> gridsElectricity;	//check grids and what is electrified
	private HashSet<HashSet<Node>> gridsDiscon;		//check grids and what is disconnected
	private HashSet<Node> sources; // set of sources for checking electrification, microgrid
	private HashSet<Point[]> connectedLines;
	private HashSet<Point[]> disconnectedLines;
	private HashMap<HashSet<Node>, double[]> netPowers;
	//summed production, consumption and net for microgrids

	public LineNetwork(ArrayList<Node> nodeArray) {
		nodes = nodeArray;
		nodeAmount = nodeArray.size();
		connectionMatrix = new short[nodeAmount][nodeAmount];
		gridsElectricity = new HashSet<HashSet<Node>>();
		gridsDiscon = new HashSet<HashSet<Node>>();
		sources = new HashSet<Node>();
		netPowers = new HashMap<HashSet<Node>, double[]>();

		for (int i = 0; i < nodeAmount; i++) {
			if (nodes.get(i) instanceof Substation) {	//substations are sources at init
				setSource(true, nodes.get(i));
			}
		}
	}
	
	public HashMap<HashSet<Node>, double[]> getPowers() {	//obtain power production, consumption
		netPowers.clear();
		//testGrid(gridsDiscon);
		for (Node aNode : nodes) {
			aNode.consume();
			aNode.produce();
			aNode.updateLabels();
			if (aNode instanceof District) {
				((District) aNode).updateHappiness();
			}
		}
		for (HashSet<Node> microgrid : gridsDiscon) {	//for each microgrid seperately
			double[] addPower = new double[3];
			for (Node aNode : microgrid) {
				addPower[0] += aNode.getProduction();
				addPower[1] += aNode.getConsumption();
			}
			addPower[2] = addPower[0]-addPower[1];	//power balance
			netPowers.put(microgrid, addPower);
		}
		return netPowers;
	}

	public void setSource(boolean isSource, Node myNode) {
		if (isSource == true) {
			if (sources.contains(myNode) == false) {
				sources.add(myNode);
				int idNode = nodes.indexOf(myNode);
				connectionMatrix[idNode][idNode] = 1;
				putGrid(myNode, gridsElectricity);
				electrify(idNode);
			}
		} else {
			if (sources.contains(myNode) == true) {
				sources.remove(myNode);
			}
		}
		myNode.setSource(isSource);
		checkSource();		
		//other sources will be set from upside, probably electricgrid
	}
	
	private void putGrid(Node aNode, HashSet<HashSet<Node>> whichGrid) {
		//adding a single element to the system, like the first substation
		boolean partOf = false;
		for (HashSet<Node> microgrid : whichGrid) {
			if (microgrid.contains(aNode) == true) {
				partOf = true;
			}
		}
		if (partOf == false) {
			whichGrid.add(new HashSet<Node>(Arrays.asList(aNode)));
		}
	}
	
	private void joinGrids(HashSet<Node> grid1, HashSet<Node> grid2, 
			HashSet<HashSet<Node>> whichGrid) {
		if (grid1 != grid2) {
			if (whichGrid.contains(grid1) == true && 
					whichGrid.contains(grid2) == true) {
				HashSet<Node> newgrid = new HashSet<Node>();
				for (Node aNode : grid1) {
					newgrid.add(aNode);
				}
				for (Node bNode : grid2) {
					if (newgrid.contains(bNode) == false) {
						newgrid.add(bNode);
					}
				}
				whichGrid.remove(grid1);
				whichGrid.remove(grid2);
				whichGrid.add(newgrid);
			}
		}
	}
	
	private void addGrid(HashSet<Node> toGrid, Node addNode, HashSet<HashSet<Node>> whichGrid) {
		//hashset is immutable, reference changes so I need to re-add whole grid upon a change
		if (toGrid.contains(addNode) == false) {
			HashSet<Node> newgrid = new HashSet<Node>();
			for (Node aNode : toGrid) {
				newgrid.add(aNode);
			}
			newgrid.add(addNode);
			whichGrid.remove(toGrid);
			whichGrid.add(newgrid);
		}
		
	}
	
	private void connectGrid(Node node1, Node node2, HashSet<HashSet<Node>> whichGrid) {
		HashSet<Node> gridOf1 = null;
		HashSet<Node> gridOf2 = null;
		for (HashSet<Node> microgrid1 : whichGrid) {
			if (microgrid1.contains(node1) == true) {
				gridOf1 = microgrid1;
			}
		}
		for (HashSet<Node> microgrid2 : whichGrid) {
			if (microgrid2.contains(node2) == true) {
				gridOf2 = microgrid2;
			}
		}
		if (gridOf1 == null) {
			if (gridOf2 == null) {	//if neither is contained, create a new microgrid
				whichGrid.add(new HashSet<Node>(Arrays.asList(node1, node2)));
			}
			else {	//if one is contained, the other joins its grid
				addGrid(gridOf2, node1, whichGrid);
			}
		}
		else {
			if (gridOf2 == null) {
				addGrid(gridOf1, node2, whichGrid);
			}
			else {	//if both are contained, their grids join each other
				joinGrids(gridOf1, gridOf2, whichGrid);
				
			}
		}
	}
	
	private void updateGrid() {
		gridsDiscon.clear();
		for (int i = 0; i < nodeAmount; i++) {
			if (connectionMatrix[i][i] == 1) {
				putGrid(nodes.get(i), gridsDiscon);
			}
			for (int j = 0; j < i; j++) { // check up until diagonal, connect nodes
				if (connectionMatrix[j][i] == 1) {
					connectGrid(nodes.get(i), nodes.get(j), gridsDiscon);
				}
			}
		}
		//testGrid(gridsDiscon);
	}
	
	private void testGrid(HashSet<HashSet<Node>> whichGrid) {	
		//this is just for printing out the grid contents
		System.out.println("start evaluation");
		for (HashSet<Node> microgrid : whichGrid) {
			System.out.println("new microgrid");
			for (Node aNode : microgrid) {
				System.out.println(nodes.indexOf(aNode) + " is here");
			}
		}
	}

	private void checkSource() {
		updateGrid();
		HashSet<HashSet<Node>> removables = new HashSet<HashSet<Node>>();
		//the disconnected microgrids need to be removed from the hashset
		for (HashSet<Node> microgrid : gridsDiscon) {
			boolean isThereASource = false;
			for (Node sourceNode : sources) {	//check if there is a source in the microgrid
				if (microgrid.contains(sourceNode) == true) {
					isThereASource = true;
					break;
				}
			}
			if (isThereASource == false) {	//if no source, disconnect the whole microgrid
				for (Node discNode : microgrid) {
					disconnectSecondary(nodes.indexOf(discNode));
				}
				removables.add(microgrid);	//add the grid to further on remove from hashset
			}
		}
		for (HashSet<Node> removeGrid : removables) {
			gridsDiscon.remove(removeGrid);
		}
	}

	private void disconnectSecondary(int id) {	//checkSource calls this, so I cannot check
		//source in this method
		if (nodes.get(id).getNodeEnabled()==true) {		//if it's not disconnected already
			if (nodes.get(id).getElectrified()==true) {		//can only disconnect electrified
				for (int i = 0; i < id; i++) {
					if (connectionMatrix[i][id] == 1) {
						connectionMatrix[i][id] = -1;
					}
				}
				for (int j = id+1; j < nodeAmount; j++) {
					if (connectionMatrix[id][j] == 1) {
						connectionMatrix[id][j] = -1;
					}
				}
				if (nodes.get(id).getSource() == false && connectionMatrix[id][id] == 1) {
					//a source gets isolated but it remains 1 in the diagonal
					connectionMatrix[id][id] = -1;
				}
				if (nodes.get(id).getSource() == false) {
					// a source cannot get disabled
					nodes.get(id).setNodeEnabled(false);
				}
				LoadSheddingPanel.setButton(ElectricGrid.getSelected());
			}
		}
	}
	
	public boolean sourceConnected(Node aNode) {	//for being able to decide if a source is
		//disconnected, because they are always enabled (LoadSheddingPanel.setButton)
		int id = nodes.indexOf(aNode);
		boolean isDisconnected = true;
		
		for (int i = 0; i < id; i++) {
			if (connectionMatrix[i][id] == 1) {
				isDisconnected = false;
			}
		}
		for (int j = id+1; j < nodeAmount; j++) {
			if (connectionMatrix[id][j] == 1) {
				isDisconnected = false;
			}
		}
		
		return isDisconnected;
	}
	
	public void disconnect(int id) {	//this is what will actually be called for disconnect
		if (nodes.get(id).getNodeEnabled()==true) {
			disconnectSecondary(id);
			checkSource();
		}
	}

	public void reconnect(int id) {
		if (nodes.get(id).getNodeEnabled()==false || nodes.get(id).getSource() == true) {
			if (connectionMatrix[id][id] == -1) {
				connectionMatrix[id][id] = 1;
			}
			for (int i = 0; i < id; i++) {
				if (connectionMatrix[i][id] == -1 && connectionMatrix[i][i] == 1) {
					connectionMatrix[i][id] = 1;
				}
			}
			for (int j = id+1; j < nodeAmount; j++) {
				if (connectionMatrix[id][j] == -1 && connectionMatrix[j][j] == 1) {
					connectionMatrix[id][j] = 1;
				}
			}
			nodes.get(id).setNodeEnabled(true);
			checkSource();	//layout changed, check for who is disconnected
			//if reconnect something without a source, disconnects immediately
		}
	}

	public boolean connect(int id1, int id2) { // boolean if actual connection got made
		boolean result = false;
		if (id1 == id2) { // if nodes are same, no connection can be made
			System.out.println("in principle I step out here, connect with itself");
			return result;
		}
		if (id2 < id1) { // make sure id1 is smaller than id2
			int temp = id2;
			id2 = id1;
			id1 = temp;
		}
		if (connectionMatrix[id1][id2] != -1 && connectionMatrix[id1][id2] != 1) { 
		// if there is no connection there yet, or it is being built
		//if (getConnected(id1, id2) == false) { // if there is no connection there yet
			if (connectionMatrix[id1][id1] == 0) {	//diagonal has to be set at connection
				if (connectionMatrix[id2][id2] == 1) {
					connectionMatrix[id1][id1] = 1;
				}
				else {
					connectionMatrix[id1][id1] = -1;
				}
			}
			if (connectionMatrix[id2][id2] == 0) {	//same, other diagonal
				if (connectionMatrix[id1][id1] == 1) {
					connectionMatrix[id2][id2] = 1;
				}
				else {
					connectionMatrix[id2][id2] = -1;
				}
			}
			if (connectionMatrix[id1][id1] == -1 || connectionMatrix[id2][id2] == -1) {
				// if one of the nodes are disconnected, the connection will be disconnected
				connectionMatrix[id1][id2] = -1;
			} else {
				connectionMatrix[id1][id2] = 1;
			}
			result = true;
			connectGrid(nodes.get(id1), nodes.get(id2), gridsElectricity);
			if (nodes.get(id1).getElectrified()==true && 
					nodes.get(id2).getElectrified()==false) {
				electrify(id2);
			}
			else if (nodes.get(id1).getElectrified()==false && 
					nodes.get(id2).getElectrified()==true) {
				electrify(id1);
			}
			checkSource();
		}
		return result;
	}
	
	public boolean destroyConnect(int id1, int id2) {	//for destroying specific line
		boolean result = false;
		if (id1 == id2) {
			System.out.println("Destroying line invalid");
			return result;
		}
		if (connectionMatrix[id1][id2] == 1 || connectionMatrix[id1][id2] == -1) {
			connectionMatrix[id1][id2] = 0;
			checkSource();
			result = true;
		}
		return result;
	}
	
	public boolean getConnected(int id1, int id2) {	//check if there is a connection here
		boolean isConnected = false;
		if (id1 == id2) { // if nodes are same, no connection can be made
			return isConnected;
		}
		if (id2 < id1) { // make sure id1 is smaller than id2
			int temp = id2;
			id2 = id1;
			id1 = temp;
		}
		if (connectionMatrix[id1][id2] != 0) {
			isConnected = true;
		}
		return isConnected;
	}
	
	public void startConnConstruction(int id1, int id2) {
		//for distinguishing under construction lines
		if (id1 == id2) { // if nodes are same, no connection can be made
			return;
		}
		if (id2 < id1) { // make sure id1 is smaller than id2
			int temp = id2;
			id2 = id1;
			id1 = temp;
		}
		connectionMatrix[id1][id2] = 2;
	}
	
	private void electrify(int id) {
		for (HashSet<Node> microgrid : gridsElectricity) {
			if (microgrid.contains(nodes.get(id)) == true) {
				for (Node aNode : microgrid) {
					int idNode = nodes.indexOf(aNode);
					if (connectionMatrix[idNode][idNode] == 1) {
						aNode.electrify(true);
					}
					else if (connectionMatrix[idNode][idNode] == -1) {
						aNode.electrify(false);
					}
				}
			}
		}
	}
	
	private void updateLineSets() {		//saves the locations of where lines should be drawn
		//two sets of pairs of points, for connected and disconnected lines
		connectedLines = new HashSet<Point[]>();
		disconnectedLines = new HashSet<Point[]>();

		for (int i = 0; i < nodeAmount; i++) {
			for (int j = i+1; j < nodeAmount; j++) {
				if (connectionMatrix[i][j] != 0) {
					Point[] connection = new Point[2];
					connection[0]=nodes.get(i).getPos();
					connection[1]=nodes.get(j).getPos();
					if (connectionMatrix[i][j]==1) {
						connectedLines.add(connection);
					}
					else if (connectionMatrix[i][j]==-1) {
						disconnectedLines.add(connection);
					}
				}
			}
		}
	}
	
	public HashSet<Point[]>[] getLineSets() {	//gets the sets of line locations, coupled
		updateLineSets();
		HashSet<Point[]>[] returnSet = new HashSet[2];
		returnSet[0] = connectedLines;
		returnSet[1] = disconnectedLines;
		return returnSet;
	}
	
	public boolean randomDestroyLines() {	//checking all line connections and destroying them
		HashSet<int[]> connections = new HashSet<int[]>();
		for (int i=0; i<nodeAmount; i++) {
			for (int j=0; j<i; j++) {
				if (connectionMatrix[j][i] == 1 || connectionMatrix[j][i] == -1) {
					connections.add(new int[]{j,i});
				}
			}
		}
		
		boolean recheckLines = false;
		//Random random = new Random();
		
		for (int[] myConn : connections) {
			if (Math.random() < MaintenancePanel.lineDestroyPercent) {
				//each line is randomly destroyed or not
				int id1 = myConn[0];
				int id2 = myConn[1];
				if (destroyConnect(id1, id2) == true) {
					Point pos1 = nodes.get(id1).getPos();
					Point pos2 = nodes.get(id2).getPos();
					int removeCost = (int)pos1.distance(pos2) * MaintenancePanel.lineUnitMaint;
					MaintenancePanel.removeMaintCost(removeCost);
					recheckLines = true;
					//if there is a destroyed line, lineVisuals will be rechecked
					Popups.appendLineSent(" " + nodes.get(id1).getName() + " - " 
							+ nodes.get(id2).getName() + ",");
				}
			}
		}
		Popups.termLineSent();
		
		return recheckLines;
	}

}
