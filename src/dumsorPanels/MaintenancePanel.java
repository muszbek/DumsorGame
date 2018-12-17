package dumsorPanels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import dumsorBase.*;

public class MaintenancePanel extends JPanel implements ResizeablePanel {
	
	public static final int lineUnitMaint = 12;
	public static final int PVUnitMaint = 30;
	public static final int batUnitMaint = 30;
	public static final double lineDestroyPercent = 0.05;
	public static final double PVDestroyPercent = 0.05;
	public static final double batDestroyPercent = 0.05;
	
	private static boolean spendPopupped = false;
	private JPanel duePanel;
	private JLabel dueLabel;
	private static JLabel dueNumber;
	private JPanel costPanel;
	private JLabel costLabel;
	private static JLabel costNumber;
	private static JToggleButton refuseButton;
	private static final Color orange = new Color(255, 80, 0);
	
	private static int maintCost = 0;	//accumulated maintenance cost of equipment
	private static final int maintFreq = 10;	//every few days maintenance cost is paid
	private static int maintCountdown;
	private static boolean paying = true;
	
	private int panelWidth;
	private int panelHeight;
	private int locX;
	private int locY;
	

	public MaintenancePanel(int x, int y, int width, int height) {
		locX=x;
		locY=y;
		sizePanel(width, height);
		setOpaque(false);
		//setLayout(null);
		
		duePanel = new JPanel();
		duePanel.setOpaque(false);
		dueLabel = new JLabel("Maintenance due in: ");
		dueLabel.setFont(new Font("Default", Font.PLAIN, 11));
		dueNumber = new JLabel();
		dueNumber.setFont(new Font("Default", Font.PLAIN, 11));
		//dueNumber.setForeground(Color.yellow);
		duePanel.add(dueLabel);
		duePanel.add(dueNumber);
		
		costPanel = new JPanel();
		costPanel.setOpaque(false);
		costLabel = new JLabel("Cost: ");
		costLabel.setFont(new Font("Default", Font.PLAIN, 11));
		costNumber = new JLabel("0 GHS");
		costNumber.setFont(new Font("Default", Font.PLAIN, 11));
		costNumber.setForeground(orange);
		costPanel.add(costLabel);
		costPanel.add(costNumber);
		
		refuseButton = new JToggleButton("Stop maint");
		refuseButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (refuseButton.isSelected() == true) {
        			int answer = Popups.skipMaintPopup(40);
        			if (answer == JOptionPane.YES_OPTION) {
        				refuseButton.setText("Cont maint");
        			}
        			else {
        				refuseButton.setSelected(false);
        			}
        			
        		}
                if (refuseButton.isSelected() == false) {
                	if (paying == false) {
                		if (MoneyPanel.spendMoney(maintCost) == true) {
                			//paying maintenance, reset
                			paying = true;
                			maintCountdown = maintFreq;
                			refuseButton.setText("Stop maint");
                		}
                		else {	//not able to pay, continue being in refusal
                			refuseButton.setSelected(true);
                		}
                	}
                	else {
                		refuseButton.setText("Stop maint");
                	}
                }
        	}
        });
		refuseButton.setFont(new Font("Default", Font.PLAIN, 10));
		add(duePanel);
		add(costPanel);
		add(refuseButton);		
	}
	
	public static void addMaintCost(int cost) {
		if (maintCost == 0) {	//maintenance starts after first purchase
			maintCountdown = maintFreq;
		}
		maintCost += cost;
		updateVisual();
		if (spendPopupped == false) {
			spendPopupped = true;
			Popups.spendMoneyPopup(62);
		}
	}
	
	public static void removeMaintCost(int cost) {
		if (maintCost >= cost) {
			maintCost -= cost;
		}
		else {
			maintCost = 0;
		}
		updateVisual();
	}
	
	public static void updateMaint() {
		if (maintCost != 0) {	//if there is equipment to maintained
			if (ElectricGrid.getTime().getHour() == 0) {	//at every dayturn
				if (maintCountdown == 0) {	//at due maintenance
					if (refuseButton.isSelected() == true) {
						//if the button is pressed at due maintenance, not paying is valid
						paying = false;
					}
					if (paying == true) {	//if paying correctly
						if (MoneyPanel.spendMoney(maintCost) == true) {	//if able to pay
							//nothing happens, action is in the condition
						}
						else {	//if not able to pay
							refuseButton.setSelected(true);
							paying = false;
							
							thigsGoWrong();
						}
					}
					else {	//if not paying
						
						thigsGoWrong();
					}
					maintCountdown = maintFreq;	//reset due anyway
				}
				else {	//not at due maintenance
					maintCountdown--;
				}
				updateVisual();
			}
		}
		
	}
	
	private static void updateVisual() {
		dueNumber.setText(maintCountdown + " days");
		costNumber.setText(maintCost + " GHS");
	}
	
	private static void thigsGoWrong() {
		ElectricGrid.removeRandomStuff();
		System.out.println("things go wrong");
	}

	@Override
	public void sizePanel(int width, int height) {
		panelWidth = width;
		panelHeight = height;
		this.setBounds(locX, locY, panelWidth, panelHeight);
	}

	@Override
	public void resizePanel(double widthRate, double heightRate) {
		double newWidth = widthRate * panelWidth;
		double newHeight = heightRate * panelHeight;
		double newX = widthRate * locX;
		double newY = heightRate * locY;
		setBounds((int)newX, (int)newY, (int)newWidth, (int)newHeight);
	}

}