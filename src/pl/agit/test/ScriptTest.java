package pl.agit.test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.stage.Stage;

import javax.script.ScriptException;

import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.Scripts.ScriptManager;
import pl.agit.Game.Sprites.Characters.Asteroid;
import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GUIElements.MainMenu;

public class ScriptTest {

	public static void main(String[] args){
		ScriptManager sm = ScriptManager.getScriptManager();
		
		try {
			sm.addScript(GameConst.JS_ASTEROID_NAME,GameConst.JS_ASTEROID);
						
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Asteroid a = new Asteroid(12, "red");
		
		Object[] o = {a,12,12,3,15};
		
		try {
			sm.getScript(GameConst.JS_ASTEROID_NAME).invokeFunction("generateShatter", o);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
