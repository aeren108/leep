package aeren.leep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

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
        JsonValue jv = getJsonValueFromPath(path);
        jv.set(value, null);
    }

    public void setData(String path, boolean value) {
        JsonValue jv = getJsonValueFromPath(path);
        jv.set(value);
    }

    public void setData(String path, String value) {
        JsonValue jv = getJsonValueFromPath(path);
        jv.set(value);
    }

    public int getInt(String path) {
        return getJsonValueFromPath(path).asInt();
    }

    public boolean getBoolean(String path) {
        return getJsonValueFromPath(path).asBoolean();
    }

    public String getString(String path) {
        return getJsonValueFromPath(path).asString();
    }

    private JsonValue getJsonValueFromPath(String path) {
        JsonValue jv = json;
        JsonValue parent = json;
        String[] split = path.split("\\.");

        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            jv = jv.get(s);

            if (jv == null) {
                if (i == split.length - 1) {
                    parent.addChild(s, new JsonValue("0"));
                } else {
                    parent.addChild(s, new JsonValue(JsonValue.ValueType.object));
                }

                jv = parent.get(s);
            } else {
                parent = jv.parent;
            }
        }

        return jv;
    }

    public void increaseData(String path, int amount) {
        setData(path, getInt(path) + amount);
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
