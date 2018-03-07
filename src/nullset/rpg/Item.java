package nullset.rpg;

import nullset.main.Vars;
import org.json.JSONArray;
import org.json.JSONObject;

import static nullset.rpg.AttribEnums.*;

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
}
