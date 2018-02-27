package nullset.entities.components.colliders;

import org.joml.Vector3f;
import nullset.rooms.TerrainData;

public class TerrainCollider extends Collider {

    private TerrainData data;

    public TerrainCollider(TerrainData d) {
        data = d;
    }

    @Override
    public boolean collides(Collider c) {
        return false;
    }

    @Override
    public boolean isInside(Collider c) {
        return false;
    }

    public boolean isSolidAt(Vector3f pos) {
        return data.heightAt(pos.x,pos.z) > pos.y;
    }
    public boolean isSolidAt(float x, float y, float z) {
        return data.heightAt(x,z) > y;
    }
    public boolean isTileSolid(int x, int y, int z) {
        return data.heightAt(x,z) > y;
    }
}
