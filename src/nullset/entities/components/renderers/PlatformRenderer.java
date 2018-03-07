package nullset.entities.components.renderers;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.texture.Texture;
import mote4.util.vertex.mesh.MeshMap;
import org.joml.Vector3f;

/**
 * Renderer optimized for platform-like objects, like elevators, buttons, etc.
 */
public class PlatformRenderer extends Renderer {

    private Texture tex;

    public PlatformRenderer(Texture t) {
        this(t,1,1);
    }
    public PlatformRenderer(Texture t, int x, int y) {
        if (t == null)
            throw new IllegalArgumentException("Texture cannot be null!");
        tex = t;
        setTilesetSize(x,y);
    }

    @Override
    public void render(TransformationMatrix matrix) {
        bindUniforms();
        matrix.setIdentity();

        matrix.translate(entity.getPos().x,entity.getPos().y,entity.getPos().z);
        matrix.translate(entity.getSize().x/2,0,entity.getSize().z/2);
        matrix.translate(modelPos.x,modelPos.y,modelPos.z);

        matrix.rotate(-modelRot.z, 0,0,1);
        matrix.rotate(-modelRot.y, 0,1,0);
        matrix.rotate(-modelRot.x, 1,0,0);

        matrix.scale(entity.getSize().x/2,1,entity.getSize().z/2);
        matrix.scale(modelScale.x,modelScale.y,modelScale.z);

        matrix.rotate((float)Math.PI/2,1,0,0);

        matrix.bind();
        tex.bind();
        MeshMap.render("quad");
    }

    @Override
    public void renderShadow(TransformationMatrix matrix) {
        render(matrix);
    }
}
