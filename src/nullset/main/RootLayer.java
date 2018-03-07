package nullset.main;

import mote4.scenegraph.Layer;
import mote4.scenegraph.target.FBO;
import mote4.util.texture.TextureMap;
import nullset.battle.Battle;
import nullset.room.Room;
import nullset.rpg.Enemy;
import nullset.scenes.BattleScene;
import nullset.scenes.GameScene;
import nullset.scenes.UIScene;

import java.util.List;

import static nullset.main.RootLayer.GameState.*;

public class RootLayer extends Layer {

    private static RootLayer instance;
    public static RootLayer getInstance() {
        if (instance == null)
            instance = new RootLayer();
        return instance;
    }

    ////////////

    enum GameState {
        TITLE,
        INGAME,
        BATTLE;
    }

    private FBO sceneFBO;
    private GameState currentState, targetState;
    private int lastW = -1, lastH = -1;

    private RootLayer() {
        super(null);
        loadTitleScreen();
    }

    private void flagStateChange(GameState s) {
        targetState = s;
    }
    private void setState(GameState s) {
        if (s != currentState) {
            super.destroy(); // delete all current scenes
            clearScenes();
            currentState = s;
            switch (currentState) {
                case TITLE:
                    addScene(UIScene.createTitleUI());
                    break;
                case INGAME:
                    addScene(new GameScene());
                    addScene(UIScene.createPauseUI());
                    break;
                case BATTLE:
                    addScene(new BattleScene());
                    addScene(UIScene.createBattleUI());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            if (lastW != -1) // if width and height have been initialized already,
                super.framebufferResized(lastW, lastH); // initialize new scenes
            System.gc(); // probably a good time to run the gc
        }
    }

    @Override
    public void update(double time, double delta) {
        super.update(time, delta);
        if (targetState != currentState)
            setState(targetState);
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

    public void loadTitleScreen() {
        flagStateChange(TITLE);
    }
    public void loadIngame() {
        String startRoom = "1a_start";
        Room.loadNewRoom(startRoom);
        flagStateChange(INGAME);
    }
    public void loadBattle(List<Enemy> enemies) {
        Battle.loadNewBattle(enemies);
        flagStateChange(BATTLE);
    }
    public void endBattle() {
        flagStateChange(INGAME);
    }
}
