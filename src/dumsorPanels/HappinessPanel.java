package dumsorPanels;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import dumsorBase.*;

public class HappinessPanel extends JPanel implements MouseListener{

	private final int horOffset = 10;
	private String name;
	private JLabel nameLabel;
	private JLabel percentLabel;

	private ImageIcon iconHappy;
	private ImageIcon iconSad;
	private ImageIcon iconAngry;

	private JLabel labelIcon = null;
	//private int iconSize = 10;
	
	public HappinessPanel(String districtName) {
		name = districtName;
		setOpaque(false);
		setLayout(new GridLayout(1, 3, horOffset, 10));
		int width = DumsorRoot.getControlPanel().getInnerPanelWidth() - 2 * horOffset;
		super.setPreferredSize(new Dimension(width, 20));
		nameLabel = new JLabel(name);
		percentLabel = new JLabel();
		
		iconHappy = new ImageIcon(ResLoader.loadImg("pics/face_happy.gif"));
		iconSad = new ImageIcon(ResLoader.loadImg("pics/face_sad.gif"));
		iconAngry = new ImageIcon(ResLoader.loadImg("pics/face_angry.gif"));
		
		labelIcon = new JLabel(iconHappy);
		
		add(nameLabel);
		add(percentLabel);
		add(labelIcon);
		addMouseListener(this);
		
		DumsorRoot.getControlPanel().addHappinessPanel(this);
	}
	
	public void updatePercent(int percent) {
		percentLabel.setText(percent + " %");
		updateIcon(percent);
	}
	
	public void updateIcon(int percent) {
		if (percent > 67) {
			labelIcon.setIcon(iconHappy);
		}
		else if (percent > 34) {
			labelIcon.setIcon(iconSad);
		}
		else {
			labelIcon.setIcon(iconAngry);
			Popups.dumsorRagePopup(60);
		}
	}
	
	public void setColorEnabled(boolean isEnabled) {
		if (isEnabled == true) {
			nameLabel.setForeground(Color.green);
		}
		else {
			nameLabel.setForeground(Color.red);
		}
	}
	
	public void setFontSelected(boolean selected) {
		int fontSize = 12;
		if (selected == true) {
			fontSize = 14;
		}
		nameLabel.setFont(new Font("Default", Font.BOLD, fontSize));
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		Node selectedNode = ElectricGrid.getNode(name);
		if (selectedNode == ElectricGrid.getSelected()) {
			ElectricGrid.selectNode(null);
		}
		else {
			ElectricGrid.selectNode(selectedNode);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}