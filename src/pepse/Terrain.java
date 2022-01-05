package pepse;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import pepse.world.Block;

import java.awt.*;

public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final float CROOKEDNESS_FACTOR = 3.0F;
    private static final String TAG = "ground";

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final int seed;
    public final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        this.groundHeightAtX0 =  2 * windowDimensions.y()/3;
        this.noiseGenerator = new NoiseGenerator(this.seed);
    }

    public float groundHeightAt(float x) {
        return (float) (noiseGenerator.noise(x)*Block.SIZE*CROOKEDNESS_FACTOR+groundHeightAtX0);
    }

    public void createInRange(int minX, int maxX) {
        minX = (int) (Math.floor(minX / Block.SIZE) * Block.SIZE);
        for(int curX  = minX; curX<maxX; curX +=Block.SIZE){
            createColumn(curX);
        }
    }

    private void createColumn(float x) {
        float minY = (float) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE);
        float maxY = windowDimensions.y()*1.5f;
        int depth_i = 0;
        int layer = this.groundLayer;
        // TODO what abput TERRAIN_DEPTH?
        for(float curY = minY; curY < maxY; curY+=Block.SIZE){
            GameObject block = new Block(new Vector2(x,curY)
                    ,new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
            block.setTag(TAG);
            if (depth_i == 2){
                layer += 1;
            }
            gameObjects.addGameObject(block, layer);
            layer++;
        }
    }



}
