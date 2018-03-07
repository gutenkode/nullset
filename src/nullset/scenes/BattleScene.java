package nullset.scenes;

import mote4.scenegraph.Scene;
import mote4.util.matrix.Transform;
import nullset.battle.Battle;

import static org.lwjgl.opengl.GL11.*;

public class BattleScene implements Scene {

    private Transform trans;

    public BattleScene() {
        trans = new Transform(true);
    }

    @Override
    public void update(double time, double delta) {
        Battle.getCurrent().update();
    }

    @Override
    public void render(double time, double delta) {
        glDisable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT);

        Battle.getCurrent().render(trans);
    }

    @Override
    public void framebufferResized(int width, int height) {
        trans.projection.setOrthographic(0,0,width,height,-1,1);
        //trans.projection.setPerspective(width,height,.1f,100f,60f);
    }

    @Override
    public void destroy() {

    }
}
