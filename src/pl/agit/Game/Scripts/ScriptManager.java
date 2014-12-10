package pl.agit.Game.Scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import pl.agit.Game.Gamedef.GameConst;

public class ScriptManager {

	private Map <String, Invocable> scriptInvokeMap;
	private ScriptEngine engine;
	
	private static ScriptManager sm=null;
	
	private ScriptManager(){
		
		scriptInvokeMap = new HashMap();
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		
		
		try {
			this.addScript(GameConst.JS_ASTEROID_NAME,GameConst.JS_ASTEROID);
			this.addScript(GameConst.JS_ALIENSHIP_NAME, GameConst.JS_ALIENSHIP);
			this.addScript(GameConst.JS_ASTEROID_DEMOLITION_NAME,
					GameConst.JS_ASTEROID_DEMOLITION);
			this.addScript(GameConst.JS_ALIEN_MAP_NAME, GameConst.JS_ALIEN_MAP);			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static ScriptManager getScriptManager(){
		if(sm==null) return new ScriptManager(); else return sm;
	}
	
	public void addScript(String name, String path) throws ScriptException, FileNotFoundException{
		if(scriptInvokeMap.containsKey(name)) return;
		
		String filePath = new File("").getAbsolutePath(); //znalezienie sciaki bezwzglednej do projektu
		engine.eval((new FileReader(filePath+path)));
		Invocable inv = (Invocable)engine;
		scriptInvokeMap.put(name, inv);
		
	}
	
	public Invocable getScript(String name){
		
		return scriptInvokeMap.get(name);
		
	}
	
//	public static void main(String[] args){
//		ScriptManager sm = ScriptManager.getScriptManager();
//		try {
//			sm.addScript("s1", "/GameScripts/test1.js");
//		} catch (ScriptException | FileNotFoundException e) {
//			System.out.println("blad skryptu");
//			e.printStackTrace();
//		}
//		
//		String m = "ALA";
//		
//		try {
//			sm.getScript("s1").invokeFunction("fun2", m);
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ScriptException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
}
