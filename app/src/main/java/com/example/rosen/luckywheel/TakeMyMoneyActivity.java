package com.example.rosen.luckywheel;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class TakeMyMoneyActivity extends Activity implements View.OnClickListener {
    private RelativeLayout relativeLayout;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_my_money);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeTakeMoneyWrapper);
        imageView = (ImageView) findViewById(R.id.imageViewTakeMoney);

        Drawable drawable = getResources().getDrawable(Settings.TAKE_MONEY_IMG);

        imageView.setImageDrawable(drawable);

        relativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
