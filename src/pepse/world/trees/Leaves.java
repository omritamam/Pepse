package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.function.BiPredicate;

public class Leaves {
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final String TAG = "leaf";

    private final float baseLocX;
    private final float baseLocY;
    private final BiPredicate<Integer, Integer> densityFunc;
    private final RectangleRenderable renderable;
    private final int layer;
    private final GameObjectCollection gameObjects;
    private final int numOfSlots;
    private int[][] leaves;

    public Leaves(GameObjectCollection gameObjects, Vector2 treeTop, int layer, int numOfSlots,
                  BiPredicate<Integer, Integer> density){
        this.gameObjects = gameObjects;
        this.baseLocX = treeTop.x() - (numOfSlots / 2);
        this.baseLocY = treeTop.y() - (numOfSlots / 2);
        this.layer = layer;
        this.numOfSlots = numOfSlots;
        this.leaves = new int[this.numOfSlots][this.numOfSlots];
        this.densityFunc = density;
        this.renderable = new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
    }

    public void createLeaves(){
        for (int i = 0; i < this.numOfSlots; i++) {
            for (int j = 0; j < this.numOfSlots; j++) {
                if (this.densityFunc.test(i, j)){
                    addLeaf(i, j);
                }
            }
        }
    }

    private void addLeaf(int i, int j) {
        float x = this.baseLocX + (float) i;
        float y = this.baseLocY + (float) j;
        GameObject block = new Block(new Vector2(x, y), this.renderable);
        block.setTag(TAG);
        this.gameObjects.addGameObject(block, this.layer);
    }
}
