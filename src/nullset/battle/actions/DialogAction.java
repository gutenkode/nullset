package nullset.battle.actions;

import nullset.scenes.UIScene;
import nullset.ui.dialogbehavior.TextDialogBehavior;

public class DialogAction implements Action {
    @Override
    public void act() {
        UIScene.getBattleUI().openDialog(new TextDialogBehavior("This is a DialogAction."));
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
