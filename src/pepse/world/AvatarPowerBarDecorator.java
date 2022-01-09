package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class AvatarPowerBarDecorator extends Avatar{
    private static final Vector2 SCORE_WINDOW_DIEMNSIONS = new Vector2(80,20);
    private static final String FONT_NAME = "Arial";
    private static final int HIGH_THRESHOLD = 90;
    private static final int MEDIUM_THRESHOLD = 20;
    private static final Color MEDIUM_COLOR = Color.BLUE;
    private static final Color LOW_COLOR = Color.RED;
    private static final Color HIGH_COLOR = Color.GREEN;
    private final TextRenderable TextRenderable;
    private final GameObject scoreWindow;

    /**
     * Construct a new GameObject instance.
     * The decorator adds a score window with avatar current score, the window will follow the avater.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     *
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public AvatarPowerBarDecorator(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        TextRenderable = new TextRenderable(String.valueOf(super.getPower()), FONT_NAME, false, true);
        scoreWindow = new GameObject(topLeftCorner.add(new Vector2(0,dimensions.y())),
                SCORE_WINDOW_DIEMNSIONS, TextRenderable);
    }

    /**
     * override the update method in oorder to update the loccation and text of scoreWindow.
     * @param deltaTime - time between frames
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double power = getPower();
        TextRenderable.setString(String.valueOf((int)power));
        if(power> HIGH_THRESHOLD){
            TextRenderable.setColor(HIGH_COLOR);
        }
        else if(power> MEDIUM_THRESHOLD){
            TextRenderable.setColor(MEDIUM_COLOR);
        }
        else{
            TextRenderable.setColor(LOW_COLOR);
        }
        scoreWindow.update(deltaTime);
        Vector2 avatarDiementions = super.getDimensions();
        scoreWindow.setCenter(super.getCenter().add(new Vector2(avatarDiementions.x()*0.15f,0.6f * avatarDiementions.y())));
    }

    /**
     * override the render method in order to render the score window as well.
     */
    @Override
    public void render(Graphics2D g) {
        super.render(g);
        scoreWindow.render(g);
    }
    /**
     * override the render method in order to render the score window as well.
     */
    @Override
    public void render(Graphics2D g, Camera camera) {
        super.render(g, camera);
        scoreWindow.render(g,camera);
    }
}
