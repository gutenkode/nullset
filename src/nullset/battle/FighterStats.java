package nullset.battle;

import static nullset.rpg.AttribEnums.*;
import static nullset.rpg.AttribEnums.Resistance.N;
import static nullset.rpg.AttribEnums.Stat.*;

import java.util.HashMap;
import java.util.Map;

public class FighterStats {

    private static FighterStats playerStats;
    public static FighterStats getPlayerStats() {
        if (playerStats == null) {
            Map<Stat,Integer> base = new HashMap<>();
            base.put(HP,100);
            base.put(SP,100);
            base.put(MP,100);
            base.put(STR,30);
            base.put(VIT,30);
            base.put(INT,20);
            base.put(WILL,20);
            base.put(AGI,30);
            base.put(RES,20);
            Map<Element,Resistance> resistance = new HashMap<>();
            playerStats = new FighterStats(base,resistance);
        }
        return playerStats;
    }

    public final Map<Stat,Integer> base;
    public final Map<Element,Resistance> resistance;

    protected FighterStats(Map<Stat,Integer> base, Map<Element,Resistance> resistance) {
        this.base = base;
        this.resistance = resistance;
    }

    public double damage(double skillPower, double baseAtk, Element element, boolean isPhysical) {
        // damage = atk * atk / (atk + def)
        // defense of 0 = full attack damage
        // defense equal to attack = half attack damage

        // damage = skill * atk / ( (skill+atk)/2 + def)
        // this enables the use of a second stat for skill power
        // a powerful skill can be used, but without the stat for it, its effectiveness is drastically cut, up to around half effectiveness
        // inversely, someone with a high base stat can use a weak skill more effectively, roughly doubling its power at high base
        // items and attacks that don't depend on a stat can just use the skill power for both parameters

        double def;
        if (isPhysical)
            def = base.getOrDefault(VIT,0);
        else
            def = base.getOrDefault(WILL,0);

        switch (resistance.getOrDefault(element,N)) {
            case RES:
                baseAtk /= 2;
                def *= 2;
                break;
            case WEAK:
                baseAtk *= 2;
                def /= 2;
                break;
            case NULL:
                baseAtk = 0;
                break;
        }

        double damage = (skillPower * baseAtk) / ( (skillPower+baseAtk)/2 + def +.0000001); // tweaked to avoid potential divide-by-zero errors
        return damage;
    }
}
