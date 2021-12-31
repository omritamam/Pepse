package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

public class Leaf extends GameObject {
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

    private final int seed;
    private final Runnable respawn;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private Transition<Float> horizontal;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, int seed, int layer,
                GameObjectCollection gameObjects, Runnable respawn) {
        super(topLeftCorner, dimensions, renderable);
        this.setTag(TAG);
        this.seed = seed;
        this.layer = layer;
        this.gameObjects = gameObjects;
        this.respawn = ()->{
            respawn.run();
            this.gameObjects.removeGameObject(this, this.layer);
        };

        float turnTime = (float) Math.random();
        // TODO change random to depend on seed
        ScheduledTask turnDelay = new ScheduledTask(this, turnTime,false,
                this::turn);
        float lifeTime = 10 + (float) new Random().nextInt(UPPER_LIFETIME_BOUND);
        // TODO change random to depend on seed
        ScheduledTask fallDelay = new ScheduledTask(this, lifeTime, false,
                this::fall);
    }

    private void turn(){
        Transition<Float> angleTransition = new Transition<>(this,
                this.renderer()::setRenderableAngle, STILL_ANGLE, TURNED_ANGLE,
                Transition.CUBIC_INTERPOLATOR_FLOAT,TURNCYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        Transition<Vector2> widthTransition = new Transition<>(this,
                this::setDimensions, STILL_DIM, TURNED_DIM, Transition.CUBIC_INTERPOLATOR_VECTOR,
                (TURNCYCLE_LENGTH) / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    private void fall(){
        this.transform().setVelocityY(LEAF_DROP_RATE);
        this.horizontal = new Transition<>(this,
                this.transform()::setVelocityX, LEFT_HORIZONTAL, RIGHT_HORIZONTAL,
                Transition.CUBIC_INTERPOLATOR_FLOAT, HORIZONTAL_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        fade();
    }

    private void fade(){
        this.renderer().fadeOut(FADEOUT_TIME, ()->{
            float respawnTime = (float) Math.random();
            // TODO change random to depend on seed
            ScheduledTask respawnDelay = new ScheduledTask(this, respawnTime,
                    false, this.respawn);
        });
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.removeComponent(this.horizontal);
    }
}
