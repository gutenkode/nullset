package nullset.entities.components.behaviors;

import nullset.entities.Entity;
import nullset.main.RootLayer;
import nullset.rpg.Enemy;

import java.util.Collections;

public class EnemyBehavior extends Behavior {

    private Enemy enemy;
    private boolean triggered;

    public EnemyBehavior(Enemy e) {
        enemy = e;
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
            RootLayer.getInstance().loadBattle(Collections.singletonList(enemy));
            entity.ROOM.removeEntity(entity);
            triggered = true;
        }
    }

    @Override
    public void update() {

    }
}
