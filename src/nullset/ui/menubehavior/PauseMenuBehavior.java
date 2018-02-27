package nullset.ui.menubehavior;

import nullset.ui.UIHandler;

public class PauseMenuBehavior extends MenuBehavior {

    public PauseMenuBehavior(UIHandler h) {
        super(h);
        title = "Paused";
        elements = new String[] {"Inventory", "Skills", "Options", "Back"};
    }

    @Override
    public void onAction() {
        switch (elements[cursorPos]) {
            case "Inventory":
                handler.openMenu(new InventoryMenuBehavior(handler));
                break;
            case "Skills":
                //
                break;
            case "Options":
            	handler.openMenu(new OptionsMenuBehavior(handler));
            	break;
            case "Back":
                onClose();
                break;
            default:
                break;
        }
    }
}
