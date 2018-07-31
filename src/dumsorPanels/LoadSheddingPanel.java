package dumsorPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import dumsorBase.*;


public class LoadSheddingPanel extends InnerControlPanel {
	private static JToggleButton disconnectButton;
	private boolean discPopupped = false;
	private boolean recPopupped = false;
	
	private static final int buttonVertOffset = 10;
	private static final int buttonHorOffset = 10;
	private static final int buttonHeight = 40;
	private static final int buttonWidth = 150;
	
	public LoadSheddingPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.setLayout(null);
		disconnectButton = new JToggleButton();
		disconnectButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		
		disconnectButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		boolean selected = disconnectButton.isSelected();
        		if (selected == true) {
        			disconnectButton.setText("Reconnect");
        			ElectricGrid.disconnect(ElectricGrid.getSelected());
        			if (discPopupped == false) {
        				discPopupped = true;
        				Popups.disconnectPopup(50);
        			}
                }
                else {
                	disconnectButton.setText("Disconnect");
                	ElectricGrid.reconnect(ElectricGrid.getSelected());
                	if (recPopupped == false) {
                		recPopupped = true;
                		Popups.reconnectPopup(50);
                	}
                }
        	}
        });
		disconnectButton.setText("Disconnect");
		disconnectButton.setEnabled(false);
		super.contentPanel.add(disconnectButton);
	}
	
	public void addHappinessPanel(HappinessPanel newPanel) {
		contentPanel.add(newPanel);
	}
	
	public static void setButton(Node myNode) {	//set the state of the node based on
		//status of selected node
		if (myNode != null) {	//if none selected, button is disabled
			if (myNode.getElectrified() == false || myNode instanceof Substation) {
				//if node is not electrified, or it is a substation -> no button
				disconnectButton.setSelected(false);
				disconnectButton.setEnabled(false);
			}
			else {
				disconnectButton.setEnabled(true);
				if (myNode.getNodeEnabled() == true) {
					if (myNode.getSource() == true) {	//sources have to be reconnected too
						disconnectButton.setSelected(
								ElectricGrid.sourceConnected(ElectricGrid.getSelected()));
					}
					else {
						disconnectButton.setSelected(false);
					}
				}
				else {
					disconnectButton.setSelected(true);
				}
			}
		}
		else {
			disconnectButton.setSelected(false);
			disconnectButton.setEnabled(false);
		}
		//change button text
		if (disconnectButton.isSelected() == true) {
			disconnectButton.setText("Reconnect");
		}
		else {
			disconnectButton.setText("Disconnect");
		}
	}

}