package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.BiPredicate;

public class TreeTop {
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final String TAG = "leaf";
    private static final Float STILL_ANGLE = -20f;
    private static final Float TURNED_ANGLE = 30f;
    private static final Float TURNCYCLE_LENGTH = 5f;
    private static final Vector2 STILL_DIM = new Vector2(25, 45);
    private static final Vector2 TURNED_DIM = new Vector2(30, 30);
    private static final int FADEOUT_TIME = 20;
    private static final int HORIZONTAL_TIME = 5;
    private static final float LEFT_HORIZONTAL = -10;
    private static final float RIGHT_HORIZONTAL = 10;
    private static final float LEAF_DROP_RATE = 20;
    private static final int UPPER_LIFETIME_BOUND = 60;

    private final float baseLocX;
    private final float baseLocY;
    private final BiPredicate<Integer, Integer> densityFunc;
    private final int layer;
    private final GameObjectCollection gameObjects;
    private final int numOfSlots;
    private final int seed;

    public TreeTop(GameObjectCollection gameObjects, Vector2 treeTop, int layer, int numOfSlots,
                   BiPredicate<Integer, Integer> density, int seed){
        this.gameObjects = gameObjects;
        this.baseLocX = treeTop.x() - ((((float) numOfSlots / 2f) - 0.5f) * Block.SIZE);
        this.baseLocY = treeTop.y() - ((numOfSlots - 1) * Block.SIZE);
        this.layer = layer;
        this.numOfSlots = numOfSlots;
        this.densityFunc = density;
        this.seed = seed;
    }

    public void createLeaves(){
        for (int i = 0; i < this.numOfSlots; i++) {
            for (int j = 0; j < this.numOfSlots; j++) {
                if (this.densityFunc.test(i, j)){
                    createLeaf(i, j);
                }
            }
        }
    }

    private void createLeaf(int i, int j) {
        float x = this.baseLocX + (i + 1) * Block.SIZE;
        float y = this.baseLocY + (j + 1) * Block.SIZE;
        Leaf leaf = new Leaf(new Vector2(x, y), Vector2.ZERO,
                new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)), this.seed, this.layer,
                this.gameObjects, ()->{createLeaf(i, j);});
        this.gameObjects.addGameObject(leaf, this.layer);

    }

}
