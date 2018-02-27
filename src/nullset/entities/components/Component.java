package nullset.entities.components;

import nullset.entities.Entity;

public abstract class Component {
    protected Entity entity;
    public void setEntity(Entity e) { entity = e; }
    public Entity getEntity() { return entity; }
}
