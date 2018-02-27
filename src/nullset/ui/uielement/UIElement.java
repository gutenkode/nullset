package nullset.ui.uielement;

import mote4.util.matrix.TransformationMatrix;

public abstract class UIElement {

    protected int width, height;

    public abstract void update();
    public abstract void render(TransformationMatrix model);

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public abstract void destroy();
}
