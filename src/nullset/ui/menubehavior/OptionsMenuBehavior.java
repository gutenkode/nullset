package nullset.ui.menubehavior;

public class OptionsMenuBehavior extends MenuBehavior {

    public OptionsMenuBehavior() {
        title = "Options";
        elements = new String[] {"Back"};
    }

    @Override
    public void onAction() {
        switch (elements[cursorPos]) {
            case "Back":
                onClose();
                break;
            default:
                break;
        }
    }
}
