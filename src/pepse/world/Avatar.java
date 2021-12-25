package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Avatar extends GameObject {
    private static final String IMAGE_PATH = "";
    public static Vector2 DIEMNSIONS = new Vector2(100,100);
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

    static Avatar create(GameObjectCollection gameObjects,
                         int layer,
                         Vector2 topLeftCorner,
                         UserInputListener inputListener,
                         ImageReader imageReader){
        return new Avatar(topLeftCorner,DIEMNSIONS,imageReader.readImage(IMAGE_PATH,true));
    }
}
