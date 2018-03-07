package nullset.battle.fighters;

import mote4.util.matrix.TransformationMatrix;
import nullset.battle.Battle;
import nullset.battle.actions.DialogAction;
import nullset.rpg.Enemy;

public class EnemyFighter extends Fighter {

    private Enemy enemy;

    public EnemyFighter(Enemy e) {
        enemy = e;
    }

    @Override
    public void takeTurn() {
        Battle.getCurrent().addAction(new DialogAction());
    }

    @Override
    public void update() {

    }

    @Override
    public void render(int slot, TransformationMatrix matrix) {

    }
}
