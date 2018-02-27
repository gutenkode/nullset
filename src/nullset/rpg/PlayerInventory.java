package nullset.rpg;

import java.util.ArrayList;
import java.util.List;

import static nullset.rpg.InventoryItem.*;

public class PlayerInventory {

    private static PlayerInventory instance;
    public static PlayerInventory getInstance() {
        if (instance == null)
            instance = new PlayerInventory();
        return instance;
    }

    /////////////////////

    private List<InventoryItem> items;

    private PlayerInventory() {
        items = new ArrayList<>();
        items.add(KEYCARD);
    }

    public List<InventoryItem> getItems() {
        return items;
    }
}
