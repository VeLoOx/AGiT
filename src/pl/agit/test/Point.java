package pl.agit.test;

import java.util.HashMap;
import java.util.Map;

public class Point {

	private int x;
	private int y;
	
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public static void main(String[] args){
		Map<Integer, String> mapa = new HashMap<>();
		mapa.put(1, "ala");
		
		System.out.println(mapa.get(1));
		System.out.println(mapa.get(1));
		mapa.put(1, "aaaaa");
		System.out.println(mapa.get(1));
	}
	
	
	
}
