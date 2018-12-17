package dumsorBase;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import dumsorBase.*;

public class DumsorMain extends JFrame {
	
	private DumsorRoot root;
	
	public DumsorMain() {
		super("Dumsor Dumsor");
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	System.out.println("Window closing");
                System.exit(0);
            }
        });
		
		root = new DumsorRoot();
		
		getContentPane().setPreferredSize(new Dimension(root.getWidth(), root.getHeight()));
		//System.out.println(root.getWidth() + " " + root.getHeight());
		pack();
		
		getContentPane().add(root);		
	}
	
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new DumsorMain().setVisible(true);
					
					Popups.startPopup(70);
				}
			});
		}
		catch(Exception e) {
			System.err.println("GUI did not load successfully");
		}
	}

}