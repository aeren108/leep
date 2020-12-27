package aeren.leep.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.HashMap;
import java.util.Map;

public class CharacterManager {
    private static CharacterManager instance;

    private Map<String, Character> chars;
    private Character current;
    private JsonValue json;

    private CharacterManager() {
        chars = new HashMap<>();
    }

    public static CharacterManager getInstance() {
        if (instance == null)
            instance = new CharacterManager();
        return instance;
    }

    public void loadCharacters() {
        json = new JsonReader().parse(Gdx.files.internal("characters.json"));
        JsonValue charsArray = json.get("characters");

        for (JsonValue jv : charsArray) {
            Character c = new Character(jv.name, jv.getBoolean("unlocked"), jv.getInt("x"), jv.getInt("y"));
            chars.put(c.name, c);
        }

        current = chars.get(json.getString("currentCharacter"));
    }

    public void saveToJson() {
        FileHandle file = Gdx.files.internal("character.json");
        file.writeBytes(json.toJson(JsonWriter.OutputType.json).getBytes(), false);
    }

    public void unlockCharacter(Character c) {
        c.unlocked = true;
        json.get("characters").get(c.name).get("unlocked").set(true);
    }

    public Character getCharacter(String name) {
        return chars.get(name);
    }

    public Character getCurrentCharacter() {
        if (current != null)
            return current;

        loadCharacters();
        return current;
    }

    public void setCurrentCharacter(Character current) {
        this.current = current;
        json.get("currentCharacter").set(current.name);
    }
}
