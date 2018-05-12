package nullset.battle.actions;

import nullset.scenes.UIScene;
import nullset.ui.menubehavior.BattleMenuBehavior;

/**
 * The action which represents the player selecting their move during battle.
 */
public class PlayerChoiceAction implements Action {

    private static PlayerChoiceAction instance;
    public static PlayerChoiceAction getInstance() {
        if (instance == null)
            instance = new PlayerChoiceAction();
        return instance;
    }

    /////////////////

    private boolean isDone;

    private PlayerChoiceAction() {
        isDone = true;
    }

    @Override
    public void start() {
        //UIScene.getBattleUI().log("Your turn.");
        UIScene.getBattleUI().openMenu(new BattleMenuBehavior());
        isDone = false;
    }

    @Override
    public void act() {

    }

    @Override
    public boolean isDone() {
        return isDone;
    }


    public void markAsDone() {
        isDone = true;
        UIScene.getBattleUI().closeAllMenus();
    }
}
