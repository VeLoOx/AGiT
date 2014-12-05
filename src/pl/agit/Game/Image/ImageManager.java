package pl.agit.Game.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageManager {
	Map<String, ImageView> imagesMap = new HashMap<>();
	private static ImageManager instance;

	private ImageManager() {

	}

	public static ImageManager getImageManager() {
		if (instance == null) {
			instance = new ImageManager();
		}
		return instance;
	}

	public void loadImage(String id, String path) {

		String dirPath = new File("").getAbsolutePath(); // znalezienie sciaki
															// bezwzglednej do
															// projektu

		URL u = null;
		try {
			u = new File(dirPath + path).toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Image image = new Image(u.toExternalForm());
		ImageView imageView = new ImageView(image);
		imagesMap.put(id, imageView);
	}
	
	public ImageView getImage(String id){
		
		return imagesMap.get(id);
		
	}
}