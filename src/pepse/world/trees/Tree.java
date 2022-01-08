package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Tree {

    private static final double TREE_CHANCE = 0.1;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final int seed;
    private TreeMap<Integer, SingleTree> trees = new TreeMap<>();
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
                SingleTree singleTree = new SingleTree(new Vector2(curX, (float) (Math.floor(this.groundHeightAt.apply((float) curX) / Block.SIZE) * Block.SIZE)), this.gameObjects, this.layer, this.seed);
                this.trees.put(curX, singleTree);
            }
        }
    }

    public void deleteOutOfRange(int minX, int maxX){
        Integer minOver = this.trees.higherKey(maxX);
        Integer maxUnder = this.trees.lowerKey(minX);
        if (minOver != null){
            var tail = this.trees.tailMap(minOver).keySet();
            for (int treeX : tail) {
                SingleTree singleTree = this.trees.get(treeX);
                singleTree.removeTree();
                this.trees.remove(treeX);
            }
            tail.clear();
        }
        if (maxUnder != null){
            var head = this.trees.headMap(maxUnder).keySet();
            for (int treeX : head) {
                SingleTree singleTree = this.trees.get(treeX);
                singleTree.removeTree();
                this.trees.remove(treeX);
            }
            head.clear();
        }
    }

}
