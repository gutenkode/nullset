package nullset.rpg;

/**
 * Enums used for defining the properties of items, skills, and other aspects of the game.
 */
public class AttribEnums {
    public enum Element { // standard spread of elements in an RPG
        PHYS, BOMB,
        FIRE, ICE, ELEC,
        LIGHT, DARK,
        PSI, ACID,
        NONE;
    }
    public enum ItemType { // key items are stored in a separate list and cannot be tossed
        KEY, CONSUMABLE;
    }
    public enum Stats {
        HEALTH, STAMINA, MANA;
    }
    public enum Effect { // the types of effects an item, skill, or attack can have
        HEAL_HP, HEAL_MP, HEAL_SP,
        BUFF_ATK, BUFF_DEF, BUFF_SPD,
        ATTACK, ATK_PERCENT, ATK_OHKO,
        NONE, GIVE_STATUS, HEAL_STATUS;
    }
}
