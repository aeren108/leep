package aeren.leep.level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;

import java.util.ArrayList;
import java.util.List;

import aeren.leep.level.pattern.Pattern;
import aeren.leep.level.pattern.PatternMatcher;

public class MapGenerator {

    private LevelData data;
    private int[][] mapData;

    private final int width = 9, height = 16;

    public MapGenerator(LevelData data) {
        this.data = data;
        mapData = new int[height][width];

        clearMap();
        randomizeMap();
    }

    private void randomizeMap() {
        for (int y = 3; y < height - 3; y++) {
            for (int x = 1; x < width - 1; x++) {
                if ((int) (Math.random() * 11) <= data.birthRate * 10) mapData[y][x] = 1;
                else mapData[y][x] = 0;
            }
        }
    }

    private void clearMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapData[y][x] = 0;
            }
        }
    }

    private void runAutomata() {
        for (int i = 0; i < data.maxStep; i++) {
            mapData = stepAutomata();
        }
    }

    private int[][] stepAutomata() {
        int[][] newMap = new int[height][width];

        for (int y = 3; y < height - 3; y++) {
            for (int x = 1; x < width - 1; x++) {
                int aliveNeighbours = aliveNeighbourCount(x, y);

                int status = mapData[y][x];
                if (status == 1) {
                    if (aliveNeighbours < data.deathThreshold) newMap[y][x] = 0;
                    else newMap[y][x] = 1;
                } else {
                    if (aliveNeighbours > data.birthThreshold) newMap[y][x] = 1;
                    else newMap[y][x] = 0;
                }
            }
        }

        return newMap;
    }

    private int aliveNeighbourCount(int x, int y) {
        int alive = 0;

        for (int i = y - 1; i < y + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {
                if (i == y && j == x)
                    continue;

                if (mapData[i][j] == 1)
                    alive++;
            }
        }

        return alive;
    }

    public TiledMap generateTiledMap() {
        clearMap();
        randomizeMap();
        runAutomata();

        TiledMap map = data.map;
        TiledMapTileLayer ground = (TiledMapTileLayer) map.getLayers().get("ground");
        TiledMapTileSet tileSet = map.getTileSets().getTileSet(0);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TiledMapTileLayer.Cell cell = ground.getCell(x, height - y - 1);
                if (mapData[y][x] == 1) {
                    cell.setTile(tileSet.getTile(1));
                    ground.setCell(x, height - y - 1, cell); // height - y - 1, because origin is bottom left corner for tiled map
                } else {
                    cell.setTile(tileSet.getTile(110));
                    ground.setCell(x, height - y - 1, cell); // height - y - 1, because origin is bottom left corner for tiled map
                }
            }
        }

        placePatterns();
        autoTile();

        if (tileCount() < 15)
            return generateTiledMap();

        return map;
    }

    private void placePatterns() {
        Pattern p = null;
        for (int i = 0; i < data.patterns.size(); i++) {
            if (tileCount() >= data.patterns.get(i).tileThreshold) {
                p = data.patterns.get(i);
                break;
            }
        }

        int implementationCount = 0;

        while (p != null && p.repeat && tileCount() >= p.tileThreshold) {
            if (implementPattern(p)) {
                implementationCount++;
                if (implementationCount >= p.max && p.after < data.patterns.size() && p.after != -1) {
                    p = data.patterns.get(p.after);
                    implementationCount = 0;
                } else {
                    break;
                }
            } else if (p.after < data.patterns.size() && p.after != -1) {
                p = data.patterns.get(p.after);
                implementationCount = 0;
            } else {
                break;
            }
        }
    }

    /*implements pattern to mapData if circumstances are right,
    returns whether pattern is implemented or not */
    private boolean implementPattern(Pattern p) {
        List<int[]> matchingTiles = patternMatchingTiles(p);

        if (matchingTiles.isEmpty())
            return false;

        int[] tile = matchingTiles.get((int) (Math.random() * matchingTiles.size()));
        PatternMatcher.applyPattern(mapData, p.targetPattern, tile[0], tile[1]);

        return true;
    }

    private List<int[]> patternMatchingTiles(Pattern p) {
        List<int[]> matchingTiles = new ArrayList<>();
        List<int[]> tilePos = tilePositions();

        for (int[] pos : tilePos) {
            int[][] curPattern = PatternMatcher.extractPattern(mapData, p.controlPattern, pos[0], pos[1]);

            if (PatternMatcher.doPatternsMatch(curPattern, p.controlPattern))
                matchingTiles.add(new int[]{pos[0], pos[1]});
        }

        return matchingTiles;
    }

    private void autoTile() {
        TiledMap map = data.map;
        TiledMapTileLayer ground = (TiledMapTileLayer) map.getLayers().get("ground");
        TiledMapTileSet tileSet = map.getTileSets().getTileSet("forest_set");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (mapData[y][x] != 0)
                    continue;

                int[][] pattern = PatternMatcher.extractTilePattern(mapData, x, y);

                for (int i = 0; i < PatternMatcher.PATTERNS.length; i++) {
                    if (PatternMatcher.doPatternsMatch(PatternMatcher.PATTERNS[i], pattern)) {
                        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                        cell.setTile(tileSet.getTile(data.autoTiles[i]));
                        ground.setCell(x, height - y - 1, cell); // height - y - 1, because origin is bottom left corner for tiled map

                        break;
                    }
                }
            }
        }
    }

    private List<int[]> tilePositions() {
        List<int[]> posList = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (mapData[y][x] == 1) {
                    posList.add(new int[] {x, y});
                }
            }
        }

        return posList;
    }

    private int tileCount() {
        int tiles = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (mapData[y][x] == 1)
                    tiles++;
            }
        }

        return tiles;
    }

    public int[][] getRawMap() {
        return mapData;
    }
}
