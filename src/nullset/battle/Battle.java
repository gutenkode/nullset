package nullset.battle;

import mote4.scenegraph.Window;
import mote4.util.matrix.Transform;
import mote4.util.shader.ShaderMap;
import nullset.battle.actions.Action;
import nullset.battle.fighters.EnemyFighter;
import nullset.battle.fighters.Fighter;
import nullset.battle.fighters.PlayerFighter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Battle {

    private static Battle currentBattle;
    private Action lastPerformedAction;

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

    private List<Fighter> fighters, enemies, players;
    private Fighter activeFighter;
    private Queue<Action> actions;
    private double advanceDelay = 0;

    private Battle(List<Enemy> enemyList) {
        actions = new LinkedList<>();
        fighters = new ArrayList<>();
        enemies = new ArrayList<>();
        players = new ArrayList<>();
        players.add(new PlayerFighter());
        for (Enemy e : enemyList)
            enemies.add(new EnemyFighter(e,enemies.size()));
        fighters.addAll(players);
        fighters.addAll(enemies);
        activeFighter = fighters.get(fighters.size()-1); // TODO sort fighters by speed
    }

    public void update() {
        if (advanceDelay > 0)
        {
            // global delay for actions, prevent actions from speeding through every frame
            advanceDelay -= Window.delta();
        }
        else if (!actions.isEmpty())
        {
            // process actions until they are all done
            if (actions.peek() != lastPerformedAction) {
                // when a new action is first called, call start() once as well as act()
                actions.peek().start();
                lastPerformedAction = actions.peek();
            }
            actions.peek().act();
            if (actions.peek().isDone()) {
                actions.remove();
                advanceDelay = .3; // time in seconds
            }
        }
        else
        {
            // if there are no more actions in the queue to process, advance to the next fighter
            // TODO this currently skips the first fighter in the list at the start of battle
            // temporary fix is setting the active fighter to the last one in the list at the start of battle
            endTurn();
            activeFighter.takeTurn(this);
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

    public List<Fighter> getAllFighters() { return fighters; }
    public List<Fighter> getAllEnemies() { return enemies; }
    public List<Fighter> getAllPlayers() { return players; }

    /**
     * End the current fighter's turn and advance to the next one.
     */
    private void endTurn() {
        activeFighter = fighters.get((fighters.indexOf(activeFighter)+1)%fighters.size());
    }
}
