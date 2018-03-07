package nullset.main;

import mote4.util.FileIO;
import org.json.JSONObject;

public class Vars {

    public static final int
            UI_SCALE = 16 *1, // must be a multiple of 16
            UI_SCREEN_EDGE_PADDING = 8, // padding between the screen edge and edge-aligned menus
            SCENE_H = 1080/3;

    public static final String
            DEFAULT_TILESET = "tileset_brick";


    public static final JSONObject ITEM_JSON, SKILL_JSON, MOD_JSON, ENEMY_JSON;
    static {
        ITEM_JSON = new JSONObject(FileIO.getString("/res/files/items.json"));
        SKILL_JSON = new JSONObject(FileIO.getString("/res/files/skills.json"));
        MOD_JSON = new JSONObject(FileIO.getString("/res/files/mods.json"));
        ENEMY_JSON = new JSONObject(FileIO.getString("/res/files/enemies.json"));
    }
}
