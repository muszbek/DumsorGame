package dumsorBase;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Substation extends Node {
	
	private final int producedBase = 800;

	public Substation(String inName, int x, int y) {
		super(inName, x, y);
		icon = "pics/substation.gif";

		try {
			super.iconImage = ImageIO.read(ResLoader.loadStr(icon));
		} catch (IOException e) {
			System.out.println("icon image not found");
		}
		
		super.consumedPower = 0;
	}
	
	@Override
	public void produce() {
		producedPower = (producedBase * ElectricGrid.getTime().getRain());
	}

}
