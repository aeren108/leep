package aeren.leep;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Utils {

    public static TextureRegion[][] getFrames(Texture t, int x, int y, int rows, int cols, int width, int height) {
        TextureRegion[][] frames = new TextureRegion[rows][cols];
        x *= 4;

        for (int i = y; i < y + rows; i++) {
            for (int j = x; j < x + cols; j++) {
                frames[i - y][j - x] = new TextureRegion(t, j * width, i * height, width, height);
            }
        }

        return frames;
    }

    public static TextureRegion[] flipFrames(TextureRegion[] frames, boolean x, boolean y) {
        TextureRegion[] flipped = new TextureRegion[frames.length];

        for (int i = 0; i < frames.length; i++) {
            Texture t = frames[i].getTexture();
            flipped[i] = new TextureRegion(t, frames[i].getRegionX(), frames[i].getRegionY(), frames[i].getRegionWidth(), frames[i].getRegionHeight());
            flipped[i].flip(x, y);
        }

        return flipped;
    }

}
