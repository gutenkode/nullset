package nullset.battle.actions;

import nullset.scenes.UIScene;
import nullset.ui.dialogbehavior.TextDialogBehavior;

public class DialogAction implements Action {

    private TextDialogBehavior behavior;

    public DialogAction(String text) {
        behavior = new TextDialogBehavior(text);
    }

    @Override
    public void start() {
        UIScene.getBattleUI().openDialog(behavior);
    }

    @Override
    public void act() {

    }

    @Override
    public boolean isDone() {
        return !behavior.isOpen();
    }
}
