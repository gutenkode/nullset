package nullset.ui.menubehavior;

import nullset.battle.Battle;
import nullset.battle.fighters.Fighter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Peter on 10/16/16.
 */
public class TargetSelectBehavior extends MenuBehavior {

    private Consumer callback;
    private ArrayList<Fighter> fighters;
    private boolean targetAll;

    public TargetSelectBehavior(Consumer<Fighter[]> cb, boolean targetAll, boolean showEnemies, boolean showPlayers) {
        title = "Select Target";
        callback = cb;
        this.targetAll = targetAll;

        if (!showEnemies && !showPlayers)
        {
            elements = new String[]{"None", "Back"};
        }
        else if (targetAll)
        {
            if (showEnemies && showPlayers) {
                elements = new String[]{"All", "Back"};
            } else if (showEnemies) {
                elements = new String[]{"All Enemies", "Back"};
            } else { // must be showPlayers
                elements = new String[]{"All Party Members", "Back"};
            }
        }
        else
        {
            fighters = new ArrayList<>();
            if (showEnemies)
                fighters.addAll(Battle.getCurrent().getAllEnemies());
            if (showPlayers)
                fighters.addAll(Battle.getCurrent().getAllPlayers());

            elements = new String[fighters.size()+1];
            for (int i = 0; i < elements.length-1; i++) {
                elements[i] = fighters.get(i).getName();
            }
            elements[elements.length-1] = "Back";
        }
    }

    @Override
    public void onAction() {
        if (elements[cursorPos].equals("Back"))
        {
            handler.closeMenu(this);
            return;
        }
        else if (targetAll)
        {
            List<Fighter> f;
            switch (elements[cursorPos]) {
                case "All":
                    f = Battle.getCurrent().getAllFighters();
                    break;
                case "All Enemies":
                    f = Battle.getCurrent().getAllEnemies();
                    break;
                case "All Party Members":
                    f = Battle.getCurrent().getAllPlayers();
                    break;
                default:
                    throw new IllegalStateException();
            }
            callback.accept(f.toArray(new Fighter[f.size()]));
        }
        else
        {
            callback.accept(new Fighter[] {fighters.get(cursorPos)});
        }
    }

    @Override
    protected void onHighlight() {
        // TODO highlight selected enemies here
    }
}
