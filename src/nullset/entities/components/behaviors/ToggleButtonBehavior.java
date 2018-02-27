package nullset.entities.components.behaviors;

import nullset.entities.Entity;

public class ToggleButtonBehavior extends Behavior {

    private int index;
    private int cooldown, maxCooldown;

    public ToggleButtonBehavior(int i) {
        index = i;
        cooldown = 0;
        maxCooldown = 5;
    }

    @Override
    public void onRoomInit() {
        entity.renderer.setFrame(index*2);
        entity.renderer.setEmissiveFrame(index*2+1);
    }

    @Override
    public void onCollide(Entity e) {
        if (e.behavior instanceof PlayerBehavior) {
            if (cooldown <= 0) {
                boolean currentFlag = entity.ROOM.STATE.getFlagState(index);
                entity.ROOM.STATE.setFlagState(index, !currentFlag);
            }
            cooldown = maxCooldown;
        }
    }

    @Override
    public void update() {
        entity.renderer.setEmissiveMult((float)cooldown/maxCooldown);
        if (cooldown > 0)
            cooldown--;
    }
}
