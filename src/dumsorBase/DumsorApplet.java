package dumsorBase;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

public class DumsorApplet extends JApplet {

	@Override
	public void init() {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					//new DumsorMain().setVisible(true);
					setContentPane(new DumsorRoot());
					Popups.startPopup(70);
				}
			});
		}
		catch(Exception e) {
			System.err.println("GUI did not load successfully");
		}
	}
}
