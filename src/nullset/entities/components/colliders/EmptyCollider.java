package nullset.entities.components.colliders;

public class EmptyCollider extends Collider {

    @Override
    public boolean isSolid() { return false; }

    @Override
    public boolean collides(Collider c) {
        return false;
    }

    @Override
    public boolean isInside(Collider c) {
        return false;
    }
}
