package dumsorPanels;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class PowerLabelPanel extends JPanel {
	
	private JLabel producedLabel;
	private JLabel consumedLabel;
	private JLabel netLabel;
	private JLabel batLabel;	//for batteries, not always visible
	private final int labelHorOffset = 10;
//	private final int thirdLoc = panelWidth/3;
//	private final int width = thirdLoc;
//	private final int height = panelHeight;
	private DecimalFormat df;
	private static final Color green = new Color(0, 150, 0);

	public PowerLabelPanel() {
		setOpaque(false);
		df = new DecimalFormat("#0.###");
		producedLabel = new JLabel();
		consumedLabel = new JLabel();
		netLabel = new JLabel();
		batLabel = new JLabel();
//		producedLabel.setFont(new Font("Default", Font.BOLD, 11));
//		consumedLabel.setFont(new Font("Default", Font.BOLD, 11));
//		netLabel.setFont(new Font("Default", Font.BOLD, 11));
//		batLabel.setFont(new Font("Default", Font.BOLD, 11));
//		producedLabel.setBounds(0, 0, width, height);
//		consumedLabel.setBounds(0, thirdLoc, width, height);
//		netLabel.setBounds(0, thirdLoc*2, width, height);
		setLayout(new GridLayout(2, 2, labelHorOffset, 10));
		//setLayout(null);
		add(producedLabel);
		add(consumedLabel);
		add(netLabel);
		add(batLabel);
	}
	
	public void updateText(double prod, double cons) {
		double net = prod - cons;
		producedLabel.setText("Produced: " + df.format(prod) + " kW");
		consumedLabel.setText("Consumed: " + df.format(cons) + " kW");
		netLabel.setText("Balance: " + df.format(net) + " kW");
		if (net >= 0) {
			netLabel.setForeground(green);
		}
		else {
			netLabel.setForeground(Color.red);
		}
	}
	
	public void updateBatText(double charge) {
		batLabel.setText("Charge: " + df.format(charge) + " kWh");
	}

}
