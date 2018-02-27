package nullset.rpg;

public enum InventoryItem {
    KEYCARD;

    public final String PRETTY_NAME;
    InventoryItem() {
        PRETTY_NAME = this.name();
    }
}
