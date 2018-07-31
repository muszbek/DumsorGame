package dumsorPanels;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;
import dumsorBase.*;
import dumsorPanels.*;

public class MoneyPanel extends JPanel implements ResizeablePanel {
	private JLabel textLabel;
	private int panelWidth;
	private int panelHeight;
	private int locX;
	private int locY;
	
	private static JLabel balanceLabel;
	private static double balance;
	private static final int lineUnitPrice = 1000;
	public static final int pvUnitPrice = 500;
	public static final int batteryUnitPrice = 2000;
	public static final int electrifyPrice = 100;
	public static final int unitPowerPrice = 1;
	private static final DecimalFormat df = new DecimalFormat("#0");
	
	public MoneyPanel(int x, int y, int width, int height) {
		locX=x;
		locY=y;
		sizePanel(width, height);
		setOpaque(false);
		
		balance = 1000000; 	//starting money
		textLabel = new JLabel("Current balance: ");
		balanceLabel = new JLabel();
		balanceLabel.setForeground(Color.blue);
		updateText();
		
		this.add(textLabel);
		this.add(balanceLabel);
	}
	
	private static void updateText() {
		balanceLabel.setText(df.format(balance) + " GHS");
	}
	
	public static boolean spendMoney(int spend) {
		boolean didSpend = true;
		if (balance >= spend) {
			balance -= spend;
			updateText();
		}
		else {
			didSpend = false;
			Popups.noMoneyPopup();
		}
		return didSpend;
	}
	
	public static void earnMoney(double earn) {
		balance += earn;
		updateText();
	}
	
	public static int priceLine(Node from, Node to) {
		double lineLength = from.getPos().distance(to.getPos());
		int linePrice = (int)(lineLength * lineUnitPrice);
		return linePrice;
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
