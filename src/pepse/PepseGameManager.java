package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;

public class PepseGameManager extends GameManager {
    private static final int FRAME_RATE = 60;
    private static final int SKY_LAYER = -200;
    private static final int DAYNIGHT_CYCLE = 30;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

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
        Night.create(gameObjects(),Layer.FOREGROUND,windowDimensions,DAYNIGHT_CYCLE);
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND + 1, windowDimensions,
                DAYNIGHT_CYCLE);
        GameObject sunHalo = SunHalo.create(gameObjects(), Layer.BACKGROUND + 2, sun, HALO_COLOR);
        sunHalo.addComponent((d)->sunHalo.setCenter(sun.getCenter()));
        Tree treeManager = new Tree(terrain::groundHeightAt);
    }
}
