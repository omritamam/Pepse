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
import pepse.world.Terrain;
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
    private static final int SURFACE_LAYER = Layer.STATIC_OBJECTS;
    private static final int DEEP_GROUND_LAYER = SURFACE_LAYER + 1;
    private static final int TREE_LAYER = SURFACE_LAYER + 10;
    private static final int LEAVES_LAYER = TREE_LAYER + 1;
    private static final int FALL_LAYER = LEAVES_LAYER + 1;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int DAYNIGHT_CYCLE = 30;
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static final float ENDLESS_WINDOW_OFFSET_FACTOR = 0.2F;

    /**
     * TODO
     * bug: flying jumping
     * bug: double leaves?
     * bug: remove out of bounds
     * avatar sound
     * avatar movement
     * numbers in constants (in avatar)
     * choose power screen
     */

    public static EnumMap<Layers, Integer> layers = new EnumMap<Layers, Integer>(Layers.class);
    private static HashSet<Enum> enviromentLayers = new HashSet<>();
    private int curMinX;
    private int curMaxX;
    private double seed;
    private WindowController windowController;
    private int windowWidth;
    private Terrain terrain;
    private Tree treeManager;
    private Avatar avatar;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
*/
     @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader,soundReader,inputListener,windowController);
        this.windowController = windowController;
        defineLayers();
        Vector2 windowDimensions = windowController.getWindowDimensions();
        windowController.setTargetFramerate(FRAME_RATE);
        this.seed = new Random().nextGaussian() * 255;
        this.windowWidth =  (int)  windowDimensions.x();
        this.curMinX = (int) -(ENDLESS_WINDOW_OFFSET_FACTOR * windowWidth);
        this.curMaxX =  (int) (1+ ENDLESS_WINDOW_OFFSET_FACTOR * windowWidth);
        // create sky
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        // create terrain
        terrain = new Terrain(gameObjects(), SURFACE_LAYER, windowDimensions, (int) seed);
        terrain.createInRange(curMinX, curMaxX);
        // create day-night cycle
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, DAYNIGHT_CYCLE);
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, DAYNIGHT_CYCLE);
        GameObject sunHalo = SunHalo.create(gameObjects(), HALO_LAYER, sun, HALO_COLOR);
        sunHalo.addComponent((d) -> sunHalo.setCenter(sun.getCenter()));
        // create trees
        treeManager = new Tree(gameObjects(), TREE_LAYER, terrain::groundHeightAt, (int) this.seed);
        treeManager.createInRange(curMinX, curMaxX);
        // create avatar
        float avatarInitalLocationX = windowDimensions.x()*0.5f;
        Vector2 avaterLocation = new Vector2(avatarInitalLocationX, terrain.groundHeightAt(avatarInitalLocationX) -
                3 * Avatar.DIEMNSIONS.y());
        Avatar avatar = Avatar.create(gameObjects(), AVATAR_LAYER, avaterLocation, inputListener, imageReader);
        this.avatar = avatar;
        setCamera(new Camera(avatar, windowController.getWindowDimensions().mult(0.5f).subtract(avaterLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        // layers collision
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TREE_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, SURFACE_LAYER, true);
        gameObjects().layers().shouldLayersCollide(LEAVES_LAYER, SURFACE_LAYER, false);
    }

    /**
     * override, run methods to support endless world.
     * @param deltaTime - time between frames
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        createInRange();
        deleteOutOfRange();
    }

    /**
     * the method creates new gameObjects in the sides of the screen, following the camera movements.
     */
    private void createInRange(){
        int cameraLocationX = (int) camera().getCenter().x();
        int newMinX = (int) (cameraLocationX - (0.5F + ENDLESS_WINDOW_OFFSET_FACTOR) * windowWidth);
        int newMaxX = (int) (cameraLocationX + (0.5F + ENDLESS_WINDOW_OFFSET_FACTOR) * windowWidth);
        if(newMinX < curMinX){
            terrain.createInRange(newMinX, curMinX);
            treeManager.createInRange(newMinX, curMinX);
        }
        if(newMaxX > curMaxX){
            terrain.createInRange(curMaxX, newMaxX );
            treeManager.createInRange(curMaxX, newMaxX );
        }
        curMinX = newMinX;
        curMaxX = newMaxX;
    }

    /**
     * removes all gameObjects outside the screen range.
     */
    private void deleteOutOfRange(){
        int counter = 0;
        var gameObjects = gameObjects();
        System.out.print((int) avatar.getTopLeftCorner().x()+" avatar,");
        System.out.print((int) curMinX+" curMinX,");
        System.out.print((int) curMaxX+" curMaxX,");
        for(GameObject gameObject : gameObjects) {
            float locationX = gameObject.getTopLeftCorner().x();
            if(locationX < curMinX || locationX > curMaxX){
                gameObjects.removeGameObject(gameObject, SURFACE_LAYER);
                gameObjects.removeGameObject(gameObject, DEEP_GROUND_LAYER);
                System.out.print((int)locationX);
                System.out.print(gameObject.getTag() +",");
            }
            counter++;
        }
        //tofo: remove counter
        System.out.println("\n "+counter);
        this.treeManager.deleteOutOfRange(curMinX, curMaxX);
    }

    /**
     * todo: are you sure it neccesary?
     */
    private void defineLayers(){
        this.layers.put(Layers.SKY, SKY_LAYER);
        this.layers.put(Layers.SUN, SUN_LAYER);
        this.layers.put(Layers.HALO, HALO_LAYER);
        this.layers.put(Layers.SURFACE, SURFACE_LAYER);
        this.layers.put(Layers.DEEP_GROUND, DEEP_GROUND_LAYER);
        this.layers.put(Layers.TREE, TREE_LAYER);
        this.layers.put(Layers.LEAVES, LEAVES_LAYER);
        this.layers.put(Layers.FALL, FALL_LAYER);
        this.layers.put(Layers.NIGHT, NIGHT_LAYER);
        this.layers.put(Layers.AVATAR, AVATAR_LAYER);
    }

}
