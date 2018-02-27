package nullset.rooms;

public class TerrainData {

    public final String roomName, tileset;
    public final int width, height;
    protected final int[][][] tileData;
    protected final int[][] heightData;

    /**
     * Data describing the heightmap of a ROOM.
     * @param tileData Stores data for texture coordinates and other tile data, in the following layers:
     *                      texture coords,
     *                      tile "shape",
     *                      detail texture coords
     *                 Tile shape is one of (0,1,2,3):
     *                      0,1=draw ground, 2,3=no ground,
     *                      0,2=no wall,     1,3=draw wall
     * @param heightData Defines the height of each tile.
     */
    protected TerrainData(String r, String t, int[][][] tileData, int[][] heightData) {
        width = tileData.length;
        height = tileData[0].length;

        roomName = r;
        tileset = t;

        this.tileData = tileData;
        this.heightData = heightData;
    }

    public int heightAt(float x, float y) {
        int testX = (int)Math.max(0,Math.min(width-1,x));
        int testY = (int)Math.max(0,Math.min(height-1,y));
        return heightData[testX][testY];
    }
}
