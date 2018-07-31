package dumsorPanels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import dumsorBase.*;

public class ControlPanel extends JPanel implements ResizeablePanel {
	private BufferedImage controlPanelBg;
	private int panelWidth;
	private int panelHeight;
	private int drawWidth;
	private int drawHeight;
	private int innerPanelWidth;	//happinessPanel needs it
	private JPanel buttonPanel;
	private JToggleButton overViewButton;
	private JToggleButton constructionButton;
	private JToggleButton loadSheddingButton;
	private OverViewPanel overViewPanel;
	private ConstructionPanel constructionPanel;
	private LoadSheddingPanel loadSheddingPanel;
	private TimePanel timePanel;
	private MoneyPanel moneyPanel;
	private MaintenancePanel maintPanel;
	private ImagePanel imagePanel;
	private JButton creditButton;
	private ImageIcon creditIcon;
	private JButton helpButton;
	private ImageIcon helpIcon;
	private boolean constPopupped = false;
	private boolean loadSheddingPopupped = false;
	
	private static final int buttonVertOffset = 40;
	private static final int buttonHorOffset = 10;
	private static final int buttonHeight = 40;
	private static final int innerPanelVertOffset = 85;
	private static final int innerPanelHorOffset = buttonHorOffset;
	private static final int innerPanelHeight = 200;
	private static final int timeVertOffset = innerPanelVertOffset + innerPanelHeight + 5;
	private static final int timeHorOffset = 20;
	private static final int timeHeight = 60;
	private static final int moneyVertOffset = timeVertOffset + timeHeight + 5;
	private static final int moneyHorOffset = 20;
	private static final int moneyHeight = buttonHeight - 20;
	//private static final int moneyWidth = 200;
	private static final int maintVertOffset = moneyVertOffset + moneyHeight;
	private static final int maintHorOffset = 10;
	private static final int maintHeight = buttonHeight * 2;
	//private static final int maintWidth = 80;
	private static final int imgHorOffset = 20;
	private static final int imgBottomOffset = 10;
	private static final int imgHeight = 220;
	private static final int creditVerOffset = 5;
	private static final int creditHeight = 30;
	private static final int helpHorOffset = buttonHorOffset + creditHeight + 5;

