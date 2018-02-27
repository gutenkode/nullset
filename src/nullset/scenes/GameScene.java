package nullset.scenes;

import mote4.scenegraph.Scene;
import mote4.util.matrix.Transform;
import nullset.rooms.Room;

import static org.lwjgl.opengl.GL11.*;

public class GameScene implements Scene {

    private Transform trans;

    public GameScene() {
        trans = new Transform(true);
    }

    @Override
    public void update(double time, double delta) {
        Room.getCurrent().update();
    }

    @Override
    public void render(double time, double delta) {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        trans.view.setIdentity();
        trans.view.translate(0,0,-5); // pull camera back
        trans.view.rotate(0.7f, 1,0,0); // angle camera down slightly (default view is head-on)
        //trans.view.rotate(.3f*(float)Math.sin(time*.6), 0,1,0); // pan back and forth
        Room.getCurrent().setView(trans.view);

        Room.getCurrent().render(trans);
        Room.getCurrent().renderColliders(trans);
    }

    @Override
    public void framebufferResized(int width, int height) {
        trans.projection.setPerspective(width,height,.1f,100f,60f);
    }

    @Override
    public void destroy() {

    }
}
