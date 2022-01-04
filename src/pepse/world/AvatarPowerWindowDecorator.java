package pepse.world;

import danogl.GameObject;
import danogl.gui.Sound;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class AvatarPowerWindowDecorator extends Avatar{
    private static final Vector2 SCORE_WINDOW_DIEMNSIONS = new Vector2(80,20);
    private static final int HIGH = 90;
    private static final int LOW = 20;

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
        TextRenderable = new TextRenderable(String.valueOf(super.getPower()),"Ariel", false, true);
        scoreWindow = new GameObject(topLeftCorner.add(new Vector2(0,dimensions.y())),
                SCORE_WINDOW_DIEMNSIONS, TextRenderable);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double power = getPower();
        TextRenderable.setString(String.valueOf((int)power));
        if(power>HIGH){
            TextRenderable.setColor(Color.GREEN);
        }
        else if(power>LOW){
            TextRenderable.setColor(Color.BLUE);
        }
        else{
            TextRenderable.setColor(Color.RED);
        }
        scoreWindow.update(deltaTime);
        Vector2 avatarDiementions = super.getDimensions();
        scoreWindow.setCenter(super.getCenter().add(new Vector2(avatarDiementions.x()*0.15f,0.6f * avatarDiementions.y())));
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        scoreWindow.render(g);
    }

    @Override
    public void render(Graphics2D g, Camera camera) {
        super.render(g, camera);
        scoreWindow.render(g,camera);
    }
}
