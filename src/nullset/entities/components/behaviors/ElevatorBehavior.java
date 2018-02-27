package nullset.entities.components.behaviors;

import nullset.entities.Entity;
import mote4.scenegraph.Window;

public class ElevatorBehavior extends Behavior {

    private int baseHeight, moveHeight;
    private boolean startUp;

    public ElevatorBehavior(int baseHeight, int moveHeight, boolean startUp) {
        this.baseHeight = baseHeight;
        this.moveHeight = moveHeight;
        this.startUp = startUp;
    }

    @Override
    public void onRoomInit() {
        entity.renderer.setEmissiveMult(1);
    }

    @Override
    public void onCollide(Entity e) {

    }

    @Override
    public void update() {
        float cycle = (float)(Math.sin(Window.time())+1)/2;
        if (cycle < .001) // to make the elevator surface flush with the top and bottom terrain
            cycle = 0;
        else if (cycle > .999)
            cycle = 1;
        entity.getPos().y = baseHeight + moveHeight*cycle;

        double time = Window.time();
        int i = 1;
        if (time%1 < .3)
            i = 2;
        entity.renderer.setEmissiveFrame(i);
    }
}
