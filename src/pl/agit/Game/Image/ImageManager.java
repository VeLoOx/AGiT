package pl.agit.Game.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import pl.agit.Game.Gamedef.GameConst;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageManager implements GameConst {
	Map<String, Image> imagesMap = new HashMap<>();
	private static ImageManager instance;

	private ImageManager() {
		this.loadImage(GFX_ASTEROID_NAME,GFX_ASTEROID);
	}

	public static ImageManager getImageManager() {
		if (instance == null) {
			instance = new ImageManager();
		}
		return instance;
	}
	
	
	
	private URL toURL(String path){
		String dirPath = new File("").getAbsolutePath();

		URL u = null;
		try {
			u = new File(dirPath + path).toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return u;
	}

	public void loadImage(String id, String path) {

		if (imagesMap.containsKey(id))
			return;

		URL tmpu = toURL(path);
		if(tmpu==null) return;
		
		Image image = new Image(tmpu.toExternalForm());
		// ImageView imageView = new ImageView(image);
		imagesMap.put(id, image);
	}
	
	public void loadImage(String id, String path, double sizex, double sizey) {

		if (imagesMap.containsKey(id))
			return;

		URL tmpu = toURL(path);
		if(tmpu==null) return;
		
		Image image = new Image(tmpu.toExternalForm(),sizex,sizey,false,false);
		// ImageView imageView = new ImageView(image);
		imagesMap.put(id, image);
	}

	public Image getImage(String id) {

		return imagesMap.get(id);

	}
}