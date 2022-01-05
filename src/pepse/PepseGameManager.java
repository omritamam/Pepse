package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.*;

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
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int DAYNIGHT_CYCLE = 30;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);

    /**
     * TODO
     * avatar sound
     * choose power screen
     */

    public static EnumMap<Layers, Integer> layers = new EnumMap<Layers, Integer>(Layers.class);
    private int curMinX;
    private int curMaxX;
    private double seed;
    private WindowController windowController;
    private int windowWidth;
    private Terrain terrain;
    private Tree treeManager;


    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.windowController = windowController;
        super.initializeGame(imageReader,
                soundReader,
                inputListener,
                windowController);
        defineLayers();
        Vector2 windowDimensions = windowController.getWindowDimensions();
        windowController.setTargetFramerate(FRAME_RATE);
        this.seed = new Random().nextGaussian() * 255;
        this.curMinX = (int) -(0.5f * windowDimensions.x());
        this.curMaxX =  (int) (1.5f * windowDimensions.x());
        this.windowWidth =  (int)  windowDimensions.x();
        // create sky
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        // create terrain
        terrain = new Terrain(gameObjects(), GROUND_LAYER, windowDimensions, (int) seed);
        terrain.createInRange(curMinX, curMaxX);
        // create day-night cycle
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, DAYNIGHT_CYCLE);
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, DAYNIGHT_CYCLE);
        GameObject sunHalo = SunHalo.create(gameObjects(), HALO_LAYER, sun, HALO_COLOR);
        sunHalo.addComponent((d) -> sunHalo.setCenter(sun.getCenter()));
        // create trees
        treeManager = new Tree(gameObjects(), TREE_LAYER, terrain::groundHeightAt, (int) this.seed);
        treeManager.createInRange(curMinX, curMaxX);
        float avatarInitalLocationX = windowDimensions.y()*0.5f;
        Vector2 avaterLocation = new Vector2(avatarInitalLocationX, terrain.groundHeightAt(avatarInitalLocationX) - 2 * Avatar.DIEMNSIONS.y());
        Avatar avatar = Avatar.create(gameObjects(), AVATAR_LAYER, avaterLocation, inputListener, imageReader);
        setCamera(new Camera(avatar, windowController.getWindowDimensions().mult(0.5f).subtract(avaterLocation), windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TREE_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, GROUND_LAYER, true);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int cameraLocationX = (int) camera().getCenter().x();
        if(cameraLocationX - windowWidth < curMinX){
            terrain.createInRange(cameraLocationX - windowWidth, curMinX);
            treeManager.createInRange(cameraLocationX - windowWidth, curMinX);
            curMinX = cameraLocationX - windowWidth;
        } if(cameraLocationX + windowWidth > curMaxX){
            terrain.createInRange(curMaxX, cameraLocationX + windowWidth );
            treeManager.createInRange(curMaxX, cameraLocationX + windowWidth );
            curMaxX = cameraLocationX + windowWidth;
        }
        for(GameObject gameObject : gameObjects()){
            float locationX = gameObject.getTopLeftCorner().x();
            if(locationX< curMinX || locationX > curMaxX){
                gameObjects().removeGameObject(gameObject, GROUND_LAYER);
            }
        }
        this.treeManager.deleteOutOfRange(curMinX, curMaxX);
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
        this.layers.put(Layers.AVATAR, AVATAR_LAYER);
    }

}
