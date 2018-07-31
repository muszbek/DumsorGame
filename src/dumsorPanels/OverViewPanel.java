package dumsorPanels;

import imported.WrapLayout;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import dumsorBase.Node;


public class OverViewPanel extends InnerControlPanel {
	
	public OverViewPanel(int x, int y, int width, int height) {
		super(x, y, width, height);
		//this.setBackground(Color.yellow);
		//this.setOpaque(false);
		this.setLayout(null);
		
	}
	
	public void update(Node selected, ArrayList<PowerLabelPanel> labels) {
		contentPanel.removeAll();
		if (selected == null) {
			for (PowerLabelPanel aPanel : labels) {
				int index = labels.indexOf(aPanel) + 1;
				contentPanel.add(new JLabel("-------------------- Grid #" + index +
						" --------------------"));
				contentPanel.add(aPanel);
			}
		}
		else {
			contentPanel.add(new JLabel("-------------------- " + selected.getNodeName() +
					" --------------------"));
			contentPanel.add(selected.getPowerLabels());
		}
	}

}
