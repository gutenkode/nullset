package nullset.entities.components.colliders;

import nullset.entities.components.Component;
import mote4.util.matrix.TransformationMatrix;

public abstract class Collider extends Component {
    abstract public boolean collides(Collider c);
    abstract public boolean isInside(Collider c);
    public void renderCollider(TransformationMatrix matrix) {}

    protected boolean solid = true;
    public boolean isSolid() { return solid; }
}
