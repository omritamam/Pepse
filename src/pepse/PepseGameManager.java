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
import java.util.EnumMap;
import java.util.Random;

public class PepseGameManager extends GameManager {
    private static final int FRAME_RATE = 60;
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = SKY_LAYER + 1;
    private static final int HALO_LAYER = SUN_LAYER + 1;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int TREE_LAYER = GROUND_LAYER + 10;
    private static final int LEAVES_LAYER = TREE_LAYER + 1;
    private static final int FALL_LAYER = LEAVES_LAYER + 1;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int DAYNIGHT_CYCLE = 30;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    public static EnumMap<Layers, Integer> layers = new EnumMap<Layers, Integer>(Layers.class);
    private double seed;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        defineLayers();
        Vector2 windowDimensions = windowController.getWindowDimensions();
        windowController.setTargetFramerate(FRAME_RATE);
        this.seed = new Random().nextGaussian() * 255;

        // create sky
        Sky.create(gameObjects(),windowDimensions, SKY_LAYER);
        // create terrain
        Terrain terrain = new Terrain(gameObjects(), GROUND_LAYER,windowDimensions,(int) seed);
//        int cameraDimensions = (int) super.camera().getDimensions().x();
//        terrain.createInRange(cameraDimensions, cameraDimensions + (int) windowDimensions.x());
        // TODO check range numbers
        terrain.createInRange(0, 1500);
        // create day-night cycle
        Night.create(gameObjects(),NIGHT_LAYER,windowDimensions,DAYNIGHT_CYCLE);
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions,
                DAYNIGHT_CYCLE);
        GameObject sunHalo = SunHalo.create(gameObjects(), HALO_LAYER, sun, HALO_COLOR);
        sunHalo.addComponent((d)->sunHalo.setCenter(sun.getCenter()));
        // create trees
        Tree treeManager = new Tree(gameObjects(), TREE_LAYER, terrain::groundHeightAt, (int) this.seed);
        // TODO check range numbers
        treeManager.createInRange(0, 1500);
    }

    private void defineLayers(){
        this.layers.put(Layers.SKY, SKY_LAYER);
        this.layers.put(Layers.SUN, SUN_LAYER);
        this.layers.put(Layers.HALO, HALO_LAYER);
        this.layers.put(Layers.GROUND, GROUND_LAYER);
        this.layers.put(Layers.TREE, TREE_LAYER);
        this.layers.put(Layers.LEAVES, LEAVES_LAYER);
        this.layers.put(Layers.FALL, FALL_LAYER);
        this.layers.put(Layers.NIGHT, NIGHT_LAYER);
    }
}
