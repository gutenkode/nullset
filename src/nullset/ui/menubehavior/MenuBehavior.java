package nullset.ui.menubehavior;

import nullset.ui.UIHandler;

/**
 * Defines the content and behavior of a menu.
 * @author Peter
 */
public abstract class MenuBehavior {

    protected int cursorPos;
    protected String title;
    protected String[] elements;
    protected UIHandler handler;

    public MenuBehavior() {
        cursorPos = 0;
        onHighlight(cursorPos);
    }

    /**
     * Tile of this menu.
     * @return 
     */
    public String getTitle() { return title; }

    /**
     * Number of entries in this menu.
     * @return 
     */
    public int getNumElements() { return elements.length; }

    /**
     * The name of the specified element.
     * @param index
     * @return 
     */
    public String getElementName(int index) { return elements[index]; }

    /**
     * Perform the action for the specified element.
     */
    public abstract void onAction();

    /**
     * Move the cursor the specified amount.
     * @param offset
     */
    public void moveCursor(int offset) {
        setCursorPos(cursorPos+offset);
    }

    /**
     * Set the cursor position.
     * The position will never be out of the range of the menu.
     * @param index
     */
    public void setCursorPos(int index) {
        cursorPos = index;
        while (cursorPos < 0)
            cursorPos += elements.length;
        cursorPos %= elements.length;
        onHighlight(cursorPos);
    }

    public int getCursorPos() { return cursorPos; }

    /**
     * Called when the cursor is hovering on an element.
     * @param index 
     */
    private void onHighlight(int index) {}

    public void onClose() {
        handler.closeMenu(this);
    }

    public final void setUIHandler(UIHandler h) {
        handler = h;
    }
}
