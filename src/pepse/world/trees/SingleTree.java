package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class SingleTree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final double EXTRA_BLOCK_CHANCE = 0.5;
    private static final String TAG = "trunk";
    private static final int BASE_TRUNK = 10;
    private static final int LEAF_SLOTS = 7;

    private final GameObjectCollection gameObjects;
    private final int layer;
    private final int seed;
    private final Vector2 baseLoc;
    private final LeafTraitsFactory factory;
    private int height;
    private HashSet<Block> trunk = new HashSet<>();
    private TreeTop treeTop;

    public SingleTree(Vector2 baseLoc, GameObjectCollection gameObjects, int layer, int seed){
        this.baseLoc = baseLoc;
        this.height = 0;
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.seed = seed;
        this.factory = new LeafTraitsFactory(LEAF_SLOTS, this.seed);
        plant();
    }

    private void plant(){
        float x = this.baseLoc.x();
        float y = this.baseLoc.y();
        for (int i = 0; i < BASE_TRUNK; i++) {
            addTrunkBlock(x, y - (i + 1) * Block.SIZE);
        }
        int height = BASE_TRUNK;
        Random randFactor = new Random(Objects.hash(x, this.seed));
        while (randFactor.nextDouble() < EXTRA_BLOCK_CHANCE){
            addTrunkBlock(x, y - height * Block.SIZE);
            height++;
        }
        addTreeTop(new Vector2(x - 0.5f * Block.SIZE, y - height * Block.SIZE));
    }

    private void addTrunkBlock(float x, float y){
        Block block = new Block(new Vector2(x, y),
                new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR)));
        block.setTag(TAG);
        this.trunk.add(block);
        this.gameObjects.addGameObject(block, this.layer);
        this.height++;
    }

    private void addTreeTop(Vector2 loc) {
        this.treeTop = new TreeTop(this.gameObjects, loc, this.layer + 1, LEAF_SLOTS,
                this.factory.getDensity(loc.x()), this.factory.getColor(loc.x()), this.seed);
    }

    public void removeTree(){
        this.treeTop.removeLeaves();
        for (Block block : this.trunk) {
            this.gameObjects.removeGameObject(block, this.layer);
        }
        this.trunk.clear();
    }
}
