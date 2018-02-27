package nullset.entities.components.behaviors;

import nullset.entities.Entity;
import mote4.scenegraph.Window;

public class TerminalBehavior extends Behavior {

    @Override
    public void onRoomInit() {
        entity.renderer.setEmissiveFrame(2);
        entity.renderer.setEmissiveMult(1);
    }

    @Override
    public void onCollide(Entity e) {

    }

    @Override
    public void update() {
        double time = Window.time();
        int i = 0;
        if (time%1 < .3)
            i = 1;
        entity.renderer.setEmissiveFrame(2+i);
    }
}
