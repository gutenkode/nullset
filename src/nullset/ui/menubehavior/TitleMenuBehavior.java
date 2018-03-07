package nullset.ui.menubehavior;

import nullset.main.RootLayer;

public class TitleMenuBehavior extends MenuBehavior {

    public TitleMenuBehavior() {
        title = "Nullset";
        elements = new String[] {"New Game", "Options", "Quit"};
    }

    @Override
    public void onAction() {
        switch (elements[cursorPos]) {
            case "New Game":
                RootLayer.getInstance().loadIngame();
                break;
            case "Options":
                handler.openMenu(new OptionsMenuBehavior());
                break;
            case "Quit":
                onClose();
                break;
            default:
                break;
        }
    }
}
