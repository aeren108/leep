package aeren.leep;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    public static AssetManager manager = new AssetManager();

    public static final AssetDescriptor<Texture> dinoIdle = new AssetDescriptor<>("sprites/dino_idle.png", Texture.class);
    public static final AssetDescriptor<Texture> fruit = new AssetDescriptor<>("sprites/fruit.png", Texture.class);
    public static final AssetDescriptor<Texture> fireball = new AssetDescriptor<>("sprites/fireball.png", Texture.class);
    public static final AssetDescriptor<Texture> alert = new AssetDescriptor<>("sprites/alert.png", Texture.class);

    public static final AssetDescriptor<Sound> pickup = new AssetDescriptor<>("sfx/pickup.wav", Sound.class);
    public static final AssetDescriptor<Sound> swipe = new AssetDescriptor<>("sfx/swipe.wav", Sound.class);
    public static final AssetDescriptor<Sound> hurt = new AssetDescriptor<>("sfx/hurt.wav", Sound.class);
    public static final AssetDescriptor<Sound> fall = new AssetDescriptor<>("sfx/fall.wav", Sound.class);

    //public static final AssetDescriptor<Texture> menubg = new AssetDescriptor<Texture>("ui/menu-back.png", Texture.class);
    public static final AssetDescriptor<Skin> skin = new AssetDescriptor<Skin>("ui/ui-skin.json", Skin.class, new SkinLoader.SkinParameter("ui/ui-skin.atlas"));

    public static void loadAssets() {
        manager.load(dinoIdle);
        manager.load(fruit);
        manager.load(fireball);
        manager.load(alert);

        manager.load(pickup);
        manager.load(swipe);
        manager.load(hurt);
        manager.load(fall);

        manager.load("sfx/level1_theme.ogg", Music.class);

        //manager.load(menubg);
        manager.load(skin);
    }

    public static void dispose() {
        manager.dispose();
    }

}
