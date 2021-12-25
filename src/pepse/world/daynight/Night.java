package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO,windowDimensions, new RectangleRenderable(Color.BLACK));
        gameObjects.addGameObject(night, layer);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("sky");
        Transition<Float> transition = new Transition<>(night,night.renderer()::setOpaqueness,
                0f,MIDNIGHT_OPACITY,Transition.CUBIC_INTERPOLATOR_FLOAT,cycleLength/2, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);


        return night;
    }
}
