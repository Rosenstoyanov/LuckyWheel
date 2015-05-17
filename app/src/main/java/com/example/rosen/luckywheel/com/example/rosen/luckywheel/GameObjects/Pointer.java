package com.example.rosen.luckywheel.com.example.rosen.luckywheel.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses.GameObject;
import com.example.rosen.luckywheel.Settings;

/**
 * Created by Rosen on 17.1.2015 г..
 */
public class Pointer extends GameObject {
    // TODO: centering the img painting adn calc degrees
    private float xCoord;
    private float yCoord;
    private Bitmap bitmap;
    private float degrees;

    public Pointer(float xCoord, float yCoord, Drawable drawable){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        bitmap = ((BitmapDrawable)drawable).getBitmap();
        calcDegrees(Settings.DRAWING_VIEW_WIDTH /2 , Settings.DRAWING_VIEW_HEIGHT /2);
    }

    public float getDegrees(){
        return  degrees;
    }
    private void calcDegrees(float cX, float cY){
        // TODO:: rounding
        float xLength = xCoord - cX;
        float yLength = yCoord - cY;

        degrees =  (float)Math.toDegrees(Math.atan2(yLength, xLength));
        if (degrees < 0){
            degrees *= -1;
        } else {
            degrees = 360 - degrees;
        }
    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawBitmap(bitmap, xCoord - bitmap.getWidth(), yCoord - bitmap.getHeight(), null);
        canvas.drawBitmap(bitmap, xCoord, yCoord, null);
    }
}
