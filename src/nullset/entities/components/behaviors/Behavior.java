package nullset.entities.components.behaviors;

import nullset.entities.Entity;
import nullset.entities.components.Component;

public abstract class Behavior extends Component {
    abstract public void onRoomInit();
    abstract public void onCollide(Entity e);
    abstract public void update();
}
