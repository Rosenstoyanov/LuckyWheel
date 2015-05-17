package com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses;

import android.graphics.Canvas;

/**
 * Created by Rosen on 29.12.2014 г..
 */
public abstract class GameObject implements GameClock.GameClockListener {
    public abstract void draw(Canvas canvas);
    @Override
    public void onGameEvent(GameEvent gameEvent) {

    }
}
