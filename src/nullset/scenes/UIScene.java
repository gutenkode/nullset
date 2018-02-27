package nullset.scenes;

import mote4.scenegraph.Scene;
import mote4.util.matrix.Transform;
import nullset.ui.UIHandler;

import static org.lwjgl.opengl.GL11.*;

public class UIScene implements Scene {

    private Transform trans;
    private UIHandler handler;

    public UIScene() {
        trans = new Transform(true);
        handler = new UIHandler();
    }

    @Override
    public void update(double time, double delta) {
        handler.update();
    }

    @Override
    public void render(double time, double delta) {
        glDisable(GL_DEPTH_TEST);
        handler.render(trans);
    }

    @Override
    public void framebufferResized(int width, int height) {
        trans.projection.setOrthographic(0,0,width,height,-1,1);
    }

    @Override
    public void destroy() {
        handler.destroy();
    }
}
