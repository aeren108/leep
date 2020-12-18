package aeren.leep.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;

public class MapGenerator {

    private LevelData data;
    private int[][] mapData;

    private int width = 9, height = 16;

    public MapGenerator(LevelData data) {
        this.data = data;
        mapData = new int[width][height];

        randomizeMap();
    }

    private void randomizeMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (Math.random() <= data.birthRate) mapData[x][y] = 1;
                else mapData[x][y] = 0;
            }
        }
    }

    private void runAutomata() {
        for (int i = 0; i < data.maxStep; i++) {
            mapData = stepAutomata();
        }
    }

    public int[][] stepAutomata() {
        int[][] newMap = new int[width][height];

        for (int y = 2; y < height - 2; y++) {
            for (int x = 1; x < width - 1; x++) {
                int aliveNeighbours = aliveNeighbourCount(x, y);

                int status = mapData[x][y];
                if (status == 1) {
                    if (aliveNeighbours < data.deathThreshold) newMap[x][y] = 0;
                    else newMap[x][y] = 1;
                } else {
                    if (aliveNeighbours > data.birthThreshold) newMap[x][y] = 1;
                    else newMap[x][y] = 0;
                }
            }
        }

        return newMap;
    }

    private int aliveNeighbourCount(int x, int y) {
        int alive = 0;

        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (mapData[i][j] == 1)
                    alive++;
            }
        }

        return alive;
    }

    public TiledMap generateTiledMap() {
        randomizeMap();
        runAutomata();

        TiledMap map = data.map;
        TiledMapTileLayer ground = (TiledMapTileLayer) map.getLayers().get("ground");
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                if (mapData[x][y] == 1) {
                    cell.setTile(tileSet.getTile(1));
                    ground.setCell(x, y, cell);
                } else {
                    cell.setTile(tileSet.getTile(110));
                    ground.setCell(x, y, cell);
                }
            }
        }


        return map;
    }
}
