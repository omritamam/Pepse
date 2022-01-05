package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final String STANDING_IMAGE_PATH = "pepse/assets/player/standing avatar.png";
    private static final String SITTING_IMAGE_PATH = "pepse/assets/player/sitting.png";

    private static final String[] JUMPING_IMAGES_PATHS = new String[]{
            "pepse/assets/player/jumping 1.png" , "pepse/assets/player/jumping 2.png", "pepse/assets/player/jumping 3.png"};
    private static final String[] FLYING_IMAGES_PATHS = new String[]{
            "pepse/assets/player/flying 1.png" , "pepse/assets/player/flying 2.png", "pepse/assets/player/flying 3.png" };
    private static final String[] WALKING_IMAGES_PATHS = new String[]{
            "pepse/assets/player/walking 1.png" , "pepse/assets/player/walking 2.png" };
    private static final String[] FALLING_IMAGES_PATHS = new String[]{
            "pepse/assets/player/falling 0.png", "pepse/assets/player/falling 1.png" };

    private static final float MOVEMENT_SPEED = 300;
    private static final float GRAVITY = 800;
    private static final float JUMP_SPEED = 400;
    private static final float FLYING_SPEED = 400;
    private static final String TAG = "Avatar";
    public static final int MAX_POWER = 100;
    public static final double POWER_UP_RESTING = 0.5;
    public static final int POWER_DOWN_FLYING = 1;
    public static Vector2 DIEMNSIONS = new Vector2(100,100);

    private double Power = 100;
    private static UserInputListener inputListener;
    private static AnimationRenderable WalkingRenderable = null;
    private static AnimationRenderable JumpingAnimation = null;
    private static AnimationRenderable FlyingAnimation = null;
    private static ImageRenderable StandingRenderable = null;
    private static Renderable FallingAnimation = null;

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
        if (JumpingAnimation != null) {
            return;
        }
        // TODO numbers in constants
        FlyingAnimation = createAnimationRenderer(imageReader,FLYING_IMAGES_PATHS,0.2);
        JumpingAnimation = createAnimationRenderer(imageReader,JUMPING_IMAGES_PATHS,0.6);
        FallingAnimation = createAnimationRenderer(imageReader,FALLING_IMAGES_PATHS,0.75);
        WalkingRenderable = createAnimationRenderer(imageReader,WALKING_IMAGES_PATHS,0.4);
        StandingRenderable = imageReader.readImage(STANDING_IMAGE_PATH,true);
    }

    private static AnimationRenderable createAnimationRenderer(ImageReader imageReader, String[] imagesPaths, double timeBetweenClips) {
        Renderable[] clips = new Renderable[imagesPaths.length];
        for (int i = 0; i < imagesPaths.length; i++) {
            clips[i] = imageReader.readImage(imagesPaths[i], true);
        }
        return new AnimationRenderable(clips,timeBetweenClips);
    }

    public static Avatar create(GameObjectCollection gameObjects,
                         int layer,
                         Vector2 topLeftCorner,
                         UserInputListener inputListener,
                         ImageReader imageReader){
        Avatar.inputListener = inputListener;
        Avatar avatar = new AvatarPowerWindowDecorator(topLeftCorner,DIEMNSIONS,imageReader.readImage(STANDING_IMAGE_PATH,true));
        gameObjects.addGameObject(avatar,layer);
        avatar.transform().setAccelerationY(GRAVITY);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        instansiateAnimations(imageReader);
        avatar.setTag(TAG);
        return avatar;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
            setVelocity(Vector2.ZERO);
    }

    @Override
    public void update(float deltaTime) {
        //add sound
        super.update(deltaTime);
        if(transform().getVelocity().x() == 0 && transform().getVelocity().y() == 0){
            renderer().setRenderable(StandingRenderable);
        }
        //left-right
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            transform().setVelocityX(-MOVEMENT_SPEED);
            renderer().setIsFlippedHorizontally(true);
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            transform().setVelocityX(MOVEMENT_SPEED);
            renderer().setIsFlippedHorizontally(false);
        }

        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE)){
                //flying
                if(inputListener.isKeyPressed(KeyEvent.VK_SHIFT)){
                    if(Power > 0){
                        transform().setAccelerationY(0);
                        transform().setVelocityY(-FLYING_SPEED);
                        renderer().setRenderable(FlyingAnimation);
                        Power = Math.max(-POWER_UP_RESTING, Power - POWER_DOWN_FLYING);
                        return;
                    }
                    else{
                        transform().setAccelerationY(GRAVITY);
                    }
                }
                //jumping
                else if(transform().getVelocity().y() == 0){
                    transform().setVelocityY(-JUMP_SPEED);
                    renderer().setRenderable(JumpingAnimation);
                }
        }
        else {
            transform().setAccelerationY(GRAVITY);
            if(transform().getVelocity().x()!=0){
                renderer().setRenderable(WalkingRenderable);
            }
        }
        if(transform().getVelocity().y() > 0.0f){
            renderer().setRenderable(FallingAnimation);
        }
        else if(transform().getVelocity().y() == 0.0f){
            Power = Math.min(MAX_POWER, Power + POWER_UP_RESTING);
        }
    }

    public double getPower() {
        return Power;
    }
}
