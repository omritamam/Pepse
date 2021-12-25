package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        GameObject sun = new GameObject(Vector2.ZERO, windowDimensions,
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag("sun");

       // Transition<Float> transition = new Transition<Float>(sun,sun::setCenter,
      //          0f,360,Transition.TransitionType.TRANSITION_LOOP,cycleLength, Transition.LINEAR_INTERPOLATOR_FLOAT, null);


        return sun;
    }
    private static Vector2 calcSunPosition(Vector2 windowDimensions,
                                           float angleInSky){
        return Vector2.ZERO;
    }
    private static void setCenter(GameObject sun, Vector2 location){
        sun.setCenter(location);

    }


}
