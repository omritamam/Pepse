package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Tree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final double TREE_CHANCE = 0.1;
    private static final double EXTRA_BLOCK_CHANCE = 0.1;
    private static final int BASE_TRUNK = 10;
    private static final String TAG = "trunk";
    private static final int LEAF_SLOTS = 9;

    private final GameObjectCollection gameObjects;
    private final int layer;
    private final int seed;

    private Function<Float, Float> groundHeightAt;

    public Tree(GameObjectCollection gameObjects, int layer, Function<Float, Float> groundHeightAt, int seed) {
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.groundHeightAt = groundHeightAt;
        this.seed = seed;
    }

    public void createInRange(int minX, int maxX){
        minX = (int) (Math.floor(minX / Block.SIZE) * Block.SIZE);
        for(int curX  = minX; curX<maxX; curX +=Block.SIZE){
            if (new Random(Objects.hash(curX, this.seed)).nextDouble() < TREE_CHANCE){
                plant(curX);
            }
        }
    }

    private void plant(float x){
        // TODO check groundheightat
        float y = this.groundHeightAt.apply(x);
        for (int i = 0; i < BASE_TRUNK; i++) {
            addTrunkBlock(x, y - (i + 1) * Block.SIZE);
        }
        int ind = BASE_TRUNK;
        Random randFactor = new Random(Objects.hash(x, this.seed));
        while (randFactor.nextDouble() < EXTRA_BLOCK_CHANCE){
            addTrunkBlock(x, y - ind * Block.SIZE);
            ind++;
        }
        addTreeTop(new Vector2(x, y - ind * Block.SIZE));
    }

    private void addTrunkBlock(float x, float y){
        GameObject block = new Block(new Vector2(x, y),
                new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR)));
        block.setTag(TAG);
        this.gameObjects.addGameObject(block, this.layer);
    }

    private void addTreeTop(Vector2 loc) {
//        BiPredicate<Integer, Integer> leavesDensity = (i, j)->
//                ((Math.abs(LEAF_SLOTS - i) < LEAF_SLOTS / 2) && (Math.abs(LEAF_SLOTS - j) < LEAF_SLOTS / 2));   // TODO set density
        BiPredicate<Integer, Integer> leavesDensity = (i, j)->(true);
        TreeTop treeTop = new TreeTop(this.gameObjects, loc, this.layer + 1, LEAF_SLOTS,
                leavesDensity, this.seed);
        treeTop.createLeaves();
    }
}
