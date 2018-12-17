package dumsorBase;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

public class ResLoader {

	public static URL load(String path) {
		//String newPath = path.replaceAll("pics", "");
		String newPath = "/" + path;
		URL input = ResLoader.class.getResource(newPath);
		
		return input;
	}
	
	public static InputStream loadStr(String path) {
//		String newPath = "/resources/" + path;
//		String newPath = path.replaceAll("pics", "");
//		newPath = path.replaceAll("pics/earth_gif", "");
//		newPath = path.replaceAll("pics/node_images", "");
		InputStream input = ResLoader.class.getClassLoader().getResourceAsStream(path);
		
		return input;
	}
	
	public static BufferedImage loadImg (String path) {
		BufferedImage input = null;
		try {
			input = ImageIO.read(loadStr(path));
		}
		catch (Exception exp) {
			System.err.println("image not found");
		}
		return input;
	}
}
