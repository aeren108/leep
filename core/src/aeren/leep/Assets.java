package aeren.leep;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
  public static AssetManager manager = new AssetManager();
  
  public static AssetDescriptor<Texture> player = new AssetDescriptor<Texture>("sprites/box_char.png", Texture.class);
  public static AssetDescriptor<Texture> fruit = new AssetDescriptor<Texture>("sprites/fruit.png", Texture.class);
  
  public static AssetDescriptor<Sound> pickup = new AssetDescriptor<Sound>("sfx/pickup.wav", Sound.class);
public static AssetDescriptor<Sound> swipe = new AssetDescriptor<Sound>("sfx/swipe.wav", Sound.class);
  
  public static void loadAssets() {
    manager.load(player);
    manager.load(fruit);
    manager.load(pickup);
    manager.load(swipe);
  }
  
  public static void dispose() {
    manager.dispose();
  }

}
