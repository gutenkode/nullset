package nullset.ui.dialogbehavior;

import nullset.ui.UIHandler;

/**
 * Defines the content and behavior of a dialog box.
 * @author Peter
 */
public abstract class DialogBehavior {

    protected String text;
    protected UIHandler handler;

    public DialogBehavior(UIHandler h) {
        handler = h;
    }

    public String getText() { return text; }

    public abstract void onAction();
}
