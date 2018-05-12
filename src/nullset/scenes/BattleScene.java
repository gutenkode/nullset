package nullset.scenes;

import mote4.scenegraph.Scene;
import mote4.util.matrix.Transform;
import nullset.battle.Battle;

import static org.lwjgl.opengl.GL11.*;

public class BattleScene implements Scene {

    private Transform trans;
    private BattleBackground background;

    public BattleScene() {
        trans = new Transform(true);
        background = new BattleBackground();
    }

    @Override
    public void update(double time, double delta) {
        Battle.getCurrent().update();
    }

    @Override
    public void render(double time, double delta) {
        glDisable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT);

        trans.model.setIdentity();
        background.render(time);
        Battle.getCurrent().render(trans);
    }

    @Override
    public void framebufferResized(int width, int height) {
        background.framebufferResized(width, height);
        trans.projection.setOrthographic(0,0,width,height,-1,1);
    }

    @Override
    public void destroy() {

    }
}
