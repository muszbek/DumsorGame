package dumsorPanels;

import imported.WrapLayout;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import dumsorBase.*;

public class ConstructionDataPanel extends JPanel {
	//pv HUD
	private JPanel pvPanel;
	private JLabel pvLabel;
	private JButton pvButton;
	private static final int pvIncrement = 10;	//won't stay like this
	private static final int pvMax = 1000;	//how much should it be???
	private int households;
	private JSpinner pvSpinner;
	private SpinnerNumberModel pvSNM;
	//house electrification HUD
	private JPanel housePanel;
	private JLabel houseLabel;
	private JButton houseButton;
	private JSpinner houseSpinner;
	private SpinnerNumberModel houseSNM;
	//battery HUD
	private JPanel batPanel;
	private JLabel batLabel;
	private JButton batButton;
	private static final int batIncrement = 10;	//won't stay like this
	private static final int batMax = 1000;	//how much should it be???
	private JSpinner batSpinner;
	private SpinnerNumberModel batSNM;

	public ConstructionDataPanel(int houses) {
		super(new WrapLayout());
		this.setOpaque(false);
		households = houses;
		pvPanel = new JPanel();
		pvPanel.setOpaque(false);
		pvLabel = new JLabel("PV capacity: 0 kW");
		pvButton = new JButton("Buy PV");
		pvSNM = new SpinnerNumberModel(0, 0, pvMax, pvIncrement);
		pvSpinner = new JSpinner(pvSNM);
		
		pvButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (ElectricGrid.getSelected() instanceof District == true) {
        			//condition is always true in theory, cannot click on non-district
        			District selectedDistrict = (District) ElectricGrid.getSelected();
        			selectedDistrict.buyPV((int)pvSpinner.getValue());
        			pvSpinner.setValue(0);
        		}
        	}
        });
		
		housePanel = new JPanel();
		housePanel.setOpaque(false);
		houseLabel = new JLabel("Electrified households: 0/" + households);
		houseButton = new JButton("Electrify house");
		houseSNM = new SpinnerNumberModel(0, 0, households, 5);
		houseSpinner = new JSpinner(houseSNM);
		houseEnable(false);
		
		houseButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (ElectricGrid.getSelected() instanceof District == true) {
        			//condition is always true in theory, cannot click on non-district
        			District selectedDistrict = (District) ElectricGrid.getSelected();
        			selectedDistrict.electrifyHouse((int)houseSpinner.getValue());
        			houseSpinner.setValue(0);
        		}
        	}
        });
		
		batPanel = new JPanel();
		batPanel.setOpaque(false);
		batLabel = new JLabel("Battery capacity: 0 kWh");
		batButton = new JButton("Buy battery");
		batSNM = new SpinnerNumberModel(0, 0, batMax, batIncrement);
		batSpinner = new JSpinner(batSNM);
		
		batButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (ElectricGrid.getSelected() instanceof District == true) {
        			//condition is always true in theory, cannot click on non-district
        			District selectedDistrict = (District) ElectricGrid.getSelected();
        			selectedDistrict.buyBattery((int)batSpinner.getValue());
        			batSpinner.setValue(0);
        		}
        	}
        });
		
		pvPanel.add(pvLabel);
		pvPanel.add(pvSpinner);
		pvPanel.add(pvButton);
		housePanel.add(houseLabel);
		housePanel.add(houseSpinner);
		housePanel.add(houseButton);
		batPanel.add(batLabel);
		batPanel.add(batSpinner);
		batPanel.add(batButton);
		
		this.add(housePanel);
		this.add(pvPanel);
		this.add(batPanel);
	}
	
	public void houseEnable(boolean isEnabled) {
		houseButton.setEnabled(isEnabled);
		houseSpinner.setEnabled(isEnabled);
	}
	
	public void updatePVtext(int PVpower) {
		pvLabel.setText("PV capacity: " + PVpower + " kW");
	}
	
	public void updateHouseText(int houses, int total) {
		houseLabel.setText("Electrified households: " + houses + "/" + total);
	}
	
	public void updateBatText(double batCap) {
		batLabel.setText("Battery capacity: " + (int)batCap + " kW");
	}
	
	public void maxHouses() {
		houseLabel.setForeground(Color.green);
		houseLabel.setText("Fully electrified district");
		houseEnable(false);
	}
	
	public void setHouseSpinnerMax(int max) {
		houseSNM.setMaximum(max);
	}

}