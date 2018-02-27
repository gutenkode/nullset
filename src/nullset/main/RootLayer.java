package nullset.main;

import mote4.scenegraph.Layer;
import mote4.scenegraph.target.FBO;
import mote4.util.texture.TextureMap;
import nullset.scenes.GameScene;
import nullset.scenes.UIScene;

public class RootLayer extends Layer {

    private static RootLayer instance;
    public static RootLayer getInstance() {
        if (instance == null)
            instance = new RootLayer();
        return instance;
    }

    ////////////

    private enum GameState {
        TITLE,
        INGAME;
    }

    private FBO sceneFBO;
    private GameState currentState;
    private int lastW = -1, lastH = -1;

    private RootLayer() {
        super(null);
        setState(GameState.INGAME);
    }

    public void setState(GameState s) {
        if (s != currentState) {
            super.destroy(); // delete all current scenes
            currentState = s;
            switch (currentState) {
                case TITLE:
                    break;
                case INGAME:
                    addScene(new GameScene());
                    addScene(new UIScene());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            if (lastW != -1) // if width and height have been initialized already,
                super.framebufferResized(lastW, lastH); // initialize new scenes
        }
    }

    @Override
    public void framebufferResized(int width, int height) {
        double aspectRatio = width/(double)height;
        int w = (int)(Vars.SCENE_H*aspectRatio);

        lastW = w;//width;
        lastH = Vars.SCENE_H;//height;
        super.framebufferResized(lastW, lastH);

        if (sceneFBO != null)
            sceneFBO.destroy();
        sceneFBO = new FBO(lastW, lastH, true, false, null);
        sceneFBO.addToTextureMap("fbo_scene");
        TextureMap.get("fbo_scene").filter(true);
        target = sceneFBO;//Framebuffer.getDefault();
    }

    @Override
    public void destroy() {
        sceneFBO.destroy();
        super.destroy();
    }
}
