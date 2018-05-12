package nullset.ui.uielement;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.FontUtils;
import mote4.util.vertex.mesh.Mesh;
import nullset.main.Input;
import nullset.ui.UIBorderBuilder;
import nullset.ui.dialogbehavior.DialogBehavior;

import static nullset.main.Vars.UI_SCALE;

public class FlavorTextElement extends UIElement {

    private Mesh border;
    private Mesh string;

    public FlavorTextElement(String text) {
        FontUtils.useMetric("font_ui");
        string = FontUtils.createString(text, 0,0, UI_SCALE,UI_SCALE);

        width = (int)Math.ceil(FontUtils.getStringWidth(text)*UI_SCALE);
        int numLines = 1+(int)text.chars().filter(ch -> ch =='\n').count();
        height = UI_SCALE * numLines;
        border = UIBorderBuilder.build(0,0,width,height);
    }


    @Override
    public void update() {

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
        string.render();
    }

    @Override
    public void destroy() {
        border.destroy();
        string.destroy();
    }
}
