package nullset.entities.components.behaviors;

import mote4.scenegraph.Window;
import nullset.entities.Entity;
import nullset.rpg.Pickup;

public class ItemPickupBehavior extends Behavior {

    private Pickup item;
    private boolean triggered;

    public ItemPickupBehavior(Pickup i) {
        item = i;
        triggered = false;
    }

    @Override
    public void onRoomInit() {

    }

    @Override
    public void onCollide(Entity e) {
        if (triggered)
            return;
        if (e.behavior instanceof PlayerBehavior) {
            item.pickup();
            entity.ROOM.removeEntity(entity);
            triggered = true;
        }
    }

    @Override
    public void update() {
        entity.renderer.modelPos.y = (float)(Math.sin(Window.time())+1)*.15f;
        entity.renderer.modelRot.y = (float)Window.time()*1.5f;
    }
}
