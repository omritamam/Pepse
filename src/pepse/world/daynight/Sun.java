package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

public class Sun {
    private static final Vector2 SIZE = new Vector2(100, 100);
    private static final String TAG = "sun";
    private static final float INIT_DEG = 0;
    private static final float FIN_DEG = 360;
    private static final int BASE_RAD = 250;
    private static final int OVAL_RAD = 100;

    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
//        Vector2 startPosition = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
        GameObject sun = new GameObject(Vector2.ZERO, SIZE,
                new OvalRenderable(Color.YELLOW));
        sun.setCenter(windowDimensions.mult(0.5f));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(TAG);
        gameObjects.addGameObject(sun, layer);
        Consumer<Float> degreeChanger =
                (d)->sun.setCenter(
                        Vector2.UP.mult(BASE_RAD+OVAL_RAD*(float)Math.sin(Math.toRadians(d))).rotated(d).add(
                                windowDimensions.mult(0.5f)));
        Transition<Float> transition = new Transition<Float>(sun, degreeChanger, INIT_DEG, FIN_DEG,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength, Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }

}