package com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses;

import android.os.Handler;

import com.example.rosen.luckywheel.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rosen on 29.12.2014 г..
 */
public class GameClock {
    private List<GameClockListener> clockListeners = new ArrayList<GameClockListener>();

    public static interface GameClockListener {
        public void onGameEvent(GameEvent gameEvent);
    }

    private Handler handler = new Handler();

    public GameClock() {
        handler.post(new ClockRunnable());
    }

    public void subscribe(GameClockListener listener) {
        clockListeners.add(listener);
    }


    private class ClockRunnable implements Runnable {
        @Override
        public void run() {
            onTimerTick();
            handler.postDelayed(this, Settings.FRAMERATE_CONSTANT_WHEEL);
        }

        private void onTimerTick() {
            for (GameClockListener listener : clockListeners) {
                listener.onGameEvent(new GameEvent());
            }
        }
    }
}
