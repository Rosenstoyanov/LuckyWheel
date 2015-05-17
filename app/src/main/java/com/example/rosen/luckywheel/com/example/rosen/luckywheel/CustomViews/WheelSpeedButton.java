package com.example.rosen.luckywheel.com.example.rosen.luckywheel.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.rosen.luckywheel.Settings;
import com.example.rosen.luckywheel.WheelStatus;

/**
 * Created by Rosen on 11.1.2015 г..
 */
public class WheelSpeedButton extends View implements View.OnClickListener {
    private int speed;
    private Rect displayRect;
    private Rect backgroundRect;
    private Paint redPaint;
    private Paint bluePaint;
    private WheelStatus wheelStatus;

    private MyRunnable myRunnable;
    private Handler handler;

    public WheelSpeedButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        speed = 0;

        displayRect = new Rect();
        backgroundRect = new Rect();

        redPaint = new Paint();
        redPaint.setColor(Color.RED);

        bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);

        handler = new Handler();
        myRunnable = new MyRunnable();
    }

    public void startWheelSpeedButton(){
        this.handler.post(myRunnable);
        this.speed = 0;
    }
    public void setWheelStatus(WheelStatus wheelStatus){
        this.wheelStatus = wheelStatus;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        displayRect.set(0, 150 - speed, 70, 150);
        backgroundRect.set(0, 0, 70, 150);
        canvas.drawRect(backgroundRect, bluePaint);
        canvas.drawRect(displayRect, redPaint);
        super.onDraw(canvas);
    }

    @Override
    public void onClick(View v) {
        if (speed < 25){
            wheelStatus.wheelSpeed(25);
        }else {
            wheelStatus.wheelSpeed(speed);
        }
        handler.removeCallbacks(myRunnable);
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            incVarriable();
            handler.postDelayed(this, Settings.FRAMERATE_CONSTANT_BUTTONN);
            invalidate();
        }
        private void incVarriable(){
            speed+=5;
            if (speed >= 150)
                speed = 0;
        }
    }
}
