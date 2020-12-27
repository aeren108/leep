package aeren.leep.level;

import java.util.HashMap;
import java.util.Map;

public class LevelDataFactory {
    private static Map<String, LevelData> levelDatas = new HashMap<>();

    public LevelData getLevelData(String path) {
        if (levelDatas.get(path) != null)
            return levelDatas.get(path);

        LevelData data = LevelDataParser.getLevelDataFromJson(path);
        levelDatas.put(path, data);
        return data;
    }
}
