package aeren.leep.input;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GestureHandler extends GestureDetector {

    public GestureHandler(SwipeListener listener) {
        super(new GestureManager(listener));
    }

    private static class GestureManager implements GestureListener {

        private final Vector2 swipeDirection;
        private final List<SwipeListener> listeners;

        public GestureManager(SwipeListener listener) {
            listeners = new ArrayList<>();
            listeners.add(listener);

            swipeDirection = Vector2.Zero;
        }

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            swipeDirection.set(velocityX, velocityY);
            for (SwipeListener listener : listeners)
                listener.onSwipe(swipeDirection);

            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }

        @Override
        public void pinchStop() {

        }

        public void addListener(SwipeListener listener) {
            listeners.add(listener);
        }
    }
}
