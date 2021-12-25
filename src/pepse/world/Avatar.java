package pepse.world;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Avatar extends GameObject {


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

    static Avatar create(danogl.collisions.GameObjectCollection gameObjects,
                         int layer,
                         danogl.util.Vector2 topLeftCorner,
                         danogl.gui.UserInputListener inputListener,
                         danogl.gui.ImageReader imageReader){
    throw new NotImplementedException();
    }
}
