package pl.agit.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MyTest {

	public static void main(String[] args) throws ScriptException, FileNotFoundException, NoSuchMethodException{
		
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader("D:/Studia/przedmioty2_2/AGiT/proj/JSProject/test.js"));
		
		Invocable invocable1 = (Invocable) engine;
		
		engine.eval(new FileReader("D:/Studia/przedmioty2_2/AGiT/proj/JSProject/test2.js"));
		Invocable invocable2 = (Invocable) engine;
		
//		final AudioClip sound = new AudioClip(new File("D:\\Studia\\przedmioty2_2\\AGiT\\proj\\JavaProject\\bin\\GameGfxFiles\\m1.mp3").toURI().toString());
//		
//		Runnable soundPlay = new Runnable() {
//            @Override
//            public void run() {
//            	sound.play();
//            	System.out.println("aaa");
//            }
//        };
		
//		

//		String ssound = "D:\\Studia\\przedmioty2_2\\AGiT\\proj\\JavaProject\\bin\\GameGfxFiles\\m1.mp3";
//	    Media sound = new Media(new File("D:\\Studia\\przedmioty2_2\\AGiT\\proj\\JavaProject\\bin\\GameGfxFiles\\m1.mp3").toURI().toString());
//	    MediaPlayer mediaPlayer = new MediaPlayer(sound);
//	    mediaPlayer.play();
		
		System.out.println("aaa");
	
		//String[] par = {"Peter"};
		//Object result = invocable.invokeFunction("fun1", par);
		//System.out.println(result);
//		Point p = new Point();
//		p.setX(10);
//		
//		System.out.println(p.getX());
//		Object[] o = new Object[1];
//		o[0] = p;
//		
		String d = "GRZESIEK";
		invocable1.invokeFunction("fun2", d);
		invocable2.invokeFunction("fun4", d);
		invocable1.invokeFunction("fun2", d);
		//System.out.println(p.getX());
		//System.out.println(((Point) result).getX());
	
	}
}
