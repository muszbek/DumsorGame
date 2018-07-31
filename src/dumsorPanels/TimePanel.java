package dumsorPanels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import dumsorBase.*;

//this is for the buttons which speed up or pause the time
public class TimePanel extends JPanel implements ResizeablePanel{
	
	private static JToggleButton stopButton;
	private static JToggleButton normalButton;
	private static JToggleButton highButton;
	private static JToggleButton highestButton;
	private static JPanel animPanel;
	private static JToggleButton memory;
	private static boolean timePopupped = false;
	
	private BufferedImage[] images;
	private ImageIcon stopIcon;
	private ImageIcon normalIcon;
	private ImageIcon fastIcon;
	private ImageIcon fastestIcon;
	private int panelWidth;
	private int panelHeight;
	private int locX;
	private int locY;
	
	private static final int buttonHorOffset = 10;
	
	public TimePanel(int x, int y, int width, int height) {
		locX=x;
		locY=y;
		sizePanel(width, height);
		setOpaque(false);
		setLayout(new GridLayout(1, 5, buttonHorOffset, 10));
		
		stopIcon = new ImageIcon(ResLoader.loadImg("pics/t1.gif"));
		normalIcon = new ImageIcon(ResLoader.loadImg("pics/t2.gif"));
		fastIcon = new ImageIcon(ResLoader.loadImg("pics/t3.gif"));
		fastestIcon = new ImageIcon(ResLoader.loadImg("pics/t4.gif"));
		
		stopButton = new JToggleButton(stopIcon);
		normalButton = new JToggleButton(normalIcon);
		highButton = new JToggleButton(fastIcon);
		highestButton = new JToggleButton(fastestIcon);
		animPanel = new JPanel(new BorderLayout()) {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(images[ElectricGrid.getTime().getHour()], 0, 0, 
						animPanel.getWidth(), animPanel.getHeight(), this);
			}
		};
		animPanel.setOpaque(false);
		
		stopButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		boolean selected = stopButton.isSelected();
                if (selected==true) {
                	stopSelected();
                }
                else {
                	stopButton.setSelected(true);
                }
        	}
        });
		
		normalButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		boolean selected = normalButton.isSelected();
                if (selected==true) {
                	normalSelected();
                }
                else {
                	normalButton.setSelected(true);
                }
        	}
        });
		
		highButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		boolean selected = highButton.isSelected();
                if (selected==true) {
                	highSelected();
                }
                else {
                	highButton.setSelected(true);
                }
        	}
        });
		
		highestButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		boolean selected = highestButton.isSelected();
                if (selected==true) {
                	highestSelected();
                }
                else {
                	highestButton.setSelected(true);
                }
        	}
        });
		
		images = new BufferedImage[24];
		for (int i=0; i<24; i++) {
            try {
                images[i] = ImageIO.read(ResLoader.loadStr("pics/earth_gif/"+i+".gif"));
            }
            catch (IOException e) {
            	System.out.println("image not found");
            }
        }
		
		this.add(animPanel);
		this.add(stopButton);
		this.add(normalButton);
		this.add(highButton);
		this.add(highestButton);
		
		stopButton.setSelected(true);
		stopSelected();
	}
	
	public static void autoPause() {	//pretty dumb with these ifs, popup window pauses
		// and I want to resume to the previous button so I save the status
		if (stopButton.isSelected() == true) {
			memory = stopButton;
		}
		else if (normalButton.isSelected() == true) {
			memory = normalButton;
		}
		else if (highButton.isSelected() == true) {
			memory = highButton;
		}
		else if (highestButton.isSelected() == true) {
			memory = highestButton;
		}
		stopSelected();
	}
	public static void autoResume() {
		if (memory == normalButton) {
			normalSelected();
		}
		else if (memory == highButton) {
			highSelected();
		}
		else if (memory == highestButton) {
			highestSelected();
		}
	}
	
	public static void stopSelected() {
		stopButton.setSelected(true);
		normalButton.setSelected(false);
		highButton.setSelected(false);
		highestButton.setSelected(false);
		ElectricGrid.pauseTime(true);
	}
	
	private static void normalSelected() {
		stopButton.setSelected(false);
		normalButton.setSelected(true);
		highButton.setSelected(false);
		highestButton.setSelected(false);
		ElectricGrid.pauseTime(false);
		ElectricGrid.changeTimeStep(1000);
		popup();
	}
	
	private static void highSelected() {
		stopButton.setSelected(false);
		normalButton.setSelected(false);
		highButton.setSelected(true);
		highestButton.setSelected(false);
		ElectricGrid.pauseTime(false);
		ElectricGrid.changeTimeStep(300);
		popup();
	}

	private static void highestSelected() {
		stopButton.setSelected(false);
		normalButton.setSelected(false);
		highButton.setSelected(false);
		highestButton.setSelected(true);
		ElectricGrid.pauseTime(false);
		ElectricGrid.changeTimeStep(100);
		popup();
	}
	
	private static void popup() {
		if (timePopupped == false) {
			timePopupped = true;
			Popups.timePopup(50);
		}
	}
	
	public static void refreshAnim() {
		if (animPanel != null) {
			animPanel.revalidate();
			animPanel.repaint();
		}
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
