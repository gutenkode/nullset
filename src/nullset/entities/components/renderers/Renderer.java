package nullset.entities.components.renderers;

import nullset.entities.components.Component;
import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import org.joml.Vector3f;

public abstract class Renderer extends Component {

    abstract public void render(TransformationMatrix matrix);
    abstract public void renderShadow(TransformationMatrix matrix);

    protected float tilesetX = 1, tilesetY = 1,
                    frame = 0, emissiveFrame = 0,
                    emissiveMult = 0;

    public void setTilesetSize(float x, float y) {
        tilesetX = x;
        tilesetY = y;
    }
    public void setFrame(float f) {
        frame = f;
    }
    public void setEmissiveFrame(float f) {
        emissiveFrame = f;
    }
    public void setEmissiveMult(float m) {
        emissiveMult = m;
    }

    public void bindUniforms() {
        Uniform.vec("spriteInfo",tilesetX,tilesetY,frame);
        Uniform.vec("spriteInfoEmissive",tilesetX,tilesetY,emissiveFrame);
        Uniform.vec("emissiveMult",emissiveMult);
    }
}
