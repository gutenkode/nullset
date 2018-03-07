package nullset.rpg;

/**
 * Something is pickupable if it can be put in an ItemPickup and collected by the player in the overworld.
 */
public interface Pickup {
    void pickup();
    String getSprite();
}
