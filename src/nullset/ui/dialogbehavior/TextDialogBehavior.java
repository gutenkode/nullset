package nullset.ui.dialogbehavior;

import nullset.ui.UIHandler;

public class TextDialogBehavior extends DialogBehavior {

    public TextDialogBehavior(UIHandler h, String s) {
        super(h);
        text = s;
    }

    @Override
    public void onAction() {
        handler.closeDialog();
    }
}
