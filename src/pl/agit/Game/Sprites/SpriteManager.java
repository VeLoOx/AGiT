package pl.agit.Game.Sprites;
import java.util.*;

public class SpriteManager {
	//aktualne elemnty znajdujace sie w grze
	private final static List GAME_ACTORS = new ArrayList();
	 
    //lista do sprawdzania kolizji
    private final static List CHECK_COLLISION_LIST = new ArrayList();
 
    //zbior uzywany do czyszczenia i usuwania spritow
    private final static Set CLEAN_UP_SPRITES = new HashSet();
 
    
    public List getAllSprites() {
        return GAME_ACTORS;
    }
 
    //dodwanie spritow do gry
    public void addSprites(Sprite[] sprites) {
        GAME_ACTORS.addAll(Arrays.asList(sprites));
    }
 
    //usuwanie spritow z gry
    public void removeSprites(Sprite[] sprites) {
        GAME_ACTORS.removeAll(Arrays.asList(sprites));
    }
 
    
    public Set getSpritesToBeRemoved() {
        return CLEAN_UP_SPRITES;
    }
 
    //dodawanie spritow do usuniecia
    public void addSpritesToBeRemoved(Sprite[] sprites) {
        if (sprites.length > 1) {
            CLEAN_UP_SPRITES.addAll(Arrays.asList((Sprite[]) sprites));
        } else {
            CLEAN_UP_SPRITES.add(sprites[0]);
        }
    }
 
    //sprawdzanie kolizji
    public List getCollisionsToCheck() {
        return CHECK_COLLISION_LIST;
    }
 
    //resetowanie kolizji
    public void resetCollisionsToCheck() {
        CHECK_COLLISION_LIST.clear();
        CHECK_COLLISION_LIST.addAll(GAME_ACTORS);
    }
 
    //usuwanie i czyszczenie spritow z wszystkich podrecznych kolekcji
    public void cleanupSprites() {
 
        // remove from actors list
        GAME_ACTORS.removeAll(CLEAN_UP_SPRITES);
 
        // reset the clean up sprites
        CLEAN_UP_SPRITES.clear();
    }
}
