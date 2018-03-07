package nullset.entities;

import nullset.entities.components.behaviors.Behavior;
import nullset.entities.components.colliders.Collider;
import nullset.entities.components.renderers.Renderer;
import org.joml.Vector3f;
import nullset.room.Room;

import java.util.Optional;

public class Entity {

    private Vector3f pos, size;

    public final Room ROOM;
    public final Behavior behavior;
    public final Collider collider;
    public final Renderer renderer;

    protected Entity(Room rm, Behavior b, Collider c, Renderer r) {
        ROOM = rm;
        behavior = b;
        collider = c;
        renderer = r;
        behavior.setEntity(this);
        collider.setEntity(this);
        renderer.setEntity(this);

        pos = new Vector3f(0);
        size = new Vector3f(1);
    }

    public Entity createShallowCopy(Room rm) {
        Entity e = new Entity(rm, behavior, collider, renderer);
        e.pos.set(pos);
        e.size.set(size);
        return e;
    }

    public Vector3f getPos() { return pos; }
    public Vector3f getSize() { return size; }

    public Optional<Entity> hasCollision() {
        return ROOM.hasCollision(this);
    }
    /* *
     * Whether this entity projects a light.
     */
    /*
    public boolean hasLight() { return false; }
    public float[] lightPos() { return new float[] {posX,posY,tileHeight+.5f}; }
    public float[] lightColor() { return new float[] {1,1,1}; }
    */
}
