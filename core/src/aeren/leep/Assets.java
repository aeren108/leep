package aeren.leep;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
  public static AssetManager manager = new AssetManager();
  
  public static final AssetDescriptor<Texture> dinoIdle = new AssetDescriptor<Texture>("sprites/dino_idle.png", Texture.class);
  public static final AssetDescriptor<Texture> fruit = new AssetDescriptor<Texture>("sprites/fruit.png", Texture.class);
  public static final AssetDescriptor<Texture> verticalFireball = new AssetDescriptor<Texture>("sprites/fireball_vertical.png", Texture.class);
  public static final AssetDescriptor<Texture> horizontalFireball = new AssetDescriptor<Texture>("sprites/fireball_horizontal.png", Texture.class);
  public static final AssetDescriptor<Texture> alert =  new AssetDescriptor<Texture>("sprites/alert.png", Texture.class);
  
  public static final AssetDescriptor<Sound> pickup = new AssetDescriptor<Sound>("sfx/pickup.wav", Sound.class);
  public static final AssetDescriptor<Sound> swipe = new AssetDescriptor<Sound>("sfx/swipe.wav", Sound.class);
  public static final AssetDescriptor<Sound> hurt = new AssetDescriptor<Sound>("sfx/hurt.wav", Sound.class);
  public static final AssetDescriptor<Sound> fall = new AssetDescriptor<Sound>("sfx/fall.wav", Sound.class);
  
  public static void loadAssets() {
    manager.load(dinoIdle);
    manager.load(fruit);
    manager.load(verticalFireball);
    manager.load(horizontalFireball);
    manager.load(alert);
    
    manager.load(pickup);
    manager.load(swipe);
    manager.load(hurt);
    manager.load(fall);
  }
  
  public static void dispose() {
    manager.dispose();
  }

}
