package nullset.room;

import mote4.scenegraph.Window;
import mote4.util.FileIO;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import static nullset.main.Vars.DEFAULT_TILESET;

/**
 * Utility class for loading map files into the game.
 * @author Peter
 */
public class RoomLoader {

    public static final String FILE_EXTENSION = ".rf2";
    private static String levelPath = "_mylevel1/"; // the folder (or folders) the set of levels is in
    private static HashMap<String, TerrainData> terrainMap = new HashMap<>();
    private static HashMap<String, List<String>> entityMap = new HashMap<>();

    // temporary variables used during map loading
    private static int mapWidth, mapHeight;
    private static String fileName, tileset;

    /**
     * Return a previously loaded RoomData object.
     * If the requested data is not loaded, an attempt will
     * be made to load it and then return the result.
     */
    public static TerrainData getTerrain(String name) {
        TerrainData d = terrainMap.get(name);
        if (d == null) {
            load(name);
            return terrainMap.get(name);
        }
        return d;
    }
    public static List<String> getEntities(String name) {
        List<String> e = entityMap.get(name);
        if (e == null) {
            load(name);
            return entityMap.get(name);
        }
        return e;
    }

    private static void load(String fileName) {
        if (terrainMap.containsKey(fileName)) {
            System.err.println("Attempted to load an already loaded map file '"+fileName+"'.");
            return; // don't load the same ROOM more than once
        }
        System.out.println("Loading map '"+fileName+"'...");

        RoomLoader.fileName = fileName;
        mapWidth = mapHeight = -1;

        ArrayList<String> file = new ArrayList<>();
        try {
            BufferedReader bf = FileIO.getBufferedReader("/res/maps/"+levelPath+fileName+FILE_EXTENSION);
            String inString;
            while ((inString = bf.readLine()) != null)
                file.add(inString);
            bf.close();
        } catch (IOException e) {
            System.err.println("Error loading map '"+fileName+"':");
            e.printStackTrace();
            Window.destroy();
        }

        // load metadata
        int start = file.indexOf("<meta>");
        int end = file.indexOf("</meta>");
        loadMeta(start, end, file);

        // load texture data
        start = file.indexOf("<tiledata>");
        end = file.indexOf("</tiledata>");
        int[][][] tileData = loadTile(start, end, file);

        // load height data
        start = file.indexOf("<walkdata>");
        end = file.indexOf("</walkdata>");
        int[][] heightData = loadHeight(start, end, file);

        // load entity data
        start = file.indexOf("<entitydata>");
        end = file.indexOf("</entitydata>");
        ArrayList<String> entities = loadEntities(start, end, file);

        terrainMap.put(fileName, new TerrainData(fileName, tileset, tileData, heightData));
        entityMap.put(fileName, entities);
    }
    /**
     * Reads values such as height and width of the map being loaded.
     * @param s Start index.
     * @param e End index.
     * @param str File to read.
     */
    private static void loadMeta(int s, int e, ArrayList<String> str) {
        tileset = DEFAULT_TILESET;
        for (int i = s+1; i < e; i++) {
            StringTokenizer tok = new StringTokenizer(str.get(i),"=");
            switch (tok.nextToken()) {
                case "width":
                    mapWidth = Integer.parseInt(tok.nextToken());
                    break;
                case "height":
                    mapHeight = Integer.parseInt(tok.nextToken());
                    break;
                case "tileset":
                    tileset = tok.nextToken();
                    break;
                default:
                    System.err.println("Unrecognized metadata tag when parsing map file '"+fileName+"'.");
                    break;
            }
        }
    }
    /**
     * Reads tile shape and texture info.
     * The returned array contains the following:
     *      texture coordinates,
     *      tile "shape",
     *      secondary texture coordinates.
     * @param s Start index.
     * @param e End index.
     * @param str File to read.
     * @return
     */
    private static int[][][] loadTile(int s, int e, ArrayList<String> str) {
        int[][][] data = new int[mapWidth][mapHeight][3];
        for (int i = s+1; i < e; i++) {
            StringTokenizer tok = new StringTokenizer(str.get(i),", ");
            for (int j = 0; j < mapWidth; j++) {
                data[j][i-s-1][0] = Integer.parseInt(tok.nextToken());
                data[j][i-s-1][1] = Integer.parseInt(tok.nextToken());
                data[j][i-s-1][2] = Integer.parseInt(tok.nextToken());
            }
        }
        return data;
    }
    /**
     * Reads height data for tiles.
     * @param s Start index.
     * @param e End index.
     * @param str File to read.
     * @return
     */
    private static int[][] loadHeight(int s, int e, ArrayList<String> str) {
        int[][] data = new int[mapWidth][mapHeight];
        for (int i = s+1; i < e; i++) {
            StringTokenizer tok = new StringTokenizer(str.get(i)," ");
            for (int j = 0; j < mapWidth; j++) {
                data[j][i-s-1] = Integer.parseInt(tok.nextToken());
            }
        }
        return data;
    }
    /**
     * Creates a list of Entity "blueprints" for each MapData object.
     * This is simply the entity constructor represented by a String.
     * Actual Entity construction is handled in MapData, as multiple sets
     * of entities may need to be created, not just once during map loading.
     * @param s Start index.
     * @param e End index.
     * @param str File to read.
     * @return
     */
    private static ArrayList<String> loadEntities(int s, int e, ArrayList<String> str) {
        // pretty simple
        ArrayList<String> list = new ArrayList<>();//new String[e-s-1];
        int ind = 0;
        for (int i = s+1; i < e; i++) {
            list.add(str.get(i).trim());
            //list[ind] = str.loadNewRoom(i).trim();
            //ind++;
        }
        return list;
    }
}
