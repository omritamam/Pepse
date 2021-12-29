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
import java.util.function.BiPredicate;

public class Leaves {
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final String TAG = "leaf";
    private static final Float STILL_ANGLE = -20f;
    private static final Float TURNED_ANGLE = 30f;
    private static final Float TURNCYCLE_LENGTH = 5f;
    private static final Vector2 STILL_DIM = new Vector2(25, 45);
    private static final Vector2 TURNED_DIM = new Vector2(30, 30);

    private final float baseLocX;
    private final float baseLocY;
    private final BiPredicate<Integer, Integer> densityFunc;
    private final int layer;
    private final GameObjectCollection gameObjects;
    private final int numOfSlots;

    public Leaves(GameObjectCollection gameObjects, Vector2 treeTop, int layer, int numOfSlots,
                  BiPredicate<Integer, Integer> density){
        this.gameObjects = gameObjects;
        this.baseLocX = treeTop.x() - ((((float) numOfSlots / 2f) - 0.5f) * Block.SIZE);
        this.baseLocY = treeTop.y() - ((numOfSlots - 1) * Block.SIZE);
        this.layer = layer;
        this.numOfSlots = numOfSlots;
        this.densityFunc = density;
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
        float x = this.baseLocX + i * Block.SIZE;
        float y = this.baseLocY + j * Block.SIZE;
        GameObject leaf = new Block(new Vector2(x, y),
                new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));
        leaf.setTag(TAG);
        this.gameObjects.addGameObject(leaf, this.layer);
        float randFactor = (float) Math.random();
        // TODO change random to depend on seed
        ScheduledTask delay = new ScheduledTask(leaf, randFactor,false, ()->{
            Transition<Float> angleTransition = new Transition<>(leaf, leaf.renderer()::setRenderableAngle,
                    STILL_ANGLE, TURNED_ANGLE, Transition.CUBIC_INTERPOLATOR_FLOAT,
                    TURNCYCLE_LENGTH + randFactor,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
            Transition<Vector2> widthTransition = new Transition<>(leaf, leaf::setDimensions,
                    STILL_DIM, TURNED_DIM, Transition.CUBIC_INTERPOLATOR_VECTOR,
                    (TURNCYCLE_LENGTH + randFactor) / 2,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        });
    }

}
