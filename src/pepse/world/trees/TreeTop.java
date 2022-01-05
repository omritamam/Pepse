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
import java.util.HashSet;
import java.util.function.BiPredicate;

public class TreeTop {
    private final float baseLocX;
    private final float baseLocY;
    private final BiPredicate<Integer, Integer> densityFunc;
    private final int layer;
    private final GameObjectCollection gameObjects;
    private final int numOfSlots;
    private final int seed;
    private final Color baseColor;
    private HashSet<Leaf> leaves = new HashSet<>();

    public TreeTop(GameObjectCollection gameObjects, Vector2 treeTop, int layer, int numOfSlots,
                   BiPredicate<Integer, Integer> density, Color baseColor, int seed){
        this.gameObjects = gameObjects;
        this.baseLocX = treeTop.x() - ((((float) numOfSlots / 2f) - 0.5f) * Block.SIZE);
        this.baseLocY = treeTop.y() - ((numOfSlots - 1) * Block.SIZE);
        this.layer = layer;
        this.numOfSlots = numOfSlots;
        this.densityFunc = density;
        this.baseColor = baseColor;
        this.seed = seed;
        createLeaves();
    }

    private void createLeaves(){
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
                new RectangleRenderable(ColorSupplier.approximateColor(this.baseColor)), this.seed, this.layer,
                this.gameObjects, ()->{createLeaf(i, j);});
        leaves.add(leaf);
        this.gameObjects.addGameObject(leaf, this.layer);
    }

    public void removeLeaves(){
        for (Leaf leaf : this.leaves) {
            this.gameObjects.removeGameObject(leaf, layer);
            this.gameObjects.removeGameObject(leaf, layer + 1);
        }
    }

}
