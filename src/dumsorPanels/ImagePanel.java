package dumsorPanels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import dumsorBase.*;

public class ImagePanel extends JPanel implements ResizeablePanel {
	
	private int panelWidth;
	private int panelHeight;
	private int locX;
	private int locY;
	
	private static BufferedImage[] nodeImages;
	private static BufferedImage overViewImage;
	private static BufferedImage constImage;
	private static BufferedImage loadSheddingImage;
	private static BufferedImage currentImage;	//copy the needed image into this, this is displayed
	
	public ImagePanel(int x, int y, int width, int height) {
		locX=x;
		locY=y;
		sizePanel(width, height);
		
		//opening images
		ArrayList<Node> localNodes = ElectricGrid.getNodes();
		nodeImages = new BufferedImage[localNodes.size()];
		for (Node myNode : localNodes) {
            try {
            	//System.out.println(myNode.getName());
                nodeImages[localNodes.indexOf(myNode)] = ImageIO.read(ResLoader.loadStr(
                		"pics/node_images/" + myNode.getName() + ".jpg"));
            }
            catch (IOException e) {
            	System.out.println("image not found");
            }
        }
		
		try {
			overViewImage = ImageIO.read(ResLoader.loadStr("pics/overview.jpg"));
			constImage = ImageIO.read(ResLoader.loadStr("pics/construction.jpg"));
			loadSheddingImage = ImageIO.read(ResLoader.loadStr("pics/loadshedding.jpg"));
		}
		catch (IOException e) {
        	System.out.println("image not found");
        }
		
		currentImage = overViewImage;
	}
	
	public static void updateImage(Node selected) {
		if (selected != null) {
			currentImage = nodeImages[ElectricGrid.getNodes().indexOf(selected)];
		}
		else {
			if (DumsorRoot.getMode() == DumsorRoot.GameMode.OVERVIEW) {
				currentImage = overViewImage;
			}
			else if (DumsorRoot.getMode() == DumsorRoot.GameMode.CONSTRUCTION) {
				currentImage = constImage;
			}
			else if (DumsorRoot.getMode() == DumsorRoot.GameMode.LOAD_SHEDDING) {
				currentImage = loadSheddingImage;
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(currentImage, 0, 0, this.getWidth(), this.getHeight(), this);
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
//folders in resource not found externally