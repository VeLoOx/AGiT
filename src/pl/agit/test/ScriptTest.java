package pl.agit.test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.script.ScriptException;

import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.Scripts.ScriptManager;

public class ScriptTest {

	public static void main(String[] args){
		ScriptManager sm = ScriptManager.getScriptManager();
		
		try {
			sm.addScript(GameConst.JS_ALIEN_MAP_NAME,
					GameConst.JS_ALIEN_MAP);
			sm.addScript(GameConst.JS_ASTEROID_DEMOLITION_NAME,
					GameConst.JS_ASTEROID_DEMOLITION);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList< byte[][]> listamapa = new ArrayList<>();
		Object[] par = {0};
		try {
			listamapa = (ArrayList<byte[][]>) sm.getScript(GameConst.JS_ASTEROID_DEMOLITION_NAME).invokeFunction("returnAlienMapList",par);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator<byte[][]> it = listamapa.iterator();
		while(it.hasNext()){
		byte[][] mapa = it.next();	
		for(int i = 0;i<mapa.length;i++){
			for(int x = 0;x<mapa[i].length;x++){
				System.out.print(mapa[i][x]+" ");
			}
			System.out.println();
		}
		System.out.println();
		}
	}
}
