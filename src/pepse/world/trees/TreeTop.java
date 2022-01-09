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

    /**
     * constructor
     * @param gameObjects collection of game objects
     * @param loc location of treetop
     * @param layer layer of leaves
     * @param numOfSlots number of leaves per row
     * @param density shape of treetop
     * @param baseColor color of treetop
     * @param seed random factor seed
     */
    public TreeTop(GameObjectCollection gameObjects, Vector2 loc, int layer, int numOfSlots,
                   BiPredicate<Integer, Integer> density, Color baseColor, int seed){
        this.gameObjects = gameObjects;
        this.baseLocX = loc.x() - (((float) numOfSlots / 2f) * Block.SIZE);
        this.baseLocY = loc.y() - ((numOfSlots - 1) * Block.SIZE);
        this.layer = layer;
        this.numOfSlots = numOfSlots;
        this.densityFunc = density;
        this.baseColor = baseColor;
        this.seed = seed;
        createLeaves();
    }

    /**
     * creates the leaves of the treetop
     */
    private void createLeaves(){
        for (int i = 0; i < this.numOfSlots; i++) {
            for (int j = 0; j < this.numOfSlots; j++) {
                if (this.densityFunc.test(i, j)){
                    leaves.add(createLeaf(i, j));
                }
            }
        }
    }

    /**
     * creates a single leaf
     * @param i location to add
     * @param j location to add
     * @return leaf added
     */
    private Leaf createLeaf(int i, int j) {
        float x = this.baseLocX + (i + 1) * Block.SIZE;
        float y = this.baseLocY + (j + 1) * Block.SIZE;
        Leaf leaf = new Leaf(new Vector2(x, y), Vector2.ZERO,
                new RectangleRenderable(ColorSupplier.approximateColor(this.baseColor)), this.seed, this.layer,
                this.gameObjects, ()->{createLeaf(i, j);});
        this.gameObjects.addGameObject(leaf, this.layer);
        return leaf;
    }

    /**
     * deletes all the leaves in treetop
     */
    public void removeLeaves(){
        for (Leaf leaf : this.leaves) {
            this.gameObjects.removeGameObject(leaf, layer);
            this.gameObjects.removeGameObject(leaf, layer + 1);
        }
        this.leaves.clear();
    }

}
