package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.Layers;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;

public class Avatar extends GameObject {
    private static final String STANDING_IMAGE_PATH = "pepse/assets/player/standing avatar.png";
    private static final String[] JUMPING_IMAGES_PATHS = new String[]{
            "pepse/assets/player/jumping 1.png" , "pepse/assets/player/jumping 2.png", "pepse/assets/player/jumping 3.png"};
    private static final String[] FLYING_IMAGES_PATHS = new String[]{
            "pepse/assets/player/flying 1.png" , "pepse/assets/player/flying 2.png", "pepse/assets/player/flying 3.png" };
    private static final String WALKING_IMAGE_PATH = "pepse/assets/player/walking.png";

    private static final float MOVEMENT_SPEED = 300;
    private static final float GRAVITY = 3000;
    private static final float JUMP_SPEED = 2500;
    private static final float FLYING_SPEED = 150;
    public static Vector2 DIEMNSIONS = new Vector2(100,100);
    private static UserInputListener inputListener;
    private static ImageRenderable WalkingRenderable;
    private double Power = 100;
    private static AnimationRenderable JumpingAnimation = null;
    private static AnimationRenderable FlyingAnimation = null;
    private static ImageRenderable StandingRenderable = null;
    //private AvaterMode mode = AvaterMode.Standing;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    private static void instansiateAnimations(ImageReader imageReader) {
        if(JumpingAnimation == null){
            Renderable[] jumpingClips = new Renderable[JUMPING_IMAGES_PATHS.length];
            Renderable[] flyingClips = new Renderable[FLYING_IMAGES_PATHS.length];
            for(int i=0; i< JUMPING_IMAGES_PATHS.length; i++){
                jumpingClips[i] = imageReader.readImage(JUMPING_IMAGES_PATHS[i],true);
            }for(int i=0; i< flyingClips.length; i++){
                flyingClips[i] = imageReader.readImage(FLYING_IMAGES_PATHS[i],true);
            }
            FlyingAnimation = new AnimationRenderable(flyingClips,0.2);
            JumpingAnimation = new AnimationRenderable(jumpingClips,0.6);
            StandingRenderable = imageReader.readImage(STANDING_IMAGE_PATH,true);
            WalkingRenderable = imageReader.readImage(WALKING_IMAGE_PATH,true);
        }

    }

    public static Avatar create(GameObjectCollection gameObjects,
                         int layer,
                         Vector2 topLeftCorner,
                         UserInputListener inputListener,
                         ImageReader imageReader){
        Avatar.inputListener = inputListener;
        Avatar avatar = new Avatar(topLeftCorner,DIEMNSIONS,imageReader.readImage(STANDING_IMAGE_PATH,true));
        gameObjects.addGameObject(avatar,layer);
        avatar.transform().setAccelerationY(GRAVITY);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        instansiateAnimations( imageReader);


        return avatar;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if(transform().getVelocity().x()==0 && transform().getVelocity().y() == 0){
            renderer().setRenderable(StandingRenderable);
        }
        if(inputListener.isKeyPressed((KeyEvent.VK_LEFT))){
            movementDir = Vector2.LEFT.mult(MOVEMENT_SPEED);
        }
        else if(inputListener.isKeyPressed((KeyEvent.VK_RIGHT))){
            movementDir = Vector2.RIGHT.mult(MOVEMENT_SPEED);
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_SPACE)){
            if(transform().getVelocity().y() == 0){
                //jumping
                movementDir = movementDir.add(Vector2.UP.mult(JUMP_SPEED));
                System.out.println("jumping");

                renderer().setRenderable(JumpingAnimation);
            }
            else{
                //flying
                if(Power>0 && inputListener.isKeyPressed((KeyEvent.VK_SHIFT)) ){
                    renderer().setRenderable(FlyingAnimation);
                    movementDir = movementDir.add(Vector2.UP.mult(FLYING_SPEED));

                    Power = Math.max(-0.5, Power-1);
                }
            }

        }
        System.out.println(Power);
        Power = Math.min(100, Power+0.5);

        setVelocity(movementDir);

    }
}
