package nullset.room;

import mote4.util.vertex.builder.Attribute;
import mote4.util.vertex.builder.ModernMeshBuilder;
import mote4.util.vertex.mesh.Mesh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class TerrainMeshBuilder {

    // number of rows/columns in the tileset
    private static final float
            TILESET_COLUMNS = 8,
            TILESET_ROWS = 8,
    // size of one tile
            TILE_SIZE_X = 1/TILESET_COLUMNS,
            TILE_SIZE_Y = 1/TILESET_ROWS;

    private static TerrainData terrainData; // temporary variable, used when buildMesh is called
    public static Mesh buildMesh(TerrainData data) {
        terrainData = data;
        ModernMeshBuilder builder = new ModernMeshBuilder();

        Attribute vertAttrib = new Attribute(0, 3);
        builder.addAttrib(vertAttrib); // vertices

        Attribute shadeAttrib = new Attribute(1,2);
        builder.addAttrib(shadeAttrib); // tile shade

        Attribute texAttrib = new Attribute(2, 2);
        builder.addAttrib(texAttrib); // tex coords

        Attribute[] normAttrib = new Attribute[]
        {
            new Attribute(3, 3),
            new Attribute(4, 3),
            new Attribute(5, 3)
        };
        builder.addAttrib(normAttrib[0]); // tangent space matrix (normals)
        builder.addAttrib(normAttrib[1]);
        builder.addAttrib(normAttrib[2]);

        for (int i = 0; i < data.width; i++)
            for (int j = 0; j < data.height; j++)
                addVertex(vertAttrib, shadeAttrib, texAttrib, normAttrib, i,j);

        return builder.constructVAO(GL_TRIANGLES);
    }

    /**
     * Adds all mesh data for a tile.
     * @param vertAttrib
     * @param texAttrib
     * @param normAttrib
     * @param shadeAttrib
     * @param i Tile X value.
     * @param j Tile Y value.
     */
    private static void addVertex(Attribute vertAttrib, Attribute shadeAttrib, Attribute texAttrib, Attribute[] normAttrib, int i, int j) {
        int h = terrainData.heightData[i][j]; // height of this tile

        if (!getShapeValue(i,j,2)) { // TODO replace this with a better check, don't draw floor tiles here for diagonal walls
            // test if ground tiles should be drawn
            if (!getShapeValue(i, j, 1)) {
                // ground tile
                vertAttrib.add(
                        i, j, h,
                        i, j + 1, h,
                        i + 1, j, h,

                        i + 1, j, h,
                        i, j + 1, h,
                        i + 1, j + 1, h
                );
                addTex(texAttrib, i, j, 0, 0);
                addNormal(normAttrib, 0, 0, 1);
                addShade(shadeAttrib, i, j, 0);
            }
        }

        // test if wall tiles should be drawn
        if (!getShapeValue(i,j,0))
            return;

        if (getShapeValue(i,j,2)) {
            // this tile is a diagonal wall
            // for now, just draw the one angle for test purposes
            int h_back = terrainData.heightData[i][j-1];
            int h2 = Math.min(h_back, terrainData.heightData[i+1][j]);
            int h3 = Math.min(h_back, terrainData.heightData[i-1][j]);
            boolean flip = h2 < h3;
            if (flip)
                h2 = h3; // we're done with h3, rest of the code just references h2
            int diff = h2 - h;
            if (h2 > h) { // if the back side is higher than the front side
                for (int k = 0; k < diff; k++) {
                    if (flip) {
                        vertAttrib.add(
                                i, j+1, h2 - k,
                                i, j+1, h2 - (1 + k),
                                i + 1, j, h2 - k,

                                i + 1, j, h2 - k,
                                i, j+1, h2 - (1 + k),
                                i + 1, j, h2 - (1 + k)
                        );
                    } else {
                        vertAttrib.add(
                                i, j, h2 - k,
                                i, j, h2 - (1 + k),
                                i + 1, j + 1, h2 - k,

                                i + 1, j + 1, h2 - k,
                                i, j, h2 - (1 + k),
                                i + 1, j + 1, h2 - (1 + k)
                        );
                    }
                    if (k == 0)
                        addTex(texAttrib, i, j, 0, 2);
                    else
                        addTex(texAttrib, i, j, 1, 2);
                    if (k == diff - 1)
                        addShade(shadeAttrib, i, j, 4);
                    else
                        addShade(shadeAttrib, i, j, -1);
                    addNormal(normAttrib, -.707f, .707f, 0);
                }
            }  else { // front side is higher; flip h and h2 for proper floor rendering
                int temp = h;
                h = h2;
                h2 = temp;
            }
            // draw the cut upper floor tile
            if (flip) {
                vertAttrib.add(
                        i, j, h2,
                        i, j + 1, h2,
                        i + 1, j, h2,

                        i + 1, j, h,
                        i, j + 1, h,
                        i + 1, j + 1, h
                );
            } else {
                vertAttrib.add(
                        i+1, j, h2,
                        i, j, h2,
                        i+1, j+1, h2,

                        i+1, j+1, h,
                        i, j, h,
                        i, j+1, h
                );
            }
            addTex(texAttrib, i,j, 0,0);
            addNormal(normAttrib, 0,0,1);
            addShade(shadeAttrib, i,j, 0);
        } else {
            // this tile is a normal wall
            // back wall
            if (j > 0) { // don't try to draw back walls for back row
                int h2 = terrainData.heightData[i][j - 1];
                if (h2 > h) { // if the back tile is higher than this one
                    int diff = h2 - h;
                    for (int k = 0; k < diff; k++) {
                        vertAttrib.add(
                                i, j, h2 - k,
                                i, j, h2 - (1 + k),
                                i + 1, j, h2 - k,

                                i + 1, j, h2 - k,
                                i, j, h2 - (1 + k),
                                i + 1, j, h2 - (1 + k)
                        );
                        if (k == 0)
                            addTex(texAttrib, i, j, 0, 2);
                        else
                            addTex(texAttrib, i, j, 1, 2);
                        if (k == diff - 1)
                            addShade(shadeAttrib, i, j, 2);
                        else
                            addShade(shadeAttrib, i, j, -1);
                        addNormal(normAttrib, 0, 1, 0);
                    }
                }
            }
            // right wall
            if (i + 1 < terrainData.heightData.length) { // don't draw right walls for far right column
                int h2 = terrainData.heightData[i + 1][j];
                if (h2 > h) { // if the right tile is higher than this one
                    int diff = h2 - h;
                    for (int k = 0; k < diff; k++) {
                        vertAttrib.add(
                                i + 1, j, h2 - k,
                                i + 1, j, h2 - (1 + k),
                                i + 1, j + 1, h2 - k,

                                i + 1, j + 1, h2 - k,
                                i + 1, j, h2 - (1 + k),
                                i + 1, j + 1, h2 - (1 + k)
                        );
                        if (k == 0)
                            addTex(texAttrib, i, j, 0, 2);
                        else
                            addTex(texAttrib, i, j, 1, 2);
                        if (k == diff - 1)
                            addShade(shadeAttrib, i, j, 3);
                        else
                            addShade(shadeAttrib, i, j, -1);
                        addNormal(normAttrib, -1, 0, 0);
                    }
                }
            }
            // left wall
            if (i > 0) { // don't draw left walls for far left column
                int h2 = terrainData.heightData[i - 1][j];
                if (h2 > h) { // if the left tile is higher than this one
                    int diff = h2 - h;
                    for (int k = 0; k < diff; k++) {
                        vertAttrib.add(
                                i, j + 1, h2 - k,
                                i, j + 1, h2 - (1 + k),
                                i, j, h2 - k,
                                i, j, h2 - k,
                                i, j + 1, h2 - (1 + k),
                                i, j, h2 - (1 + k)
                        );
                        if (k == 0)
                            addTex(texAttrib, i, j, 0, 2);
                        else
                            addTex(texAttrib, i, j, 1, 2);
                        if (k == diff - 1)
                            addShade(shadeAttrib, i, j, 1);
                        else
                            addShade(shadeAttrib, i, j, -1);
                        addNormal(normAttrib, 1, 0, 0);
                    }
                }
            }
        }
    }

    /**
     * Adds texture coordinate data for a tile.
     * @param attrib The Attribute to add to.
     * @param i X index.
     * @param j Y index.
     * @param offset Vertical tiles to offset by, useful for rendering multi-tile textures.
     * @param texType Which "set" of texture coordinates to use, 0 for default, 2 for special.
     *                0 is currently used for floor textures and 2 for wall textures.
     */
    private static void addTex(Attribute attrib, int i, int j, int offset, int texType) {
        float indX = (int)(terrainData.tileData[i][j][texType]% TILESET_COLUMNS)* TILE_SIZE_X;
        float indY = (int)(terrainData.tileData[i][j][texType]/ TILESET_COLUMNS)* TILE_SIZE_Y;
        indY += offset*TILE_SIZE_Y;

        attrib.add(
                indX, indY,
                indX, indY+ TILE_SIZE_Y,
                indX+ TILE_SIZE_X, indY,

                indX+ TILE_SIZE_X, indY,
                indX, indY+ TILE_SIZE_Y,
                indX+ TILE_SIZE_X, indY+ TILE_SIZE_Y
        );
    }

    /**
     * Similar to addTex, but only a tile index is required.
     * Adds the "shade" texture, making the edges of platforms a darker color.
     * Any edge near a ledge or the edge of the map will have shade added to it.
     * @param attrib The Attribute to add to.
     * @param x X coordinate of the tile.
     * @param x X coordinate of the tile.
     * @param type The itemType of tile shade is being applied to.
     *             0: Floor tile
     *             1,2,3: Left,front,right wall tile
     *             4: Force full lower-wall shade
     *             Anything else: Use no shade (index 0)
     */
    private static void addShade(Attribute attrib, int x, int y, int type) {
        int ind = 0;

        switch (type)
        {
            case 0:
                // this is a floor tile; determine the correct shade
                // pattern based on surrounding tile heights
                int h = terrainData.heightData[x][y];

                boolean l = false;
                if (x > 0)
                    l = terrainData.heightData[x-1][y] == h;
                boolean r = false;
                if (x < terrainData.heightData.length-1)
                    r = terrainData.heightData[x+1][y] == h;
                boolean u = false;
                if (y > 0)
                    u = terrainData.heightData[x][y-1] == h;
                boolean d = false;
                if (y < terrainData.heightData[0].length-1)
                    d = terrainData.heightData[x][y+1] == h;

                // all sides
                if (!l && !r && !u && !d)
                    ind = 11;
                    // single side
                else if (!l && r && u && d)
                    ind = 3;
                else if (l && !r && u && d)
                    ind = 2;
                else if (l && r && !u && d)
                    ind = 1;
                else if (l && r && u && !d)
                    ind = 4;
                    // L sides
                else if (!l && r && !u && d)
                    ind = 7;
                else if (!l && r && u && !d)
                    ind = 6;
                else if (l && !r && !u && d)
                    ind = 8;
                else if (l && !r && u && !d)
                    ind = 5;
                    // parallel sides
                else if (!l && !r && u && d)
                    ind = 9;
                else if (l && r && !u && !d)
                    ind = 10;
                    // three sides
                else if (!l && !r && !u && d)
                    ind = 14;
                else if (!l && !r && u && !d)
                    ind = 15;
                else if (!l && r && !u && !d)
                    ind = 12;
                else if (l && !r && !u && !d)
                    ind = 13;
                else
                {
                    ind = 0;
                    /*
                    boolean ul = terrainData.heightData[x-1][y-1] == h;
                    boolean ur = terrainData.heightData[x+1][y-1] == h;
                    boolean dl = terrainData.heightData[x-1][y+1] == h;
                    boolean dr = terrainData.heightData[x+1][y+1] == h;

                    // single corners
                    if (!ul && ur && dl && dr)
                        ind = 11;
                    */
                }
                break;
            // wall tiles
            case 1: // left
            case 2: // back
            case 3: // right
                ind = 17;
                boolean leftLower = false, rightLower = false;
                switch (type) {
                    case 1: // left
                        if (y < terrainData.heightData[0].length-1) {
                            leftLower = terrainData.heightData[x][y + 1] < terrainData.heightData[x][y]; // if the tile to the left is lower than the current tile
                            leftLower = leftLower && terrainData.heightData[x - 1][y + 1] > terrainData.heightData[x][y]; // if there is no wall directly to the left, still draw the full edge regardless
                        }
                        if (y > 0) {
                            rightLower = terrainData.heightData[x][y - 1] < terrainData.heightData[x][y];
                            rightLower = rightLower && terrainData.heightData[x - 1][y - 1] > terrainData.heightData[x][y];
                        }
                        break;
                    case 2: // back
                        if (x > 0) {
                            leftLower = terrainData.heightData[x - 1][y] < terrainData.heightData[x][y];
                            leftLower = leftLower && terrainData.heightData[x - 1][y - 1] > terrainData.heightData[x][y];
                        }
                        if (x < terrainData.heightData.length-1) {
                            rightLower = terrainData.heightData[x + 1][y] < terrainData.heightData[x][y];
                            rightLower = rightLower && terrainData.heightData[x + 1][y - 1] > terrainData.heightData[x][y];
                        }
                        break;
                    case 3: // right
                        if (y > 0) {
                            leftLower = terrainData.heightData[x][y - 1] < terrainData.heightData[x][y];
                            leftLower = leftLower && terrainData.heightData[x + 1][y - 1] > terrainData.heightData[x][y];
                        }
                        if (y < terrainData.heightData[0].length-1) {
                            rightLower = terrainData.heightData[x][y + 1] < terrainData.heightData[x][y];
                            leftLower = leftLower && terrainData.heightData[x + 1][y + 1] > terrainData.heightData[x][y];
                        }
                        break;
                }
                if (leftLower && rightLower) {}
                else if (leftLower && !rightLower) {
                    ind--;
                } else if (!leftLower && rightLower) {
                    ind++;
                }
                break;
            case 4: // force full lower-wall shade
                ind = 17;
                break;
            default:
                // apply no shade
                break;
        }

        // calculate texture coordinates based on the value of 'ind'
        float indX = (int)(ind% TILESET_COLUMNS)* TILE_SIZE_X;
        float indY = (int)(ind/ TILESET_COLUMNS)* TILE_SIZE_Y;
        // and add it to the Attribute
        attrib.add(
                indX, indY,
                indX, indY+ TILE_SIZE_Y,
                indX+ TILE_SIZE_X, indY,

                indX+ TILE_SIZE_X, indY,
                indX, indY+ TILE_SIZE_Y,
                indX+ TILE_SIZE_X, indY+ TILE_SIZE_Y
        );
    }

    /**
     * Adds the normal matrix used for lighting and bumpmapping.
     * @param attrib The array of Attributes for normals.
     * @param x Normal X value.
     * @param y Normal Y value.
     * @param z Normal Z value.
     */
    private static void addNormal(Attribute[] attrib, float x, float y, float z) {
        for (int i = 0; i < 6; i++) {
            if (x == 0 && y == 1 && z == 0) {
                attrib[0].add(1,0,0);
                attrib[1].add(0,0,1);
                attrib[2].add(0,1,0);
            } else if (x == 0 && y == 0 && z == 1) {
                attrib[0].add(1,0,0);
                attrib[1].add(0,-1,0);
                attrib[2].add(0,0,1);
            } else if (x == 1 && y == 0 && z == 0) {
                attrib[0].add(0,-1,0);
                attrib[1].add(0,0,1);
                attrib[2].add(1,0,0);
            } else if (x == -1 && y == 0 && z == 0) {
                attrib[0].add(0,1,0);
                attrib[1].add(0,0,1);
                attrib[2].add(-1,0,0);
            } else {
                attrib[0].add(y,z,-x);
                attrib[1].add(x,z,-y);
                attrib[2].add(x,y,z);
            }
        }
    }

    /**
     * Test the shape value for a tile.
     * Tile values are stored in a single int as separate bits; index is the offset of the bit to test.
     * @return
     */
    private static boolean getShapeValue(int x, int y, int index) {
        return (terrainData.tileData[x][y][1] & (1 << index)) != 0;
    }
}
