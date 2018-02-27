package nullset.ui.menubehavior;

import nullset.ui.UIHandler;

public class OptionsMenuBehavior extends MenuBehavior {

    public OptionsMenuBehavior(UIHandler h) {
        super(h);
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
