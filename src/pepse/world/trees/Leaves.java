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

public class Leaves {
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
    private static final int UPPER_LIFETIME_BOUND = 30;

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

        // turn leaves
        float turnTime = (float) Math.random();
        // TODO change random to depend on seed
        Runnable turn = ()->{
            Transition<Float> angleTransition = new Transition<>(leaf, leaf.renderer()::setRenderableAngle,
                    STILL_ANGLE, TURNED_ANGLE, Transition.CUBIC_INTERPOLATOR_FLOAT,
                    TURNCYCLE_LENGTH + turnTime,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
            Transition<Vector2> widthTransition = new Transition<>(leaf, leaf::setDimensions,
                    STILL_DIM, TURNED_DIM, Transition.CUBIC_INTERPOLATOR_VECTOR,
                    (TURNCYCLE_LENGTH + turnTime) / 2,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        };
        ScheduledTask turnDelay = new ScheduledTask(leaf, turnTime,false, turn);

        // fade
        Runnable fade = ()->{
            // respawn
            Runnable respawn = ()->{createLeaf(i, j);};
            leaf.renderer().fadeOut(FADEOUT_TIME, ()->{
                float respawnTime = (float) Math.random();
                // TODO change random to depend on seed
                ScheduledTask respawnDelay = new ScheduledTask(leaf, respawnTime,false, respawn);
            });
        };

        // leaves fall
        float lifeTime = 10 + (float) new Random().nextInt(UPPER_LIFETIME_BOUND);
        // TODO change random to depend on seed
        Runnable fall = ()->{
            leaf.transform().setVelocityY(LEAF_DROP_RATE);
            Transition<Float> horizontal = new Transition<>(leaf, leaf.transform()::setVelocityX,
                    LEFT_HORIZONTAL, RIGHT_HORIZONTAL, Transition.CUBIC_INTERPOLATOR_FLOAT, HORIZONTAL_TIME,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
            fade.run();
        };
        ScheduledTask fallDelay = new ScheduledTask(leaf, lifeTime, false, fall);

    }

}
