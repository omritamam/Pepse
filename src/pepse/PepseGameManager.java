package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.daynight.Night;

public class PepseGameManager extends GameManager {
    public static int SKY_LAYER = -200;
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Sky.create(gameObjects(),windowDimensions, SKY_LAYER);
        Terrain terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS,windowDimensions,0);
        terrain.createInRange(0,1000);
        Night.create(gameObjects(),Layer.FOREGROUND,windowDimensions,30);
    }
}
