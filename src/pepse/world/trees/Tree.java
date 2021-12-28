package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.function.Function;

public class Tree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final double TREE_CHANCE = 0.1;
    private static final double EXTRA_BLOCK_CHANCE = 0.1;
    private static final int BASE_TRUNK = 10;
    private static final String TRUNK_TAG = "trunk";
    private final RectangleRenderable renderable;
    private final GameObjectCollection gameObjects;
    private final int layer;

    private Function<Float, Float> groundHeightAt;

    public Tree(GameObjectCollection gameObjects, int layer, Function<Float, Float> groundHeightAt) {
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.groundHeightAt = groundHeightAt;
        this.renderable = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
    }

    public void createInRange(int minX, int maxX){
        minX = (int) (Math.floor(minX / Block.SIZE) * Block.SIZE);
        for(int curX  = minX; curX<maxX; curX +=Block.SIZE){
            if (Math.random() < TREE_CHANCE){
                plant(curX);
            }
        }
    }

    private void plant(float x){
        float y = this.groundHeightAt.apply(x);
        for (int i = 0; i < BASE_TRUNK; i++) {
            addTrunkBlock(x, y - i * Block.SIZE);
        }
        int ind = BASE_TRUNK;
        double randFactor = Math.random();
        while (randFactor < EXTRA_BLOCK_CHANCE){
            addTrunkBlock(x, y - ind * Block.SIZE);
            ind++;
            randFactor = Math.random();
        }
        addLeaves(ind);
    }

    private void addTrunkBlock(float x, float y){
        GameObject block = new Block(new Vector2(x, y), this.renderable);
        block.setTag(TRUNK_TAG);
        this.gameObjects.addGameObject(block, this.layer);
    }

    private void addLeaves(int ind) {
        // TODO
    }
}
