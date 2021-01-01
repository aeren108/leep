package aeren.leep;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Vector2> removeAdjacentTiles(List<Vector2> tiles, int posx, int posy, int distance) {
        List<Vector2> distants = new ArrayList<>();
        distants.addAll(tiles);

        for (int y = posy - distance; y <= posy + distance; y++) {
            for (int x = posx - distance; x <= posx + distance; x++) {
                for (int i = 0; i < distants.size(); i++) {
                    Vector2 tile = distants.get(i);

                    if ((int) tile.x == x && (int) tile.y == y)
                        distants.remove(tile);
                }
            }
        }

        return distants;
    }

}
