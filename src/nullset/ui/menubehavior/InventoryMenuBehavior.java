package nullset.ui.menubehavior;

import nullset.rpg.Item;
import nullset.rpg.PlayerInventory;
import nullset.ui.UIHandler;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryMenuBehavior extends MenuBehavior {

    public InventoryMenuBehavior() {
        title = "Inventory";

        // TODO this is probably a trash way of producing a list of names for items, but
        List<Item> items = PlayerInventory.getInstance().getItems();
        elements = new String[items.size()+1];
        int ind = 0;
        for (String s : items.stream().map(i -> "x1 "+i.itemName).collect(Collectors.toList())) {
            elements[ind] = s;
            ind++;
        }
        elements[elements.length-1] = "Back";
    }

    @Override
    public void onAction() {
        switch (elements[cursorPos]) {
            case "Back":
                onClose();
                break;
            default:
                break;
        }
    }
}
