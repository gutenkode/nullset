package nullset.entities.components.behaviors;

import nullset.entities.Entity;
import nullset.room.Room;

public class RoomLinkBehavior extends Behavior {

    public final String ROOM_NAME;
    private int rotation;

    public RoomLinkBehavior(String roomName, int rotation) {
        this.ROOM_NAME = roomName;
        this.rotation = rotation;
    }

    @Override
    public void onRoomInit() {

    }

    @Override
    public void onCollide(Entity e) {
        if (e.behavior instanceof PlayerBehavior) {
            // make the new room current and create a player in it, and delete the old player entity
            Room oldRoom = Room.getCurrent();
            Room newRoom = Room.loadNewRoom(ROOM_NAME);
            Entity newPlayer = oldRoom.moveEntityTo(e, newRoom);

            // find the reciprocating link in the new room and put the player at its location
            Entity otherLink = newRoom.getLinkTo(oldRoom.NAME);
            newPlayer.getPos().set(otherLink.getPos());
            newPlayer.getPos().add(0.5f-newPlayer.getSize().x/2f,0,0.5f-newPlayer.getSize().z/2f); // center the player

            // place the player IN FRONT of the link, not IN the link
            float offsetX = entity.getSize().x/2+newPlayer.getSize().x/2;
            float offsetZ = entity.getSize().z/2+newPlayer.getSize().z/2;
            switch (rotation) {
                case 0:
                    newPlayer.getPos().add(offsetX,0,0);
                    break;
                case 1:
                    newPlayer.getPos().add(0,0,-offsetZ);
                    break;
                case 2:
                    newPlayer.getPos().add(-offsetX,0,0);
                    break;
                case 3:
                    newPlayer.getPos().add(0,0,offsetZ);
                    break;
                default:
                    throw new IllegalStateException("Invalid rotation value: "+rotation);
            }
        }
    }

    @Override
    public void update() {

    }
}
