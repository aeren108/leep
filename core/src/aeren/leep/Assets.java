package aeren.leep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {
    private static Assets instance;

    private AssetManager manager;
    private Map<String, List<Asset>> groups;
    //TODO: Main menu bacground texture

    private Assets(String assetsFile) {
        manager = new AssetManager();

        loadGroups(assetsFile);
        loadGroup("global");
        finishLoading();
    }

    public static Assets getInstance() {
        if (instance == null)
            instance = new Assets("assets.json");

        return instance;
    }

    public void loadGroup(String groupName) {
        List<Asset> assets = groups.get(groupName);

        if (assets == null) return;

        for (Asset asset : assets)
            manager.load(asset.path, asset.type);
    }

    public void unloadGroup(String groupName) {
        List<Asset> assets = groups.get(groupName);

        if (assets == null) return;

        for (Asset asset : assets)
            manager.unload(asset.path);
    }

    public <T> T get(String fileName) {
        return manager.get(fileName);
    }

    public <T> T get (String fileName, Class<T> type) {
        return manager.get(fileName, type);
    }

    private void loadGroups(String assetFile) {
        groups = new HashMap<>();

        JsonValue json = new JsonReader().parse(Gdx.files.internal(assetFile));
        JsonValue assets = json.get("assets");

        for (JsonValue group : assets) {
            List<Asset> elements = new ArrayList<>();

            for (JsonValue element : group) {
                Asset a = new Asset(element.getString("type"), element.getString("path"));
                elements.add(a);
            }

            groups.put(group.name, elements);
        }
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public boolean update() {
        return manager.update();
    }

    public void dispose() {
        manager.dispose();
    }

    private class Asset {
        public Class<?> type;
        public String path;

        public Asset(String type, String path) {
            try {
                this.type = Class.forName(type);
                this.path = path;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
