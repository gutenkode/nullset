package nullset.battle;

import mote4.util.matrix.Transform;
import mote4.util.shader.ShaderMap;
import nullset.battle.actions.Action;
import nullset.battle.fighters.EnemyFighter;
import nullset.battle.fighters.Fighter;
import nullset.battle.fighters.PlayerFighter;
import nullset.rpg.Enemy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Battle {

    private static Battle currentBattle;

    public static Battle getCurrent() {
        if (currentBattle == null)
            throw new IllegalStateException();
        return currentBattle;
    }
    public static Battle loadNewBattle(List<Enemy> enemies) {
        currentBattle = new Battle(enemies);
        return currentBattle;
    }

    ////////////

    private List<Fighter> fighters;
    private Fighter activeFighter;
    private Queue<Action> actions;

    private Battle(List<Enemy> enemies) {
        actions = new LinkedList<>();
        fighters = new ArrayList<>();
        fighters.add(new PlayerFighter());
        for (Enemy e : enemies)
            fighters.add(new EnemyFighter(e));
        activeFighter = fighters.get(0); // TODO sort fighters by speed
    }

    public void update() {
        if (!actions.isEmpty())
        {
            // process actions until they are all done
            actions.peek().act();
            if (actions.peek().isDone())
                actions.remove();
        }
        else
        {
            // if there are no more actions in the queue to process, advance to the next fighter
            // TODO this currently skips the first fighter in the list at the start of battle
            endTurn();
            activeFighter.takeTurn();
        }
        for (Fighter f : fighters)
            f.update();
    }

    public void render(Transform trans) {
        ShaderMap.use("sprite_unlit");
        trans.bind();
        int slot = 0;
        for (Fighter f : fighters) {
            f.render(slot, trans.model);
            slot++; // TODO care about player vs. enemy fighters
        }
    }

    /**
     * Add an action to perform in the battle.
     * Fighters add actions they wish to perform on their turn, which are then executed in order.
     * @param action
     */
    public void addAction(Action action) {
        actions.add(action);
    }
    /**
     * End the current fighter's turn and advance to the next one.
     */
    private void endTurn() {
        activeFighter = fighters.get((fighters.indexOf(activeFighter)+1)%fighters.size());
    }
}
