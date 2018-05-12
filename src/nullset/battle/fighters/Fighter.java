package nullset.battle.fighters;

import mote4.util.matrix.TransformationMatrix;
import nullset.battle.Battle;
import nullset.battle.FighterStats;
import nullset.rpg.AttribEnums;
import nullset.rpg.AttribEnums.Element;

import static nullset.rpg.AttribEnums.Stat.*;

public abstract class Fighter {

    protected int hp,sp,mp;
    protected FighterStats stats;
    public Fighter(FighterStats stats) {
        this.stats = stats;
        hp = stats.base.getOrDefault(HP,0);
        sp = stats.base.getOrDefault(SP,0);
        mp = stats.base.getOrDefault(MP,0);
    }

    public void damage(double skillPower, double baseAtk, Element element, boolean isPhysical) {
        hp -= stats.damage(skillPower, baseAtk, element, isPhysical);
        hp = Math.max(hp,0);
    }

    public void heal(AttribEnums.Stat stat, int amount) {
        switch (stat) {
            case HP:
                hp += amount;
                hp = Math.min(hp, stats.base.get(HP));
                break;
            case SP:
                sp += amount;
                sp = Math.min(sp, stats.base.get(SP));
                break;
            case MP:
                mp += amount;
                mp = Math.min(mp, stats.base.get(MP));
                break;
            default:
                throw new IllegalArgumentException("Cannot heal stat of type: "+stat);
        }
    }

    public FighterStats getStats() { return stats; }

    /////////////////////

    /**
     * Called once when this Fighter's turn starts.
     */
    public abstract void takeTurn(Battle battle);

    /**
     * Called every frame during battle.
     */
    public abstract void update();

    /**
     * Render this Fighter in the position specified.
     * @param slot
     * @param matrix
     */
    public abstract void render(int slot, TransformationMatrix matrix);

    /**
     * Player-facing name of this Fighter.
     */
    public abstract String getName();

    public void destroy() {}
}
