package com.example.rosen.luckywheel;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class GameOverActivity extends Activity implements View.OnClickListener {

    private ImageView imageView;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        imageView = (ImageView) findViewById(R.id.imageViewGameOver);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeGameOverWrapper);
        Drawable drawable = getResources().getDrawable(Settings.GAME_OVER_IMG);
        imageView.setImageDrawable(drawable);

//        relativeLayout.setBackgroundColor(Color.GRAY);
//        relativeLayout.setAlpha(1000);
        relativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
