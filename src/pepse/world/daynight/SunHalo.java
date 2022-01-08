package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {
    private static final Vector2 SIZE = new Vector2(250, 250);
    private static final String TAG = "halo";

    /**
     *
     * @param gameObjects
     * @param layer
     * @param sun
     * @param color
     * @return
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun, Color color){
        GameObject halo = new GameObject(Vector2.ZERO, SIZE, new OvalRenderable(color));
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag(TAG);
        gameObjects.addGameObject(halo, layer);
        return halo;
    }
}
