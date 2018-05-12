package nullset.battle.actions;

import nullset.battle.fighters.Fighter;
import nullset.rpg.Item;
import nullset.scenes.UIScene;

public class ItemAction implements Action {

    private Item item;
    private Fighter[] targets;

    public ItemAction(Item item, Fighter... targets) {
        this.item = item;
        this.targets = targets;
    }

    @Override
    public void start() {
        UIScene.getBattleUI().log("Item used: "+item.itemName);
        item.useInBattle(targets);
        item.consume();
    }

    @Override
    public void act() {

    }

    @Override
    public boolean isDone() {
        return true;
    }
}
