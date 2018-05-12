package nullset.battle.actions;

/**
 * An action to be performed during a battle.
 */
public interface Action {
    void start();
    void act();
    boolean isDone();
}
