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

import aeren.leep.DataManager;

public class CharacterManager {
    private static CharacterManager instance;

    private Map<String, Character> characterMap;
    private List<Character> characterList;
    private Character current;

    private DataManager dm;

    private CharacterListener listener;

    private CharacterManager() {
        characterMap = new HashMap<>();
        characterList = new ArrayList<>();

        dm = DataManager.getInstance();
    }

    public static CharacterManager getInstance() {
        if (instance == null)
            instance = new CharacterManager();
        return instance;
    }

    public void loadCharacters() {
        JsonValue json = new JsonReader().parse(Gdx.files.internal("characters.json"));

        for (JsonValue jv : json) {
            Character c = new Character(jv.name, jv.getString("status"), jv.getString("condition"), jv.getInt("conditionValue"),
                                        jv.getInt("x"), jv.getInt("y"), false);
            characterMap.put(c.name, c);
            characterList.add(c);

            //dm.setData("characters."+c.name+".unlocked", false);
        }

        current = characterMap.get(dm.getString("currentCharacter"));
        checkUnlockConditions();
    }

    public boolean checkUnlockConditions() {
        boolean isUnlocked = false;
        for (Character c : characterList) {
            int currentConditionValue = dm.getInt(c.condition);
            if (currentConditionValue >= c.conditionValue) {
                unlockCharacter(c);
                isUnlocked = true;
            }
        }

        return isUnlocked;
    }

    public void unlockCharacter(Character c) {
        c.unlocked = true;
        //dm.setData("characters."+c.name+".unlocked", true);
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
        dm.setData("currentCharacter", current.name);

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
