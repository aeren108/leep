package aeren.leep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private static DataManager instance;

    public static final String TOTAL_GAMES = "totalGamesPlayed";
    public static final String TOTAL_FRUITS = "totalFruitsCollected";
    public static final String TOTAL_DEATHS = "totalDeaths";

    private FileHandle file;
    private JsonValue json;
    private JsonValue highscores;

    private DataManager() {
        file = Gdx.files.local("data.json");
    }

    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    public void loadData() {
        json = new JsonReader().parse(file);
        highscores = json.get("highscores");
    }

    public void setData(String path, int value) {
        if (json.get(path) == null)
            json.addChild(path, new JsonValue(value));

        json.get(path).set(value, null);
    }

    public int getData(String path) {
        return json.get(path).asInt();
    }

    public void increaseData(String path, int amount) {
        setData(path, getData(path) + amount);
    }

    public void setHighscore(String level, int highscore) {
        if (highscores.get(level) == null)
            highscores.addChild(level, new JsonValue(highscore));

        highscores.get(level).set(highscore, null);
    }

    public int getHighscore(String level) {
        if (highscores.get(level) == null)
            highscores.addChild(level, new JsonValue(0));

        return highscores.get(level).asInt();
    }

    public void increaseHighscore(String level, int amount) {
        setHighscore(level, getHighscore(level) + amount);
    }

    public void flush() {
        file.writeBytes(json.toJson(JsonWriter.OutputType.json).getBytes(), false);
    }
}
