package nullset.entities.components.behaviors;

import mote4.scenegraph.Window;
import nullset.entities.Entity;
import nullset.entities.components.colliders.TerrainCollider;
import nullset.main.Input;
import org.joml.Vector3f;

import java.util.Optional;

public class PlayerBehavior extends Behavior {

    private final float WALK_SPEED = .005f, GRAVITY = -.007f;
    private Vector3f vel;

    public PlayerBehavior() {
        vel = new Vector3f(0);
    }

    @Override
    public void onRoomInit() {

    }

    @Override
    public void onCollide(Entity e) {

    }

    @Override
    public void update()
    {
        // set animation frame
        entity.renderer.setFrame((int)(Window.time()*15));

        // gravity and y collision, TODO scale by delta
        vel.y += GRAVITY;
        entity.getPos().y += vel.y;
        for (Entity e : entity.ROOM.getEntities()) {
            if (entity.collider.collides(e.collider)) {
                e.behavior.onCollide(entity);
                if (e.collider instanceof TerrainCollider) {
                    entity.getPos().y -= vel.y;
                    vel.y = 0;
                } else if (e.collider.isSolid()) {
                    entity.getPos().y = e.getPos().y + e.getSize().y;
                    vel.y = 0; // TODO, this should be set to the entity's vertical velocity, optimally...
                }
            }
        }

        // move left/right and x collision
        if (Input.getInstance().isKeyDown(Input.Key.LEFT))
            vel.x -= WALK_SPEED;
        else if (Input.getInstance().isKeyDown(Input.Key.RIGHT))
            vel.x += WALK_SPEED;

        entity.getPos().x += vel.x;
        Optional<Entity> collision = entity.hasCollision();
        if (collision.isPresent()) {
            collision.get().behavior.onCollide(entity);
            if (collision.get().collider.isSolid()) {
                entity.getPos().x -= vel.x;
                vel.x = 0;
            }
        }

        // move forward/back and z collision
        if (Input.getInstance().isKeyDown(Input.Key.UP))
            vel.z -= WALK_SPEED;
        else if (Input.getInstance().isKeyDown(Input.Key.DOWN))
            vel.z += WALK_SPEED;

        entity.getPos().z += vel.z;
        collision = entity.hasCollision();
        if (collision.isPresent()) {
            collision.get().behavior.onCollide(entity);
            if (collision.get().collider.isSolid()) {
                entity.getPos().z -= vel.z;
                vel.z = 0;
            }
        }

        // dampen velocities, TODO scale by delta
        vel.mul(.9f,.96f,.9f);

        // make the room's camera follow the player
        Vector3f lookAtPos = new Vector3f();
        entity.getPos().add(entity.getSize().x/2,0,entity.getSize().z/2, lookAtPos);
        entity.ROOM.lookAt(lookAtPos);

        /*
            Collision detection logic:
                If the player intersects with any solid object, immediately fail.
                For the four bottom corners of the player's hitbox: (or the four vertical lines, to prevent edge case failures)
                    (better idea, only do one test in the center of the player's hitbox, e.g. let the player walk halfway over ledges)
                    If there's a dropoff to the next point of collision greater than a certain threshold, fail.
                    e.g. the player can't walk off cliffs taller than a certain height.

             This algorithm treats all solid objects the same, including the terrain.
             With proper care, this should make using elevators and moving platforms very fluid,
             and remove almost all special condition handling.
         */
    }
}
