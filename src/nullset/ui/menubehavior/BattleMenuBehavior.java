package nullset.ui.menubehavior;

import nullset.ui.UIHandler;

public class BattleMenuBehavior extends MenuBehavior {

    public BattleMenuBehavior() {
        title = "Player Action";
        elements = new String[]{"Attack", "Skills", "Inventory", "Run"};
    }

    @Override
    public void onAction() {
        switch (elements[cursorPos]) {
            case "Attack":
                //
                break;
            case "Skills":
                //
                break;
            case "Inventory":
                handler.openMenu(new InventoryMenuBehavior());
                break;
            case "Run":
                //
                break;
            default:
                break;
        }
    }
}