package nullset.entities.components.behaviors;

import mote4.scenegraph.Window;
import nullset.entities.Entity;
import nullset.entities.components.colliders.BoxCollider;
import nullset.entities.components.renderers.PlatformRenderer;

public class TogglePlatformBehavior extends Behavior {

    private int index, rotation;
    private boolean startInverted;

    private float position;

    public TogglePlatformBehavior(int i, int r, boolean inv) {
        index = i;
        rotation = r;
        startInverted = inv;
    }

    @Override
    public void onRoomInit() {
        boolean flag = entity.ROOM.STATE.getFlagState(index) ^ startInverted;

        if (flag)
            position = 0;
        else
            position = 1;

        entity.renderer.setEmissiveFrame(index+1);
        entity.renderer.setEmissiveMult(1);
        ((PlatformRenderer)entity.renderer).modelRot.y = rotation *(float)Math.PI/2;
    }

    @Override
    public void onCollide(Entity e) {

    }

    @Override
    public void update() {
        boolean flag = entity.ROOM.STATE.getFlagState(index) ^ startInverted;
        ((BoxCollider)entity.collider).setSolid(flag);
        if (flag) {
            if (position > 0)
                position -= Window.delta()*3;
            else
                position = 0;
        } else {
            if (position < 1)
                position += Window.delta()*3;
            else
                position = 1;
        }

        ((PlatformRenderer)entity.renderer).modelRot.x = position *-(float)Math.PI/2;
    }
}
