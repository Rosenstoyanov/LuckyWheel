package com.example.rosen.luckywheel.com.example.rosen.luckywheel.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses.GameClock;
import com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses.GameEvent;
import com.example.rosen.luckywheel.Settings;
import com.example.rosen.luckywheel.WheelStatus;
import com.example.rosen.luckywheel.com.example.rosen.luckywheel.GameObjects.Pointer;
import com.example.rosen.luckywheel.com.example.rosen.luckywheel.GameObjects.Sector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rosen on 29.12.2014 г..
 */
public class GameView extends ImageView implements GameClock.GameClockListener, View.OnTouchListener {
    private Sector sector1;
    private Sector sector2;
    private Sector sector3;
    private Sector sector4;
    private Sector sector5;
    private Sector sector6;

    private float speed = 0.0f;//check what happend with 0 while with -1 spins back
    private boolean gameStatusChecked = false;
    private WheelStatus wheelStatus;
    private List<Pointer> pointersList;
    private float wonCoef = 0.0f;
    private Bitmap backWheel;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        backWheel = BitmapFactory.decodeResource(getResources(), Settings.WRAPPING_WHEEL);

        sector1 = new Sector(getResources().getDrawable(Settings.FIRST_SECTOR));//-three
        sector2 = new Sector(getResources().getDrawable(Settings.SECOND_SECTOR));//+two
        sector3 = new Sector(getResources().getDrawable(Settings.THIRT_SECTOR));//1
        sector4 = new Sector(getResources().getDrawable(Settings.FOURTH_SECTOR));//+two
        sector5 = new Sector(getResources().getDrawable(Settings.FIFTH_SECTOR));//-three
        sector6 = new Sector(getResources().getDrawable(Settings.SIXT_SECTOR));//1

        sector1.setStartDegrees(0.0f);
        sector2.setStartDegrees(45.0f);
        sector3.setStartDegrees(90.0f);
        sector4.setStartDegrees(180.0f);
        sector5.setStartDegrees(225.0f);
        sector6.setStartDegrees(270.0f);
        //negative coef sectors
        sector1.setWonCoefficient(-3.0f);
        sector2.setWonCoefficient(2.0f);
        sector3.setWonCoefficient(1.0f);
        sector4.setWonCoefficient(2.0f);
        sector5.setWonCoefficient(-3.0f);
        sector6.setWonCoefficient(1.0f);

        sector1.setSectorDegr(45.0f);
        sector2.setSectorDegr(45.0f);
        sector3.setSectorDegr(90.0f);
        sector4.setSectorDegr(45.0f);
        sector5.setSectorDegr(45.0f);
        sector6.setSectorDegr(90.0f);

        pointersList = new ArrayList<>();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Settings.DRAWING_VIEW_HEIGHT = this.getHeight();
        Settings.DRAWING_VIEW_WIDTH = this.getWidth();
//        Log.i("DH", ""+ Settings.DRAWING_VIEW_HEIGHT);
//        Log.i("DW", ""+Settings.DRAWING_VIEW_WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawOval(0,0,450,450,new Paint(Color.BLUE));
        canvas.drawBitmap(backWheel,
                (Settings.DRAWING_VIEW_WIDTH / 2) - (backWheel.getWidth()/2),
                (Settings.DRAWING_VIEW_HEIGHT /2) - (backWheel.getHeight()/2), null);
        sector1.draw(canvas);
        sector2.draw(canvas);
        sector3.draw(canvas);
        sector4.draw(canvas);
        sector5.draw(canvas);
        sector6.draw(canvas);
        //Log.i("comp", (Settings.DRAWING_VIEW_HEIGHT == canvas.getHeight() && Settings.DRAWING_VIEW_WIDTH == canvas.getWidth()) + "");// true
        for(Pointer point : pointersList){
            if (point != null){
                point.draw(canvas);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        sector1.setDegreesInc(speed);
        sector2.setDegreesInc(speed);
        sector3.setDegreesInc(speed);
        sector4.setDegreesInc(speed);
        sector5.setDegreesInc(speed);
        sector6.setDegreesInc(speed);

        sector1.onGameEvent(gameEvent);
        sector2.onGameEvent(gameEvent);
        sector3.onGameEvent(gameEvent);
        sector4.onGameEvent(gameEvent);
        sector5.onGameEvent(gameEvent);
        sector6.onGameEvent(gameEvent);

        if (speed > 0.0f){
            speed -= 0.5f;
            if (gameStatusChecked){
                gameStatusChecked = false;
                wheelStatus.wheelIsSpinning(true);
            }
        } else if (!gameStatusChecked && speed == 0){
            wonCoef = 0.0f;
            // TODO: find better way to find selected win sector
            for (Pointer point : pointersList){
                checkSectorsAftherSpin(point.getDegrees());
            }
            wheelStatus.onSpinFinish(wonCoef);
            wonCoef = 0.0f;
            wheelStatus.wheelIsSpinning(false);
            gameStatusChecked = true;
        }
        invalidate();
    }

    private void checkSectorsAftherSpin(float bitedDeg){
        sector1.correctDegreees();
        sector2.correctDegreees();
        sector3.correctDegreees();
        sector4.correctDegreees();
        sector5.correctDegreees();
        sector6.correctDegreees();
        if (sector1.isChosenSector(bitedDeg)){
            wonCoef += sector1.getWonCoefficient();
        } else if (sector2.isChosenSector(bitedDeg)){
            wonCoef += sector2.getWonCoefficient();
        }
        else if (sector3.isChosenSector(bitedDeg)){
            wonCoef += sector3.getWonCoefficient();
        }
        else if (sector4.isChosenSector(bitedDeg)){
            wonCoef += sector4.getWonCoefficient();
        }
        else if (sector5.isChosenSector(bitedDeg)){
            wonCoef += sector5.getWonCoefficient();
        }
        else if (sector6.isChosenSector(bitedDeg)){
            wonCoef += sector6.getWonCoefficient();
        }
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public void setOnSpinFinishListener(WheelStatus onSpinFinishListener) {
        this.wheelStatus = onSpinFinishListener;
    }

    public void clearPointers(){
        this.pointersList.clear();
        invalidate();
    }

    public int pointerCount(){
        return this.pointersList.size();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getActionMasked()){
            boolean canDraw =
                    Math.abs((event.getX() - (Settings.DRAWING_VIEW_WIDTH/2)) *
                    (event.getX() - (Settings.DRAWING_VIEW_WIDTH/2))) +
                            Math.abs((event.getY() - (Settings.DRAWING_VIEW_HEIGHT / 2)) *
                                    (event.getY() - (Settings.DRAWING_VIEW_HEIGHT / 2))) >
//                        192 * two;
                    ((BitmapDrawable)(getResources().getDrawable(Settings.THIRT_SECTOR))).getBitmap().getWidth() *
                            ((BitmapDrawable)(getResources().getDrawable(Settings.THIRT_SECTOR))).getBitmap().getWidth();
            if (pointersList.size() <= 4 && canDraw){
                pointersList.add(new Pointer(event.getX(), event.getY(), getResources().getDrawable(Settings.POINTER)));
            }
            invalidate();
        }
        return false;
    }
}
