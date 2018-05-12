package nullset.ui.dialogbehavior;

public class TextDialogBehavior extends DialogBehavior {

    private boolean isOpen;

    public TextDialogBehavior(String s) {
        text = s;
        isOpen = true;
    }

    @Override
    public void onAction() {
        handler.closeDialog(this);
        isOpen = false;
    }

    public boolean isOpen() { return isOpen; }
}
