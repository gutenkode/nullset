package nullset.main;

import mote4.scenegraph.Window;
import mote4.util.ErrorUtils;
import mote4.util.FileIO;
import mote4.util.shader.ShaderUtils;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.FontUtils;
import mote4.util.vertex.builder.StaticMeshBuilder;
import mote4.util.vertex.mesh.MeshMap;
import nullset.scenes.PostScene;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeLimits;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author Peter
 */
public class Main{

    public static void main(String[] args)
    {
        ErrorUtils.debug(true);
        Window.initWindowedPercent(.75, 16.0/9.0);
        //Window.initFullscreen();

        Window.setVsync(true);
        Window.setTitle("Nullset");
        Window.displayDeltaInTitle(true);
        //Window.setCursorHidden(true);

        //glfwSetWindowAspectRatio(Window.getWindowID(), 16, 9);
        glfwSetWindowSizeLimits(Window.getWindowID(), 640, 480, GLFW_DONT_CARE, GLFW_DONT_CARE);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        loadResources();

        Window.addLayer(RootLayer.getInstance());
        Window.addScene(PostScene.getInstance());
        Window.loop();
    }

    private static void loadResources()
    {
        // TODO fix this mess
        // allow the moteEngine module to read the packages in this module
        for (String pn : Main.class.getModule().getPackages())
            Main.class.getModule().addOpens(pn, FileIO.class.getModule());
        // specify the module to load resources from
        // since the load operation is done in the FileIO class in the moteEngine module, the moteEngine module needs to be able to read this project's module AND have a reference to it to find the resources
        FileIO.setResourceModule(Main.class.getModule());

        /////////////////

        TextureMap.loadIndex("index.txt");
        ShaderUtils.loadIndex("index.txt");
        //AudioLoader.loadIndex("index.txt");

        MeshMap.add(StaticMeshBuilder.loadQuadMesh(), "quad");
        MeshMap.add(StaticMeshBuilder.constructVAOFromOBJ("cube", false), "cube");
        MeshMap.add(StaticMeshBuilder.constructVAOFromOBJ("barrel", false), "barrel");
        MeshMap.add(StaticMeshBuilder.constructVAOFromOBJ("pipe", false), "pipe");
        MeshMap.add(StaticMeshBuilder.constructVAOFromOBJ("hexahedron", false), "hexahedron");

        FontUtils.loadMetric("font/6px/6px_metric","font_6px");
        FontUtils.loadMetric("font/misterpixel/misterpixel_metric","font_ui");
    }
}