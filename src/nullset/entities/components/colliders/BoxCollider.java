package nullset.entities.components.colliders;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.vertex.mesh.MeshMap;
import org.joml.Vector3f;

public class BoxCollider extends Collider {

    public void setSolid(boolean s) {
        solid = s;
    }

    @Override
    public boolean collides(Collider c) {
        if (c == this)
            return false;
        if (c instanceof BoxCollider)
        {
            return collides(
                    entity.getPos(),entity.getSize(),
                    c.getEntity().getPos(), c.getEntity().getSize());
        }
        else if (c instanceof TerrainCollider)
        {
            TerrainCollider t = (TerrainCollider)c;
            // test the four bottom corners of the box
            // this works unless the entity's size is >1
            return t.isSolidAt(entity.getPos()) ||
                   t.isSolidAt(entity.getPos().x+entity.getSize().x, entity.getPos().y, entity.getPos().z) ||
                   t.isSolidAt(entity.getPos().x, entity.getPos().y, entity.getPos().z+entity.getSize().z) ||
                   t.isSolidAt(entity.getPos().x+entity.getSize().x, entity.getPos().y, entity.getPos().z+entity.getSize().z);
        }
        else
            return false;
    }

    private boolean collides(Vector3f p1, Vector3f s1, Vector3f p2, Vector3f s2) {
        return (p1.x < p2.x+s2.x && p1.x+s1.x > p2.x &&
                p1.y < p2.y+s2.y && p1.y+s1.y > p2.y &&
                p1.z < p2.z+s2.z && p1.z+s1.z > p2.z);
    }

    @Override
    public boolean isInside(Collider c) {
        if (c == this)
            return false;
        if (c instanceof BoxCollider)
        {
            return isInside(
                    entity.getPos(),entity.getSize(),
                    c.getEntity().getPos(), c.getEntity().getSize());
        }
        else
            return false;
    }

    private boolean isInside(Vector3f p1, Vector3f s1, Vector3f p2, Vector3f s2) {
        return (p1.x >= p2.x && p1.x+s1.x <= p2.x+s2.x &&
                p1.y >= p2.y && p1.y+s1.y <= p2.y+s2.y &&
                p1.z >= p2.z && p1.z+s1.z <= p2.z+s2.z);
    }

    @Override
    public void renderCollider(TransformationMatrix matrix) {
        matrix.setIdentity();
        matrix.translate(entity.getPos().x,entity.getPos().y,entity.getPos().z);
        matrix.translate(entity.getSize().x/2,entity.getSize().y/2,entity.getSize().z/2);
        matrix.scale(entity.getSize().x/2,entity.getSize().y/2,entity.getSize().z/2);
        matrix.bind();
        MeshMap.render("cube");
    }
}
