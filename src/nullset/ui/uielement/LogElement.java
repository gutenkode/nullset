package nullset.ui.uielement;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.FontUtils;
import mote4.util.vertex.mesh.Mesh;
import nullset.ui.UIBorderBuilder;

import java.util.ArrayList;
import java.util.List;

import static nullset.main.Vars.UI_SCALE;

public class LogElement extends UIElement {

    private Mesh border;
    private Mesh string;
    private List<String> lines;
    private int maxLines = 4;
    private boolean isUpdated = true;

    public LogElement() {
        lines = new ArrayList<>();

        width = 300;
        height = UI_SCALE*maxLines;
        border = UIBorderBuilder.build(0,0,width,height);
    }

    @Override
    public void update() {
        if (!isUpdated) {
            isUpdated = true;

            if (string != null)
                string.destroy();

            while (lines.size() > maxLines)
                lines.remove(0);

            StringBuilder b = new StringBuilder();
            for (String s : lines)
                b.append(s).append("\n");

            FontUtils.useMetric("font_ui");
            string = FontUtils.createString(b.toString(), 0,0, UI_SCALE,UI_SCALE);
        }
    }

    public void log(String s) {
        lines.add(s);
        isUpdated = false;
    }
    public void clear() {
        lines.clear();
        isUpdated = false;
    }

    @Override
    public void render(TransformationMatrix model) {
        Uniform.vec("spriteInfo",1,1,0);
        model.bind();

        // render background
        TextureMap.bindFiltered("ui_scalablemenu_blur");
        border.render();

        // render text
        if (string != null) {
            TextureMap.bind("font_ui");
            string.render();
        }
    }

    @Override
    public void destroy() {
        border.destroy();
        string.destroy();
    }
}
