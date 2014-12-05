package pl.agit.Game.Sound;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.media.AudioClip;

public class SoundManager {
	    ExecutorService soundPool = Executors.newFixedThreadPool(2);
	    Map<String, AudioClip> soundEffectsMap = new HashMap<>();
	    private static SoundManager instance;

	    
//	    public SoundManager(int numberOfThreads) {
//	        soundPool = Executors.newFixedThreadPool(numberOfThreads);
//	    }
	    
	    private SoundManager(int numberOfThreads){
	    	 soundPool = Executors.newFixedThreadPool(numberOfThreads);
	    }
	    
	    public static SoundManager getSoundManager(int numberOfThreads){
	    	if (instance == null) {
                instance = new SoundManager(numberOfThreads);
            }
	    	return instance;
	    }

	   
	    public void loadSoundEffects(String id,String path) {
	    	
	    	String dirPath = new File("").getAbsolutePath(); //znalezienie sciaki bezwzglednej do projektu
	    		    		    
	    	URL u=null;
			try {
				u = new File(dirPath+path).toURI().toURL();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
	    	
	        AudioClip sound = new AudioClip(u.toExternalForm());
	        soundEffectsMap.put(id, sound);
	    }

	   
	    public void playSound(final String id) {
	        Runnable soundPlay = new Runnable() {
	            @Override
	            public void run() {
	                soundEffectsMap.get(id).play();
	            }
	        };
	        soundPool.execute(soundPlay);
	    }

	   
	    public void shutdown() {
	        soundPool.shutdown();
	    }

	}