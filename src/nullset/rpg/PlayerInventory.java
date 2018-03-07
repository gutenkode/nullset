package nullset.rpg;

import java.util.ArrayList;
import java.util.List;

import static nullset.rpg.Item.*;

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
    }

    public void addItem(Item item) {
        items.add(item);
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
