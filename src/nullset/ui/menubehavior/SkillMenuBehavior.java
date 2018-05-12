package nullset.ui.menubehavior;

import nullset.battle.Battle;
import nullset.battle.actions.PlayerChoiceAction;
import nullset.battle.actions.SkillAction;
import nullset.battle.fighters.Fighter;
import nullset.main.RootLayer;
import nullset.rpg.PlayerInventory;
import nullset.rpg.Skill;
import nullset.ui.dialogbehavior.TextDialogBehavior;

import java.util.List;

import static nullset.main.RootLayer.GameState.BATTLE;
import static nullset.main.RootLayer.GameState.INGAME;
import static nullset.rpg.AttribEnums.Effect.NONE;

public class SkillMenuBehavior extends MenuBehavior {

    public SkillMenuBehavior() {
        title = "Skills";

        List<Skill> skills = PlayerInventory.getInstance().getSkills();
        elements = new String[skills.size()+1];
        for (int i = 0; i < elements.length-1; i++) {
            elements[i] = skills.get(i).skillName;
        }
        elements[elements.length-1] = "Back";
    }

    @Override
    public void onAction() {
        switch (elements[cursorPos]) {
            case "Back":
                onClose();
                break;
            default:
                Skill s = PlayerInventory.getInstance().getSkills().get(cursorPos);
                if (RootLayer.getInstance().getCurrentState() == INGAME && s.effect != NONE)
                {
                    s.useInGame();
                }
                else if (RootLayer.getInstance().getCurrentState() == BATTLE && s.effect != NONE)
                {
                    // TODO handle different types of targeting for skills
                    handler.openMenu(new TargetSelectBehavior(this::skillCallback, false,true,false));
                }
                else
                    handler.openDialog("You can't use this here.");
                break;
        }
    }

    @Override
    protected void onHighlight() {
        if (cursorPos == elements.length-1)
            handler.closeFlavorText();
        else {
            Skill s = PlayerInventory.getInstance().getSkills().get(cursorPos);
            handler.openFlavorText(s.description);
        }
    }

    private void skillCallback(Fighter... fighters) {
        Skill s = PlayerInventory.getInstance().getSkills().get(cursorPos);
        Battle.getCurrent().addAction(new SkillAction(s, fighters));
        PlayerChoiceAction.getInstance().markAsDone();
    }
}
