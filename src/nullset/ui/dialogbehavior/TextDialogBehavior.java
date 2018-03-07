package nullset.ui.dialogbehavior;

import nullset.ui.UIHandler;

public class TextDialogBehavior extends DialogBehavior {

    public TextDialogBehavior(String s) {
        text = s;
    }

    @Override
    public void onAction() {
        handler.closeDialog();
    }
}
