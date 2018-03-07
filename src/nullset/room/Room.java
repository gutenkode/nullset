package nullset.room;

import mote4.util.matrix.Transform;
import mote4.util.shader.ShaderMap;
import mote4.util.shader.Uniform;
import nullset.entities.Entity;
import nullset.entities.EntityFactory;
import nullset.entities.components.behaviors.RoomLinkBehavior;
import nullset.main.LazyList;
import mote4.util.matrix.TransformationMatrix;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.*;

public class Room {

    private static Room currentRoom;
    private static HashMap<String, Room> loadedRooms = new HashMap<>();

    public static Room getCurrent() { // TODO don't use a structure like this, could cause problems
        if (currentRoom == null) {
            throw new IllegalStateException();
            //currentRoom = new Room(START_ROOM, new GlobalState());
            //loadedRooms.put(START_ROOM, currentRoom);
        }
        return currentRoom;
    }
    public static Room loadNewRoom(String name) {
        if (!loadedRooms.containsKey(name)) {
            if (currentRoom == null)
                loadedRooms.put(name, new Room(name, new GlobalState()));
            else
                loadedRooms.put(name, new Room(name, currentRoom.STATE));
        }
        currentRoom = loadedRooms.get(name);

        currentRoom.entities.update();
        for (Entity e : currentRoom.entities)
            e.behavior.onRoomInit();

        return currentRoom;
    }

    ////////////

    public final String NAME;
    public final GlobalState STATE;
    public final TerrainData TERRAIN;

    private LazyList<Entity> entities;
    private Vector3f cameraPos;

    private Room(String name, GlobalState state) {
        NAME = name;
        TERRAIN = RoomLoader.getTerrain(name);
        STATE = state;
        entities = EntityFactory.deserializeEntities(RoomLoader.getEntities(name),TERRAIN,this);
        cameraPos = new Vector3f(TERRAIN.width/2,0,TERRAIN.height/2);
        //entities.add(EntityFactory.createPlayer(TERRAIN.width/2,TERRAIN.height/2,TERRAIN,this));

        entities.update();
        for (Entity e : entities)
            e.behavior.onRoomInit();

        // initialize shader uniforms
        ShaderMap.use("terrain_unlit");
        Uniform.sampler("tex_shade", 1, "tileset_shade", false);
        ShaderMap.use("terrain_lit");
        Uniform.sampler("tex_shade", 1, "tileset_shade", false);
    }

    public void update() {
        for (Entity e : entities)
            e.behavior.update();

        entities.update(); // add and remove entities here

        // TODO remove this brute-force collision checking, have entities check collisions only when they want to
        /*
        for (Entity e : entities)
            for (Entity e2 : entities)
                if (e.collider.collides(e2.collider)) {
                    e.behavior.onCollide(e2);
                    e2.behavior.onCollide(e);
                }*/
    }

    public void render(Transform trans) {
        ShaderMap.use("sprite_unlit");
        trans.bind();
        for (Entity e : entities)
            e.renderer.render(trans.model);
    }

    public void renderColliders(Transform trans) {
        ShaderMap.use("color");
        trans.bind();
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        for (Entity e : entities)
            e.collider.renderCollider(trans.model);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    public Optional<Entity> hasCollision(Entity testEntity) {
        for (Entity e : entities)
            if (testEntity.collider.collides(e.collider))
                return Optional.of(e);
        return Optional.empty();
    }
    public List<Entity> getEntities() { return entities; }

    public Entity getLinkTo(String roomName) {
        for (Entity e : entities)
            if (e.behavior instanceof RoomLinkBehavior) {
                RoomLinkBehavior b = (RoomLinkBehavior)e.behavior;
                if (b.ROOM_NAME.equals(roomName))
                    return e;
            }
        return null;
    }

    public void removeEntity(Entity e) {
        for (Entity e1 : entities)
            if (e1 == e) {
                entities.remove(e);
                return;
            }
        throw new IllegalArgumentException("Specified entity is not in this room.");
    }
    public Entity moveEntityTo(Entity e, Room rm) {
        for (Entity e1 : entities)
            if (e1 == e) {
                Entity newEntity = e.createShallowCopy(rm);
                rm.entities.add(newEntity);
                entities.remove(e);
                return newEntity;
            }
        throw new IllegalArgumentException("Specified entity is not in this room.");
    }

    public void applyViewPosition(TransformationMatrix matrix) {
        float panLeftRight = (-.5f+cameraPos.x/TERRAIN.width) *.5f;
        float panUpDown = (-cameraPos.y) *.04f;
        matrix.rotate(panUpDown, 1,0,0);
        matrix.rotate(panLeftRight, 0,1,0);
        matrix.translate(-cameraPos.x,-cameraPos.y,-cameraPos.z);
    }
    public void lookAt(Vector3f pos) {
        cameraPos.set(pos);
    }
}
