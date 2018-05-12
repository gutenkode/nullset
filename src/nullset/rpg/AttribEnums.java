package nullset.rpg;

/**
 * Enums used for defining the properties of items, skills, and other aspects of the game.
 */
public class AttribEnums {
    public enum Element { // standard spread of elements in an RPG
        PHYS,
        FIRE, ICE, ELEC,
        LIGHT, DARK,
        PSI, ACID,
        NONE; // "colorless" element
    }
    public enum Resistance { // types of elemental resistances
        N, // neutral
        RES,
        WEAK,
        NULL
    }
    public enum ItemType { // key items are stored in a separate list and cannot be tossed
        KEY, CONSUMABLE;
    }
    public enum Stat {
        HP, SP, MP, // standard health, stamina, mana
        STR, VIT, // physical attack and defense
        INT, WILL, // mental/magic attack and defense
        AGI, RES // speed/accuracy, and resistance to status effects and debuffs
    }
    public enum Status { // status ailments
        POISON,
        FATIGUE
    }
    public enum Effect { // the types of effects an item, skill, or attack can have
        ATTACK,
        HEAL_HP, HEAL_SP, HEAL_MP,
        BUFF,
        STATUS,
        NONE
    }
}
