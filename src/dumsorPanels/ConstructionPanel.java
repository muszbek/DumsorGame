package dumsorPanels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import dumsorBase.*;

public class ConstructionPanel extends InnerControlPanel {
	private JToggleButton constructLineButton;
	private JPanel constDataSpace;
	private boolean lineConstPopupped = false;
	
	private static final int buttonVertOffset = 10;
	private static final int buttonHorOffset = 10;
	private static final int buttonHeight = 40;
	private static final int buttonWidth = 150;
	private static final int constDataPanelOffset = buttonVertOffset + buttonHeight + 10;
	
	public ConstructionPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.setLayout(null);
		super.contentPanel.setLayout(null);
		constructLineButton = new JToggleButton("Construct Line");
		constructLineButton.setBounds(buttonHorOffset, buttonVertOffset,
				buttonWidth, buttonHeight);
		
		constDataSpace = new JPanel(new FlowLayout());
		constDataSpace.setBounds(0, constDataPanelOffset, 
				super.panelWidth, super.panelHeight-constDataPanelOffset);
		constDataSpace.setOpaque(false);
		//constDataPanel.setBackground(Color.black);
		
		constructLineButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
                boolean selected = constructLineButton.isSelected();
                if (selected==true) {
                	DumsorRoot.setLineConstruction(true);
                	if (lineConstPopupped == false) {
                		lineConstPopupped = true;
                		Popups.lineConstPopup(50);
                	}
                }
                else {
                	quitLineConst();
                }
        	}
        });
		
		super.contentPanel.add(constructLineButton);
		super.contentPanel.add(constDataSpace);
	}
	
	public void quitLineConst () {	//this is called outside when gamemode is changed
		DumsorRoot.setLineConstruction(false);
		constructLineButton.setSelected(false);
	}
	
	public void update(Node selected) {
		constDataSpace.removeAll();
		if (selected instanceof District == true) {
			District selectedDistrict = (District) selected;
			constDataSpace.add(selectedDistrict.getConstDataPanel());
		}
	}

}
