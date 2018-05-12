package nullset.ui.menubehavior;

import nullset.battle.Battle;
import nullset.battle.actions.AttackAction;
import nullset.battle.actions.PlayerChoiceAction;
import nullset.battle.fighters.Fighter;
import nullset.ui.dialogbehavior.TextDialogBehavior;

import static nullset.rpg.AttribEnums.Element.PHYS;
import static nullset.rpg.AttribEnums.Stat.STR;

public class BattleMenuBehavior extends MenuBehavior {

    public BattleMenuBehavior() {
        title = "Player Action";
        elements = new String[] {"Attack", "Skills", "Inventory", "Run"};
    }

    @Override
    public void onAction() {
        switch (elements[cursorPos]) {
            case "Attack":
                handler.openMenu(new TargetSelectBehavior(this::attackCallback, false,true,false));
                break;
            case "Skills":
                handler.openMenu(new SkillMenuBehavior());
                break;
            case "Inventory":
                handler.openMenu(new InventoryMenuBehavior());
                break;
            case "Run":
                handler.openDialog(new TextDialogBehavior("You can't run from this fight!"));
                break;
            default:
                break;
        }
    }

    private void attackCallback(Fighter... fighters) {
        if (fighters.length > 1)
            throw new IllegalArgumentException("Basic attack can only have one target!");

        Fighter player = Battle.getCurrent().getAllPlayers().get(0);
        Fighter enemy = fighters[0];
        double skillPower = player.getStats().base.get(STR);

        Battle.getCurrent().addAction(new AttackAction(player, enemy, skillPower, PHYS, true));
        PlayerChoiceAction.getInstance().markAsDone();
    }
}