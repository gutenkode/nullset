package nullset.ui.uielement;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.FontUtils;
import mote4.util.vertex.mesh.Mesh;
import nullset.main.Input;
import nullset.ui.UIBorderBuilder;
import nullset.ui.menubehavior.MenuBehavior;

import static nullset.main.Vars.UI_SCALE;

/**
 * Renders the contents of a menu.
 */
public class MenuElement extends UIElement {

    private static Mesh cursor;
    static {
        FontUtils.useMetric("font_ui");
        cursor = FontUtils.createString(">",0,0,UI_SCALE,UI_SCALE);
    }

    private Mesh border;
    private Mesh[] strings;
    private MenuBehavior behavior;

    public MenuElement(MenuBehavior b) {
        behavior = b;
        strings = new Mesh[b.getNumElements()+1];
        FontUtils.useMetric("font_ui");
        float maxWidth = FontUtils.getStringWidth(b.getTitle())*UI_SCALE;
        strings[0] = FontUtils.createString(b.getTitle(), 0,0, UI_SCALE,UI_SCALE);
        for (int i = 0; i < b.getNumElements(); i++) {
            maxWidth = Math.max(maxWidth, FontUtils.getStringWidth(b.getElementName(i))*UI_SCALE+UI_SCALE);
            strings[i+1] = FontUtils.createString(b.getElementName(i), UI_SCALE,UI_SCALE*(i+1), UI_SCALE,UI_SCALE);
        }
        width = (int)Math.ceil(maxWidth);
        height = UI_SCALE* strings.length;
        border = UIBorderBuilder.build(0,0, width,height);
    }

    public MenuBehavior getBehavior() {
        return behavior;
    }

    @Override
    public void update() {
        if (Input.getInstance().isKeyNew(Input.Key.YES))
        {
            behavior.onAction();
        }
        else if (Input.getInstance().isKeyNew(Input.Key.NO))
        {
            behavior.onClose();
        }
        else if (Input.getInstance().isKeyNew(Input.Key.UP))
        {
            // cursor up
            behavior.moveCursor(-1);
        }
        else if (Input.getInstance().isKeyNew(Input.Key.DOWN))
        {
            // cursor down
            behavior.moveCursor(1);
        }
    }
    @Override
    public void render(TransformationMatrix model) {
        Uniform.vec("spriteInfo",1,1,0);

        model.bind();
        // render background
        TextureMap.bind("ui_scalablemenu");
        border.render();
        // render text
        TextureMap.bind("font_ui");
        for (Mesh m : strings)
            m.render();
        // render cursor
        model.push();
        {
            model.translate(0, UI_SCALE * (1 + behavior.getCursorPos()));
            model.bind();
            cursor.render();
        }
        model.pop();
    }

    @Override
    public void destroy() {
        for (Mesh m : strings)
            m.destroy();
        border.destroy();
    }
}
