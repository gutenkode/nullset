package nullset.entities.components.behaviors;

import mote4.util.shader.Uniform;
import nullset.entities.Entity;
import mote4.scenegraph.Window;

public class WaterBehavior extends Behavior {

    private int width, height, baseHeight;
    public WaterBehavior(int w, int h, int b) {
        width = w;
        height = h;
        baseHeight = b;
    }

    @Override
    public void onRoomInit() {
        entity.renderer.setTilesetSize(1f/width,1f/height); // hackish, but efficient
    }

    @Override
    public void onCollide(Entity e) {

    }

    @Override
    public void update() {
        entity.getPos().y = .02f + baseHeight + (float)(Math.sin((float)Window.time()/2)+1)/16;
        entity.renderer.setFrame((float)Window.time()/50); // decent temporary effect
    }
}
