package nullset.entities.components.renderers;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.texture.Texture;
import mote4.util.vertex.mesh.Mesh;
import org.joml.Vector3f;

public class MeshRenderer extends Renderer {

    private Mesh mesh;
    private Texture tex;

    public MeshRenderer(Mesh m, Texture t) {
        if (m == null)
            throw new IllegalArgumentException("Mesh cannot be null!");
        if (t == null)
            throw new IllegalArgumentException("Texture cannot be null!");
        mesh = m;
        tex = t;
    }

    @Override
    public void render(TransformationMatrix matrix) {
        bindUniforms();
        matrix.setIdentity();

        matrix.translate(entity.getPos().x,entity.getPos().y,entity.getPos().z);
        matrix.translate(modelPos.x,modelPos.y,modelPos.z);

        matrix.rotate(-modelRot.z, 0,0,1);
        matrix.rotate(-modelRot.y, 0,1,0);
        matrix.rotate(-modelRot.x, 1,0,0);

        matrix.scale(modelScale.x,modelScale.y,modelScale.z);

        matrix.bind();
        tex.bind();
        mesh.render();
    }

    @Override
    public void renderShadow(TransformationMatrix matrix) {
        render(matrix);
    }
}
