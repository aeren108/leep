package aeren.leep.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterManager {
    private static CharacterManager instance;

    private Map<String, Character> characterMap;
    private List<Character> characterList;
    private Character current;

    private JsonValue json;
    private FileHandle file;

    private CharacterListener listener;

    private CharacterManager() {
        characterMap = new HashMap<>();
        characterList = new ArrayList<>();
        file = Gdx.files.local("characters.json");
    }

    public static CharacterManager getInstance() {
        if (instance == null)
            instance = new CharacterManager();
        return instance;
    }

    public void loadCharacters() {
        json = new JsonReader().parse(Gdx.files.local("characters.json"));
        JsonValue charsArray = json.get("characters");

        for (JsonValue jv : charsArray) {
            Character c = new Character(jv.name, "DENEME", jv.getBoolean("unlocked"), jv.getInt("x"), jv.getInt("y"));
            characterMap.put(c.name, c);
            characterList.add(c);
        }

        current = characterMap.get(json.getString("currentCharacter"));
    }

    public void flush() {
        file.writeString(json.toJson(JsonWriter.OutputType.json), false);
    }

    public void unlockCharacter(Character c) {
        c.unlocked = true;
        json.get("characters").get(c.name).get("unlocked").set(true);
    }

    public Character getCharacter(String name) {
        return characterMap.get(name);
    }

    public Character getCharacter(int index) {
        return characterList.get(index);
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

        if (listener != null)
            listener.onCharacterChanged(current);
    }

    public List<Character> getCharacters() {
        return characterList;
    }

    public void setCharacterListener(CharacterListener listener) {
        this.listener = listener;
    }
}
