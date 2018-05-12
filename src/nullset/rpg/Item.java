package nullset.rpg;

import nullset.battle.fighters.Fighter;
import nullset.main.Vars;
import nullset.scenes.UIScene;
import org.json.JSONArray;
import org.json.JSONObject;

import static nullset.rpg.AttribEnums.*;
import static nullset.rpg.AttribEnums.ItemType.CONSUMABLE;
import static nullset.rpg.AttribEnums.Stat.HP;
import static nullset.rpg.AttribEnums.Stat.MP;
import static nullset.rpg.AttribEnums.Stat.SP;

public enum Item implements Pickup {

    ITEM_KEYCARD, ITEM_KEYCARD2, ITEM_KEYCARD3, ITEM_KEYCARD4,
    ITEM_HP_HEAL,
    ITEM_MP_HEAL,
    ITEM_SP_HEAL,
    ITEM_BOMB,
    ITEM_BUFF_ATK;

    public final String itemName, description, spriteName;
    public final ItemType itemType;
    public final Effect ingameEffect, battleEffect;
    public final int[] attribs;

    Item() {
        JSONObject json = Vars.ITEM_JSON.getJSONObject(name());

        itemName = json.getString("itemName");
        description = json.getString("description");
        spriteName = json.getString("spriteName");
        itemType = ItemType.valueOf(json.getString("itemType"));
        ingameEffect = Effect.valueOf(json.getString("ingameEffect"));
        battleEffect = Effect.valueOf(json.getString("battleEffect"));
        if (json.has("attribs")) {
            JSONArray arr = json.getJSONArray("attribs");
            attribs = new int[arr.length()];
            for (int i = 0; i < attribs.length; i++)
                attribs[i] = arr.getInt(i);
        } else
            attribs = new int[0];
    }

    @Override
    public void pickup() {
        PlayerInventory.getInstance().addItem(this);
    }

    @Override
    public String getSprite() {
        return spriteName;
    }

    public void useInGame() {
        switch (this) {
            case ITEM_HP_HEAL:
            case ITEM_SP_HEAL:
            case ITEM_MP_HEAL:
                UIScene.getPauseUI().openDialog("Note: unimplemented.");
                break;
            default:
                throw new IllegalStateException("Attempted to use item "+this+" in overworld, but it has no defined action. Ingame effect: "+ingameEffect);
        }
    }

    public void useInBattle(Fighter... targets) {
        switch (this) {
            case ITEM_HP_HEAL:
                for (Fighter f : targets)
                    f.heal(HP, attribs[0]); // healing amount is stored in the attribs array
                break;
            case ITEM_SP_HEAL:
                for (Fighter f : targets)
                    f.heal(SP, attribs[0]);
                break;
            case ITEM_MP_HEAL:
                for (Fighter f : targets)
                    f.heal(MP, attribs[0]);
                break;
            case ITEM_BOMB:
            case ITEM_BUFF_ATK:
                UIScene.getBattleUI().openDialog("Note: unimplemented.");
                break;
            default:
                throw new IllegalStateException("Attempted to use item "+this+" in battle, but it has no defined action. Battle effect: "+battleEffect);
        }
    }

    public void consume() {
        if (itemType != CONSUMABLE)
            throw new IllegalStateException("Cannot consume a non-consumable item.");
        PlayerInventory.getInstance().removeItem(this);
    }
}
