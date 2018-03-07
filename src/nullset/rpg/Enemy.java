package nullset.rpg;

import nullset.main.Vars;
import org.json.JSONObject;

public enum Enemy {

    ENEMY_SLIME,
    ENEMY_NOISE,
    ENEMY_BOSS;

    public final String enemyName, spriteName;
    Enemy() {
        JSONObject json = Vars.ENEMY_JSON.getJSONObject(name());

        enemyName = json.getString("enemyName");
        spriteName = json.getString("spriteName");
    }
}
