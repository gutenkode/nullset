package nullset.battle.fighters;

import mote4.util.matrix.TransformationMatrix;

public abstract class Fighter {
    /**
     * Called once when this Fighter's turn starts.
     */
    public abstract void takeTurn();

    /**
     * Called every frame during battle.
     */
    public abstract void update();

    /**
     * Render this Fighter in the position specified.
     * @param slot
     * @param matrix
     */
    public abstract void render(int slot, TransformationMatrix matrix);
}
