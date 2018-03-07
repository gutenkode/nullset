package nullset.rpg;

import nullset.main.Vars;
import org.json.JSONObject;

public enum Mod implements Pickup {

    MOD_DAMAGE_BOOST,
    MOD_EFFICIENCY,
    MOD_OVERCLOCK,
    MOD_BESERK,
    MOD_ACCURACY,
    MOD_MULTI_TARGET;

    public final String modName, description;
    Mod() {
        JSONObject json = Vars.MOD_JSON.getJSONObject(name());

        modName = json.getString("modName");
        description = json.getString("description");
    }

    @Override
    public void pickup() {
        PlayerInventory.getInstance().addMod(this);
    }
    @Override
    public String getSprite() {
        return "skill_mod";
    }
}