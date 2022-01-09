package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Layers;
import pepse.PepseGameManager;
import pepse.world.Block;

import java.util.Objects;
import java.util.Random;

public class Leaf extends Block {
    private static final String TAG = "leaf";
    private static final Float STILL_ANGLE = -20f;
    private static final Float TURNED_ANGLE = 30f;
    private static final Float TURNCYCLE_LENGTH = 5f;
    private static final Vector2 STILL_DIM = new Vector2(25, 45);
    private static final Vector2 TURNED_DIM = new Vector2(30, 30);
    private static final int FADEOUT_TIME = 20;
    private static final int HORIZONTAL_TIME = 5;
    private static final float LEFT_HORIZONTAL = -20;
    private static final float RIGHT_HORIZONTAL = 20;
    private static final float LEAF_DROP_RATE = 20;
    private static final int UPPER_LIFETIME_BOUND = 100;

    private final int seed;
    private final Runnable respawn;
    private final GameObjectCollection gameObjects;
    private final float[] hashFactors;
    private int layer;
    private Transition<Float> horizontal;
    private Transition<Float> angleTransition;
    private Transition<Vector2> widthTransition;

    /**
     * constructor
     * @param topLeftCorner of the leaf
     * @param dimensions of the leaf
     * @param renderable of the lead
     * @param seed random factor seed
     * @param layer of the seed
     * @param gameObjects collection of game objects
     * @param respawn respawn function for the leaf
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, int seed, int layer,
                GameObjectCollection gameObjects, Runnable respawn) {
        super(topLeftCorner, renderable);
        this.setTag(TAG);
        this.seed = seed;
        this.layer = layer;
        this.gameObjects = gameObjects;
        this.hashFactors = new float[] {topLeftCorner.x(), topLeftCorner.y()};
        physics().setMass(0);
        this.respawn = ()->{
            respawn.run();
            this.gameObjects.removeGameObject(this, this.layer);
        };

        float turnTime = new Random(Objects.hash(
                this.hashFactors[0], this.hashFactors[1], this.seed)).nextFloat();
        ScheduledTask turnDelay = new ScheduledTask(this, turnTime,false,
                this::turn);
        float lifeTime = new Random(Objects.hash(this.hashFactors[0], this.hashFactors[1], this.seed))
                .nextInt(UPPER_LIFETIME_BOUND);
        ScheduledTask fallDelay = new ScheduledTask(this, lifeTime, false,
                this::fall);
    }

    /**
     * turns the leaf which means changing their dimensions and angle
     */
    private void turn(){
        this.angleTransition = new Transition<>(this,
                this.renderer()::setRenderableAngle, STILL_ANGLE, TURNED_ANGLE,
                Transition.CUBIC_INTERPOLATOR_FLOAT,TURNCYCLE_LENGTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        this.widthTransition = new Transition<>(this,
                this::setDimensions, STILL_DIM, TURNED_DIM, Transition.CUBIC_INTERPOLATOR_VECTOR,
                (TURNCYCLE_LENGTH) / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * makes the leaf fall
     */
    private void fall(){
        this.transform().setVelocityY(LEAF_DROP_RATE);
        this.gameObjects.removeGameObject(this, this.layer);
        this.layer++;
        this.gameObjects.addGameObject(this, this.layer);
        this.gameObjects.layers().shouldLayersCollide(PepseGameManager.layers.get(Layers.FALL),
                PepseGameManager.layers.get(Layers.SURFACE), true);
        this.gameObjects.layers().shouldLayersCollide(PepseGameManager.layers.get(Layers.FALL),
                PepseGameManager.layers.get(Layers.DEEP_GROUND),false);
        this.horizontal = new Transition<>(this,
                this.transform()::setVelocityX, LEFT_HORIZONTAL, RIGHT_HORIZONTAL,
                Transition.CUBIC_INTERPOLATOR_FLOAT, HORIZONTAL_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        fade();
    }

    /**
     * makes the leaf fade
     */
    private void fade(){
        this.renderer().fadeOut(FADEOUT_TIME, ()->{
            float respawnTime = new Random(Objects.hash(
                    this.hashFactors[0], this.hashFactors[1], this.seed)).nextFloat();
            ScheduledTask respawnDelay = new ScheduledTask(this, respawnTime,
                    false, this.respawn);
        });
    }

    /**
     * overrides GameObject onCollisionEnter
     * resets the velocity of the leaf, and the transitions
     * @param other object collided with
     * @param collision collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.setVelocity(Vector2.ZERO);
        this.removeComponent(this.horizontal);
        this.removeComponent(this.angleTransition);
        this.removeComponent(this.widthTransition);
    }
}
