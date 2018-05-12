package nullset.ui;

import mote4.util.matrix.Transform;
import mote4.util.shader.ShaderMap;
import nullset.main.Vars;
import nullset.ui.dialogbehavior.DialogBehavior;
import nullset.ui.dialogbehavior.TextDialogBehavior;
import nullset.ui.menubehavior.PauseMenuBehavior;
import nullset.ui.menubehavior.MenuBehavior;
import nullset.ui.uielement.DialogElement;
import nullset.ui.uielement.FlavorTextElement;
import nullset.ui.uielement.LogElement;
import nullset.ui.uielement.MenuElement;

import java.util.Stack;

import static nullset.main.Vars.UI_SCALE;
import static nullset.main.Vars.UI_SCREEN_EDGE_PADDING;
import static nullset.ui.UIHandler.UIState.*;

/**
 * Encapsulates operations needed to use menus.
 */
public class UIHandler {

    public enum UIState {
        INGAME_UNPAUSED,
        INGAME_PAUSED,
        INGAME_DIALOG;
    }

    private UIState currentState;
    private Stack<MenuElement> menuElements;
    private DialogElement dialogElement;
    private LogElement logElement;
    private FlavorTextElement flavorTextElement;
    private boolean showLog = false;

    public UIHandler() {
        currentState = INGAME_UNPAUSED;
        menuElements = new Stack<>();
        logElement = new LogElement();
    }

    public void update() {
        if (showLog)
            logElement.update();
        if (dialogElement != null)
            dialogElement.update();
        else if (!menuElements.isEmpty())
            menuElements.peek().update();
    }
    public void render(Transform trans) {
        ShaderMap.use("sprite_unlit");

        // render log box
        if (showLog) {
            trans.model.setIdentity();
            trans.model.translate(150 + UI_SCALE + UI_SCREEN_EDGE_PADDING, UI_SCALE + UI_SCREEN_EDGE_PADDING, 0);
            trans.bind();
            logElement.render(trans.model);
        }

        // render the menu stack
        trans.model.setIdentity();
        trans.model.translate(UI_SCALE+UI_SCREEN_EDGE_PADDING,UI_SCALE+UI_SCREEN_EDGE_PADDING,0);
        trans.bind();
        for (MenuElement r : menuElements) {
            r.render(trans.model);
            trans.model.translate(UI_SCALE/2,UI_SCALE/2,0);
        }
        // render flavor text if it's active
        if (flavorTextElement != null) {
            trans.model.translate(menuElements.peek().getWidth(),0,0);
            flavorTextElement.render(trans.model);
        }

        // render dialog box
        if (dialogElement != null) {
            trans.model.setIdentity();
            trans.model.translate(UI_SCALE+UI_SCREEN_EDGE_PADDING, Vars.SCENE_H-(dialogElement.getHeight()+UI_SCALE+UI_SCREEN_EDGE_PADDING), 0);
            trans.bind();
            dialogElement.render(trans.model);
        }
    }

    public void openMenu(MenuBehavior b) {
        b.setUIHandler(this);
        MenuElement renderer = new MenuElement(b);
        menuElements.push(renderer);
        closeFlavorText(); // automatically close flavor text as it cannot be relevant to a new menu
        b.refresh();
    }
    public void closeMenu(MenuBehavior b) {
        if (b == null || menuElements.peek().getBehavior() == b) {
            if (menuElements.size() > 1) { // cannot close the root menu
                menuElements.peek().destroy();
                menuElements.pop();
                closeFlavorText(); // automatically close flavor text as it cannot be relevant to a new menu
                menuElements.peek().getBehavior().refresh();
            }
        } else {
            throw new IllegalArgumentException("Attempted to close a menu that is not active.");
        }
    }

    public void closeAllMenus() {
        while (!menuElements.isEmpty()) {
            menuElements.peek().destroy();
            menuElements.pop();
        }
        closeFlavorText();
    }
    public int numOpenMenus() { return menuElements.size(); }

    public void openDialog(DialogBehavior b) {
        b.setUIHandler(this);
        dialogElement = new DialogElement(b);
    }
    public void openDialog(String s) {
        DialogBehavior b = new TextDialogBehavior(s);
        b.setUIHandler(this);
        dialogElement = new DialogElement(b);
    }
    public void closeDialog(DialogBehavior b) {
        if (dialogElement.getBehavior() == b) {
            dialogElement.destroy();
            dialogElement = null;
        } else
            throw new IllegalArgumentException("Attempted to close a dialog that is not active.");
    }

    public void log(String s) {
        showLog = true;
        logElement.log(s);
    }
    public void clearLog() {
        logElement.clear();
    }

    public void openFlavorText(String s) {
        if (flavorTextElement != null)
            flavorTextElement.destroy();
        flavorTextElement = new FlavorTextElement(s);
    }
    public void closeFlavorText() {
        if (flavorTextElement != null)
            flavorTextElement.destroy();
        flavorTextElement = null;
    }

    public void destroy() {
        for (MenuElement r : menuElements)
            r.destroy();
        // TODO delete other elements too
    }
}
