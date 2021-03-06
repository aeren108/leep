package aeren.leep.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

import aeren.leep.DataManager;

public class CharacterManager {
    private static CharacterManager instance;

    private List<Character> characters;
    private Character current;

    private DataManager dm;

    private CharacterListener listener;

    private CharacterManager() {
        characters = new ArrayList<>();
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
            characters.add(c);
        }

        current = getCharacter(dm.getString("currentCharacter"));
        checkUnlockConditions();
    }

    public boolean checkUnlockConditions() {
        boolean isUnlocked = false;
        for (Character c : characters) {
            int currentConditionValue = dm.getInt(c.condition);
            if (!c.unlocked && currentConditionValue >= c.conditionValue) {
                unlockCharacter(c);
                isUnlocked = true;
            }
        }

        return isUnlocked;
    }

    public void unlockCharacter(Character c) {
        c.unlocked = true;
    }

    public Character getCharacter(int index) {
        return characters.get(index);
    }

    public Character getCharacter(String name) {
        for (Character c : characters) {
            if (c.name.equals(name))
                return c;
        }

        return null;
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
        return characters;
    }

    public void setCharacterListener(CharacterListener listener) {
        this.listener = listener;
    }
}
