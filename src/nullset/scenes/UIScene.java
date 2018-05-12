package nullset.scenes;

import mote4.scenegraph.Scene;
import mote4.util.matrix.Transform;
import nullset.ui.UIHandler;
import nullset.ui.dialogbehavior.*;
import nullset.ui.menubehavior.*;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;

public class UIScene implements Scene {

    private static UIScene titleScene, pauseScene, battleScene;
    public static UIScene createTitleUI() {
        if (titleScene == null) {
            titleScene = new UIScene();
            titleScene.handler.openMenu(new TitleMenuBehavior());
        }
        return titleScene;
    }
    public static UIScene createPauseUI() {
        if (pauseScene == null) {
            pauseScene = new UIScene();
            pauseScene.handler.openMenu(new PauseMenuBehavior());
            pauseScene.handler.openDialog(new TextDialogBehavior("Hello, World!\nThis line is longer than the first one.\nThis one is short."));
        }
        return pauseScene;
    }
    public static UIScene createBattleUI() {
        if (battleScene == null) {
            battleScene = new UIScene();
            //battleScene.handler.openMenu(new BattleMenuBehavior());
        }
        return battleScene;
    }
    public static UIHandler getTitleUI() {
        return titleScene.handler;
    }
    public static UIHandler getPauseUI() {
        return pauseScene.handler;
    }
    public static UIHandler getBattleUI() {
        return battleScene.handler;
    }

    ////////////

    private Transform trans;
    private UIHandler handler;

    private UIScene() {
        trans = new Transform(true);
        handler = new UIHandler();

    }

    public UIHandler getHandler() {
        return handler;
    }

    @Override
    public void update(double time, double delta) {
        handler.update();
    }

    @Override
    public void render(double time, double delta) {
        glDisable(GL_DEPTH_TEST);
        // note, doesn't clear the framebuffer since UIScene draws on top of other scenes

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
