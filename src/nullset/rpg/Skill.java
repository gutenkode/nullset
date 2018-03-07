package nullset.rpg;

import nullset.main.Vars;
import org.json.JSONObject;

import static nullset.rpg.AttribEnums.*;

public enum Skill implements Pickup {

    SKILL_WEAK_PHYS,
    SKILL_WEAK_FIRE,
    SKILL_MED_FIRE,
    SKILL_WEAK_ELEC,
    SKILL_WEAK_ICE,
    SKILL_WEAK_LIGHT,
    SKILL_WEAK_DARK,
    SKILL_WEAK_HEAL,
    SKILL_BUFF_DEF,
    SKILL_POISON;

    public final String skillName, description, spriteName;
    public final Effect effect;
    public final String animation;
    public final Element element;
    public final int power, accuracy, cost, critrate;
    Skill() {
        JSONObject json = Vars.SKILL_JSON.getJSONObject(name());

        skillName = json.getString("skillName");
        description = json.getString("description");
        spriteName = json.getString("spriteName");
        effect = Effect.valueOf(json.getString("effect"));
        animation = "TODO"; // TODO
        element = Element.valueOf(json.getString("element"));
        power = accuracy = cost = critrate = 0;
    }

    @Override
    public void pickup() {
        PlayerInventory.getInstance().addSkill(this);
    }
    @Override
    public String getSprite() {
        return spriteName;
    }
}
