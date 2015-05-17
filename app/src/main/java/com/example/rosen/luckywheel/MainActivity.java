package com.example.rosen.luckywheel;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button btnNewGame;
    private Button btnLoadGame;
    private final float BEGINING_SUM = 10_000f;

    private Button btnStartGame;
    private EditText etPlayerName;
    private SharedPreferences sharedPreferences;

    private RelativeLayout relativeLayout;

    //TODO: pri po golqma zalojena syma vinagi pokazva game over i pri mnogo golqm zalojena syma pravi infinity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeMainWrapper);
        relativeLayout.setOnClickListener(this);
        relativeLayout.setBackground(getResources().getDrawable(Settings.MAIN_BACKGROUND_IMG));

        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnLoadGame = (Button) findViewById(R.id.btnLoadGame);
        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        etPlayerName = (EditText) findViewById(R.id.etPlayerName);

        sharedPreferences = getSharedPreferences(Settings.APP_DATA_PREFERENCE, 0);

        btnStartGame.setOnClickListener(this);
        btnNewGame.setOnClickListener(this);
        btnLoadGame.setOnClickListener(this);

        btnStartGame.setVisibility(View.INVISIBLE);
        etPlayerName.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnStartGame.setVisibility(View.INVISIBLE);
        etPlayerName.setVisibility(View.INVISIBLE);
        if (sharedPreferences.getBoolean(Settings.GAME_OVER, false)){
            sharedPreferences.edit().clear().commit();
        }
        if (null == sharedPreferences.getString(Settings.PLAYER_NAME, null)){
            btnLoadGame.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == btnNewGame.getId()){
            btnNewGame.setEnabled(false);
            btnLoadGame.setEnabled(false);
            btnStartGame.setVisibility(View.VISIBLE);
            etPlayerName.setVisibility(View.VISIBLE);
        }
        if (id == btnLoadGame.getId()){
            Intent i = new Intent(this, GameActivity.class);
            startActivity(i);
        }
        if (id == btnStartGame.getId()){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Settings.PLAYER_NAME, etPlayerName.getText().toString() + ": ");
            editor.putString(Settings.PLAYER_TOTAL_CASH, String.valueOf(BEGINING_SUM));
            editor.putBoolean(Settings.GAME_OVER, false);
            editor.putBoolean(Settings.IS_STARTED_FIRST,true);
            editor.commit();
            btnNewGame.setEnabled(true);
            etPlayerName.setText("");
            startActivity(new Intent(this, GameActivity.class));
        }
        if (id == relativeLayout.getId()){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}
