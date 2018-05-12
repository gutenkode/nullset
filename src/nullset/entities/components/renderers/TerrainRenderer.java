package nullset.entities.components.renderers;

import mote4.util.matrix.Transform;
import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.ShaderMap;
import mote4.util.shader.Uniform;
import mote4.util.texture.Texture;
import mote4.util.vertex.mesh.Mesh;
import mote4.util.vertex.mesh.MeshMap;
import nullset.room.TerrainMeshBuilder;
import nullset.room.TerrainData;

public class TerrainRenderer extends Renderer {

    private Mesh mesh;
    private Texture tex;
    private TerrainData data;

    public TerrainRenderer(TerrainData data, Texture t) {
        tex = t;
        this.data = data;
        String key = "terrain_"+data.roomName;
        if (!MeshMap.contains(key)) {
            MeshMap.add(TerrainMeshBuilder.buildMesh(data), key);
        }
        mesh = MeshMap.get(key);
    }

    @Override
    public void render(TransformationMatrix matrix) {
        String shader = ShaderMap.getCurrentName();
        ShaderMap.use("terrain_unlit");
        Uniform.vec("mapSize",data.width,data.height);
        Transform.rebindCurrentTransform();
        //bindUniforms();
        matrix.setIdentity();

        matrix.translate(0,0,0);
        matrix.rotate((float)Math.PI/2,1,0,0);
        matrix.scale(1,1,-1);

        matrix.bind();
        tex.bind();
        mesh.render();
        ShaderMap.use(shader);
    }

    @Override
    public void renderShadow(TransformationMatrix matrix) {
        render(matrix);
    }
}