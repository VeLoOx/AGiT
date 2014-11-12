package pl.agit.Game.Sprites;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;

public abstract class Sprite {
	public List animations = new ArrayList();
	 
    //aktualnie wyswietlany wezel
    public Node node;
 
    //wektor predkosci x/
    public double vX = 0;
 
    //wektor predkosci y
    public double vY = 0;
 
   // martyw lub nie
    public boolean isDead = false;
 
    //metoda uaktualniajaca stan sprita (predkosc/status itp)
    public abstract void update();
 
    // czy koliduje z innymi
    public boolean collide(Sprite other) {
        return false;
    }
}
