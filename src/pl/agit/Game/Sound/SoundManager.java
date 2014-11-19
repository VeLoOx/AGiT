package pl.agit.Game.Sound;
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

	    /**
	     * Constructor to create a simple thread pool.
	     *
	     * @param numberOfThreads - number of threads to use media players in the map.
	     */
	    public SoundManager(int numberOfThreads) {
	        soundPool = Executors.newFixedThreadPool(numberOfThreads);
	    }
	    
	    private SoundManager(){
	    	
	    }
	    
	    public static SoundManager getSoundManager(int numberOfThreads){
	    	if (instance == null) {
                instance = new SoundManager(numberOfThreads);
            }
	    	return instance;
	    }

	    /**
	     * Load a sound into a map to later be played based on the id.
	     *
	     * @param id  - The identifier for a sound.
	     * @param url - The url location of the media or audio resource. Usually in src/main/resources directory.
	     */
	    public void loadSoundEffects(String id, URL url) {
	        AudioClip sound = new AudioClip(url.toExternalForm());
	        soundEffectsMap.put(id, sound);
	    }

	    /**
	     * Lookup a name resource to play sound based on the id.
	     *
	     * @param id identifier for a sound to be played.
	     */
	    public void playSound(final String id) {
	        Runnable soundPlay = new Runnable() {
	            @Override
	            public void run() {
	                soundEffectsMap.get(id).play();
	            }
	        };
	        soundPool.execute(soundPlay);
	    }

	    /**
	     * Stop all threads and media players.
	     */
	    public void shutdown() {
	        soundPool.shutdown();
	    }

	}