package nullset.ui.menubehavior;

public class PauseMenuBehavior extends MenuBehavior {

    public PauseMenuBehavior() {
        title = "Paused";
        elements = new String[] {"Inventory", "Skills", "Options", "Back"};
    }

    @Override
    public void onAction() {
        switch (elements[cursorPos]) {
            case "Inventory":
                handler.openMenu(new InventoryMenuBehavior());
                break;
            case "Skills":
                //
                break;
            case "Options":
            	handler.openMenu(new OptionsMenuBehavior());
            	break;
            case "Back":
                onClose();
                break;
            default:
                break;
        }
    }
}
