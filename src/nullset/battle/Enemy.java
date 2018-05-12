package nullset.battle;

import nullset.main.Vars;
import nullset.rpg.AttribEnums;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores enemy data parsed from the json file.
 */
public enum Enemy {

    ENEMY_SLIME,
    ENEMY_NOISE,
    ENEMY_BOSS;

    public final String enemyName, spriteName,
            encounterString, deathString,
            background, music;
    public final int[] spriteFrames;
    public final FighterStats stats;

    Enemy() {
        JSONObject json = Vars.ENEMY_JSON.getJSONObject(name());

        enemyName = json.getString("enemyName");
        spriteName = json.getString("spriteName");
        encounterString = json.getString("encounterString");
        deathString = json.getString("deathString");
        background = json.getString("background");
        music = json.getString("music");

        JSONArray arr = json.getJSONArray("spriteFrames");
        spriteFrames = new int[arr.length()];
        int j = 0;
        for (Object o : arr) {
            int i = (Integer)o;
            spriteFrames[j] = i;
            j++;
        }

        Map<AttribEnums.Stat,Integer> statMap = new HashMap<>();
        Map<AttribEnums.Element,AttribEnums.Resistance> resistanceMap = new HashMap<>();

        JSONObject statsObj = json.getJSONObject("base");
        for (String key : statsObj.keySet())
            statMap.put(AttribEnums.Stat.valueOf(key), statsObj.getInt(key));

        JSONObject resObj = json.getJSONObject("resistance");
        for (String key : resObj.keySet())
            resistanceMap.put(AttribEnums.Element.valueOf(key), AttribEnums.Resistance.valueOf(resObj.getString(key)));

        stats = new FighterStats(statMap,resistanceMap);
    }
}
