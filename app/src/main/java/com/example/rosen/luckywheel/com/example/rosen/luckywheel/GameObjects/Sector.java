package com.example.rosen.luckywheel.com.example.rosen.luckywheel.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses.GameClock;
import com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses.GameEvent;
import com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses.GameObject;
import com.example.rosen.luckywheel.Settings;

/**
 * Created by Rosen on 29.12.2014 г..
 */
public class Sector extends GameObject implements GameClock.GameClockListener {
    private Bitmap bitmap;
    private float degrees = 0.0f;
    private float degreesInc = 0.0f;
    private float wonCoefficient;
    private float sectorDeg;

    public Sector(Drawable drawable){
        bitmap = ((BitmapDrawable)drawable).getBitmap();
    }

    public void setWonCoefficient(float coefficient){
        this.wonCoefficient = coefficient;
    }

    public float getWonCoefficient(){
        return wonCoefficient;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(-degrees, Settings.DRAWING_VIEW_WIDTH / 2, Settings.DRAWING_VIEW_HEIGHT / 2);
        canvas.drawBitmap(bitmap, Settings.DRAWING_VIEW_WIDTH / 2, (Settings.DRAWING_VIEW_HEIGHT / 2) - bitmap.getHeight(), null);
        canvas.restore();

//        Matrix mat = new Matrix();
//        mat.postRotate(i, 0, bitmap.getHeight());
//        Bitmap bMapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
//        canvas.drawBitmap(bMapRotated,
//                Settings.DRAWING_VIEW_WIDTH / two
//                ,(Settings.DRAWING_VIEW_HEIGHT / two) - bitmap.getHeight()
//                , null);
    }

    public boolean isChosenSector(float degreesSelector){
        return degrees <= degreesSelector && degreesSelector <= degrees + calcSectorDegrees();
    }

    public void setSectorDegr(float deg){
        sectorDeg = deg;
    }
    // TODO: Find way get poiner degrees on bid

    private float calcSectorDegrees(){
        float f = bitmap.getHeight() / bitmap.getWidth();
        Math.toDegrees(f);
        return sectorDeg;
    }
    public void setStartDegrees (float degrees){
        this.degrees = degrees;
    }

    public void setDegreesInc(float degreesInc){
        this.degreesInc = degreesInc;
    }

    public void correctDegreees(){
        while (degrees > 360){
            degrees -= 360;
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        degrees += degreesInc;
    }
}