	public ControlPanel(int width, int height) {
		// size is defined by the map and an arbitrary integer
		panelWidth = width;
		panelHeight = height;
		sizePanel(panelWidth, panelHeight);
		this.setLayout(null);

		try {
			this.controlPanelBg = ImageIO.read(ResLoader.loadStr(
					"pics/ghana_flag_map_opaque.jpg"));
		} catch (IOException e) {
			System.out.println("control panel image not found");
		}
		
		innerPanelWidth = panelWidth-2*innerPanelHorOffset;
		
		overViewButton = new JToggleButton("Overview");
		constructionButton = new JToggleButton("Construction");
		loadSheddingButton = new JToggleButton("Load Shedding");
		overViewPanel = new OverViewPanel(innerPanelHorOffset, innerPanelVertOffset,
				innerPanelWidth, innerPanelHeight);
		constructionPanel = new ConstructionPanel(innerPanelHorOffset, innerPanelVertOffset,
				innerPanelWidth, innerPanelHeight);
		loadSheddingPanel = new LoadSheddingPanel(innerPanelHorOffset, innerPanelVertOffset,
				innerPanelWidth, innerPanelHeight);
		timePanel = new TimePanel(timeHorOffset, timeVertOffset,
				panelWidth-2*timeHorOffset, timeHeight);
		moneyPanel = new MoneyPanel(moneyHorOffset, moneyVertOffset, 
				panelWidth-2*moneyHorOffset, moneyHeight);
		maintPanel = new MaintenancePanel(maintHorOffset, maintVertOffset,
				panelWidth-2*maintHorOffset, maintHeight);
		imagePanel = new ImagePanel(imgHorOffset, panelHeight-imgBottomOffset-imgHeight,
				panelWidth-2*imgHorOffset, imgHeight);
		
		creditIcon = new ImageIcon(ResLoader.loadImg("pics/person_icon.gif"));
		creditButton = new JButton(creditIcon);
		creditButton.setBounds(buttonHorOffset, creditVerOffset, creditHeight, creditHeight);
		helpIcon = new ImageIcon(ResLoader.loadImg("pics/help_icon.gif"));
		helpButton = new JButton(helpIcon);
		helpButton.setBounds(helpHorOffset, creditVerOffset, creditHeight, creditHeight);
		
		overViewButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
                boolean selected = overViewButton.isSelected();
                if (selected==true) {
                	overViewSelected();
                }
                else {
                	overViewButton.setSelected(true);
                }
        	}
        });
		constructionButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		boolean selected = constructionButton.isSelected();
                if (selected==true) {
                	constructionSelected();
                }
                else {
                	constructionButton.setSelected(true);
                }
        	}
        });
		loadSheddingButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		boolean selected = loadSheddingButton.isSelected();
                if (selected==true) {
                	loadSheddingSelected();
                }
                else {
                	loadSheddingButton.setSelected(true);
                }
        	}
        });
		creditButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Popups.creditPopup(80);
			}
		});
		helpButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (DumsorRoot.getMode() == DumsorRoot.GameMode.OVERVIEW) {
					Popups.overViewPopup(70);
				}
				else if (DumsorRoot.getMode() == DumsorRoot.GameMode.CONSTRUCTION) {
					Popups.constPopup(70);
				}
				else if (DumsorRoot.getMode() == DumsorRoot.GameMode.LOAD_SHEDDING) {
					Popups.loadSheddingPopup(70);
				}
			}
		});
		
		buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setBounds(buttonHorOffset, buttonVertOffset, 
				panelWidth-buttonHorOffset*2, buttonHeight);
		buttonPanel.setLayout(new GridLayout(1, 3, buttonHorOffset, 10));
		buttonPanel.add(overViewButton);
		buttonPanel.add(constructionButton);
		buttonPanel.add(loadSheddingButton);
		overViewButton.setSelected(true);
		overViewSelected();
		
		this.add(buttonPanel);
		this.add(timePanel);
		this.add(moneyPanel);
		this.add(maintPanel);
		this.add(imagePanel);
		this.add(creditButton);
		this.add(helpButton);

	}
	
	public LoadSheddingPanel getLSPanel() {
		return loadSheddingPanel;
	}
	
	public int getInnerPanelWidth() {
		return innerPanelWidth;
	}
	
	public void addHappinessPanel(HappinessPanel newPanel) {
		//loadSheddingPanel.contentPanel.add(newPanel);
		loadSheddingPanel.addHappinessPanel(newPanel);
		//System.out.println("happiness panel added");
	}

	@Override
	public void sizePanel(int width, int height) {
		drawWidth = width;
		drawHeight = height;
		setBounds(0, 0, drawWidth, drawHeight);
	}
	
	@Override
	public void resizePanel(double widthRate, double heightRate) {
		int newWidth = (int)(widthRate * panelWidth);
		int newHeight = (int)(heightRate * panelHeight);
		sizePanel(newWidth, newHeight);
		supportButtonResize(widthRate, heightRate);
		buttonResize(widthRate, heightRate);
		overViewPanel.resizePanel(widthRate, heightRate);
		constructionPanel.resizePanel(widthRate, heightRate);
		loadSheddingPanel.resizePanel(widthRate, heightRate);
		timePanel.resizePanel(widthRate, heightRate);
		moneyPanel.resizePanel(widthRate, heightRate);
		maintPanel.resizePanel(widthRate, heightRate);
		imagePanel.resizePanel(widthRate, heightRate);
	}
	
	private void overViewSelected() {
		constructionButton.setSelected(false);
    	loadSheddingButton.setSelected(false);
    	constructionPanel.quitLineConst();
    	DumsorRoot.setGameMode(DumsorRoot.GameMode.OVERVIEW);
    	this.remove(constructionPanel);
    	this.remove(loadSheddingPanel);
    	this.add(overViewPanel);
    	ElectricGrid.updateImage();
    	overViewPanel.revalidate();
    	this.repaint();
	}
	
	private void constructionSelected() {
		overViewButton.setSelected(false);
    	loadSheddingButton.setSelected(false);
    	DumsorRoot.setGameMode(DumsorRoot.GameMode.CONSTRUCTION);
    	this.remove(overViewPanel);
    	this.remove(loadSheddingPanel);
    	this.add(constructionPanel);
    	ElectricGrid.updateImage();
    	constructionPanel.revalidate();
    	this.repaint();
    	
    	if (Popups.getPopupsEnabled() == true) {
    		if (constPopupped == false) {
        		constPopupped = true;
        		Popups.constPopup(70);
        	}
    	}
    	
    	//ElectricGrid.selectNode(null);	//more convenient
	}
	
	private void loadSheddingSelected() {
		overViewButton.setSelected(false);
    	constructionButton.setSelected(false);
    	constructionPanel.quitLineConst();
    	DumsorRoot.setGameMode(DumsorRoot.GameMode.LOAD_SHEDDING);
    	this.remove(overViewPanel);
    	this.remove(constructionPanel);
    	this.add(loadSheddingPanel);
    	ElectricGrid.updateImage();
    	loadSheddingPanel.revalidate();
    	this.repaint();
    	
    	if (Popups.getPopupsEnabled() == true) {
    		if (loadSheddingPopupped == false) {
        		loadSheddingPopupped = true;
        		Popups.loadSheddingPopup(70);
        	}
    	}
	}
	
	public void selectionUpdate(Node selected, ArrayList<PowerLabelPanel> labels) {
		overViewPanel.update(selected, labels);
		constructionPanel.update(selected);
		if (DumsorRoot.getMode() == DumsorRoot.GameMode.OVERVIEW) {
			overViewPanel.revalidate();
			//overViewPanel.repaint();
		}
		ElectricGrid.updateImage();
		repaint();
	}
	
	private void buttonResize(double scaleX, double scaleY) {
		double newX = scaleX * buttonHorOffset;
		double newY = scaleY * buttonVertOffset;
		double newWidth = scaleX * panelWidth-buttonHorOffset*2;
		double newHeight = scaleY * buttonHeight;
		buttonPanel.setBounds((int)newX, (int)newY, (int)newWidth, (int)newHeight);
	}
	
	private void supportButtonResize(double scaleX, double scaleY) {
		double newX = scaleX * buttonHorOffset;
		double newY = scaleY * creditVerOffset;
		double newWidth = scaleX * creditHeight;
		double newHeight = scaleY * creditHeight;
		double newHelpX = scaleX * helpHorOffset;
		creditButton.setBounds((int)newX, (int)newY, (int)newWidth, (int)newHeight);
		helpButton.setBounds((int)newHelpX, (int)newY, (int)newWidth, (int)newHeight);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(controlPanelBg, 0, 0, drawWidth, drawHeight, this);
	}
}
//need constant location for the labels