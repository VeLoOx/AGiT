package pl.agit.test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MyTest {

	public static void main(String[] args) throws ScriptException, FileNotFoundException, NoSuchMethodException{
		
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader("D:/Studia/przedmioty2_2/AGiT/proj/JSProject/test.js"));
		
		Invocable invocable = (Invocable) engine;
	
		//String[] par = {"Peter"};
		//Object result = invocable.invokeFunction("fun1", par);
		//System.out.println(result);
		Point p = new Point();
		p.setX(10);
		
		System.out.println(p.getX());
		Object[] o = new Object[1];
		o[0] = p;
		
		Object result = invocable.invokeFunction("fun3", o);
		System.out.println(p.getX());
		System.out.println(((Point) result).getX());
	
	}
}
