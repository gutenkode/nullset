package nullset.rpg;

import java.util.ArrayList;
import java.util.List;

import static nullset.rpg.Item.*;
import static nullset.rpg.Skill.*;
import static nullset.rpg.Mod.*;

public class PlayerInventory {

    private static PlayerInventory instance;
    public static PlayerInventory getInstance() {
        if (instance == null)
            instance = new PlayerInventory();
        return instance;
    }

    /////////////////////

    private List<Item> items;
    private List<Skill> skills;
    private List<Mod> mods;

    private PlayerInventory() {
        items = new ArrayList<>();
        skills = new ArrayList<>();
        mods = new ArrayList<>();

        items.add(ITEM_KEYCARD);
        items.add(ITEM_HP_HEAL);
        items.add(ITEM_BOMB);
        items.add(ITEM_BUFF_ATK);
        skills.add(SKILL_WEAK_FIRE);
        skills.add(SKILL_WEAK_LIGHT);
        skills.add(SKILL_WEAK_PHYS);
        mods.add(MOD_DAMAGE_BOOST);
    }

    public void addItem(Item item) {
        items.add(item);
    }
    public void removeItem(Item item) {
        items.remove(item);
    }
    public void addSkill(Skill skill) {
        skills.add(skill);
    }
    public void addMod(Mod mod) {
        mods.add(mod);
    }

    public List<Item> getItems() {
        return items;
    }
    public List<Skill> getSkills() {
        return skills;
    }
    public List<Mod> getMods() {
        return mods;
    }
}
