package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class AvatarPowerWindowDecorator extends Avatar{
    private static final Vector2 SCORE_WINDOW_DIEMNSIONS = new Vector2(100,25);
    private final TextRenderable TextRenderable;
    private final GameObject scoreWindow;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public AvatarPowerWindowDecorator(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        TextRenderable = new TextRenderable(String.valueOf(super.getPower()));
        scoreWindow = new GameObject(topLeftCorner.add(new Vector2(dimensions.x() *0.5f,dimensions.y())),
                SCORE_WINDOW_DIEMNSIONS, TextRenderable);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double power = getPower();
        TextRenderable.setString(String.valueOf(power));
        if(power>90){
            TextRenderable.setColor(Color.GREEN);
        }
        else if(power>20){
            TextRenderable.setColor(Color.BLUE);

        }
        else{
            TextRenderable.setColor(Color.RED);
        }
        scoreWindow.update(deltaTime);
    }
}
