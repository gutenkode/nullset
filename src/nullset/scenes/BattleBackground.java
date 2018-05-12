package nullset.scenes;

import mote4.util.matrix.Transform;
import mote4.util.shader.ShaderMap;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.builder.MeshBuilder;
import mote4.util.vertex.mesh.Mesh;
import mote4.util.vertex.mesh.MeshMap;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class BattleBackground {

    private static Mesh background;
    private static String bgName;

    static {
        float startPos = -3f, // start coordinates
                step = .25f;      // will add new grid coordinates at this interval
        float[][][] points = new float[25][25][3]; // how big the grid should be

        // construct grid of points for the mesh
        for (int x = 0;  x < points.length; x++)
            for (int y = 0;  y < points[0].length; y++) {
                float sineZdistort = (float)Math.sin(y)/4 -(float)Math.cos(x)/4;
                double dist = Math.pow((float)x/points.length*2-1, 2)+Math.pow((float)y/points.length*2-1, 2);
                float edgeZdistort = (float)Math.pow(dist, 5)*2;

                points[x][y] = new float[] {startPos+step*x, // x and y grid coordinates
                        startPos+step*y,
                        sineZdistort+edgeZdistort};
            }

        MeshBuilder mb = new MeshBuilder(3);
        mb.includeTexCoords(2);

        // convert list of points into a list of vertices and texture coordinates
        for (int x = 0;  x < points.length-1; x++)
            for (int y = 0;  y < points[0].length-1; y++) {
                mb.vertices(points[x][y]);
                mb.vertices(points[x+1][y]);
                mb.vertices(points[x][y+1]);

                mb.vertices(points[x][y+1]);
                mb.vertices(points[x+1][y]);
                mb.vertices(points[x+1][y+1]);

                float texScale = .125f;
                float texX1 = (x-1)*texScale;
                float texX2 = x*texScale;
                float texY1 = (y-1)*texScale;
                float texY2 = y*texScale;
                mb.texCoords(texX1,texY1, texX2,texY1, texX1,texY2,
                        texX1,texY2, texX2,texY1, texX2,texY2);
            }
        background = mb.constructVAO(GL11.GL_TRIANGLES);
    }

    private Transform trans;

    public BattleBackground() {
        trans = new Transform();
        bgName = "red";
    }

    public static void setBackground(String bg) {
        bgName = bg;
    }

    public void render(double time) {
        // background
        ShaderMap.use("battle_bg");
        trans.view.setIdentity();
        trans.view.translate(0,0, -3.8f); // pull camera back
        trans.view.rotate((float)time/10, 0, 0, 1);
        trans.bind();
        Uniform.vec("cycle", (float)time/10);
        TextureMap.bindUnfiltered("ui_bg_"+ bgName);
        background.render();

        // floor
        ShaderMap.use("texture");
        trans.view.setIdentity();
        trans.view.translate(0, -.75f, -3.5f); // pull camera back
        trans.view.translate(0,.35f,0); // slide up
        trans.view.rotate(-1.0f, 1, 0, 0); // rotate down
        trans.view.scale(2.0f, 1.5f, 2);
        trans.bind();
        TextureMap.bindUnfiltered("ui_floor_tile1");
        MeshMap.render("quad");

        trans.view.setIdentity();
    }

    public void framebufferResized(int width, int height) {
        trans.projection.setPerspective(width,height,.1f,100f,50f);
    }
}
