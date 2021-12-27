package pepse;

import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.daynight.Night;

public class PepseGameManager extends GameManager {
    private static final int FRAME_RATE = 80;
    public static int SKY_LAYER = -200;
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        windowController.setTargetFramerate(FRAME_RATE);
        Sky.create(gameObjects(),windowDimensions, SKY_LAYER);
        Terrain terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS,windowDimensions,0);
//        int cameraDimensions = (int) super.camera().getDimensions().x();
//        terrain.createInRange(cameraDimensions, cameraDimensions + (int) windowDimensions.x());
        // TODO why these numbers?
        terrain.createInRange(0, 1500);
        Night.create(gameObjects(),Layer.FOREGROUND,windowDimensions,30);
    }
}
