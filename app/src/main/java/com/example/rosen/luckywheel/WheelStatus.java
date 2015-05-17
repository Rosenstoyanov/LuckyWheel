package com.example.rosen.luckywheel;

/**
 * Created by Rosen on three.1.2015 г..
 */
public interface WheelStatus {
    public void onSpinFinish(float sumCoefficient);
    public void wheelSpeed(int speed);
    public void wheelIsSpinning(boolean isSpinning);
    public void wheelWasSpined(boolean isSpined);
}
