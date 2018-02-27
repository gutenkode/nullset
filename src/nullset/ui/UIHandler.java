package nullset.ui;

import mote4.util.matrix.Transform;
import mote4.util.shader.ShaderMap;
import nullset.main.Vars;
import nullset.ui.dialogbehavior.DialogBehavior;
import nullset.ui.dialogbehavior.TextDialogBehavior;
import nullset.ui.menubehavior.PauseMenuBehavior;
import nullset.ui.menubehavior.MenuBehavior;
import nullset.ui.uielement.DialogElement;
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

    public UIHandler() {
        currentState = INGAME_UNPAUSED;
        menuElements = new Stack<>();
        openMenu(new PauseMenuBehavior(this));
        openDialog(new TextDialogBehavior(this, "Hello, World!\nThis line is longer than the first one.\nThis one is short."));
    }

    public void update() {
        menuElements.peek().update();
        if (dialogElement != null) dialogElement.update();
    }
    public void render(Transform trans) {
        ShaderMap.use("sprite_unlit");

        trans.model.setIdentity();
        trans.model.translate(UI_SCALE+UI_SCREEN_EDGE_PADDING,UI_SCALE+UI_SCREEN_EDGE_PADDING,0);
        trans.bind();
        for (MenuElement r : menuElements) { // TODO change this to a relative-to system for rendering? more dynamic that way
            r.render(trans.model);
            trans.model.translate(UI_SCALE/2,UI_SCALE/2,0);
        }

        if (dialogElement != null) {
            trans.model.setIdentity();
            trans.model.translate(UI_SCALE+UI_SCREEN_EDGE_PADDING, Vars.SCENE_H-(dialogElement.getHeight()+UI_SCALE+UI_SCREEN_EDGE_PADDING), 0);
            trans.bind();
            dialogElement.render(trans.model);
        }
    }

    public void openMenu(MenuBehavior b) {
        MenuElement renderer = new MenuElement(b);
        menuElements.push(renderer);
    }
    public void closeMenu(MenuBehavior b) {
        if (b == null || menuElements.peek().getBehavior() == b) {
            if (menuElements.size() > 1) {
                menuElements.peek().destroy();
                menuElements.pop();
            }
        } else {
            throw new IllegalArgumentException("Attempted to close a menu that is not active.");
        }
    }

    public void openDialog(DialogBehavior b) {
        dialogElement = new DialogElement(b);
    }
    public void closeDialog() {
        dialogElement = null;
    }

    public void destroy() {
        for (MenuElement r : menuElements)
            r.destroy();
    }
}
