package com.example.rosen.luckywheel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rosen.luckywheel.com.example.rosen.luckywheel.CustomViews.GameView;
import com.example.rosen.luckywheel.com.example.rosen.luckywheel.CustomViews.WheelSpeedButton;
import com.example.rosen.luckywheel.com.example.rosen.luckywheel.gameClasses.GameClock;

public class GameActivity extends Activity implements View.OnClickListener, WheelStatus {

    private GameView gameView;
    private EditText editTextBid;
    private TextView tvYouWon;
    private TextView tvCurrentMoney;
    private TextView tvPlayerName;
    private Button btnPointersOK;
    private Button btnClearPoiners;
    private Button btnBid;
    private Button btnTakeMyMoney;
    private LinearLayout linearLayout;
    private CheckBox checkBoxAccel;

    private WheelSpeedButton wheelSpeedButton;
    private SharedPreferences sharedPreferences;
    private boolean wasSpined = false;

    private Boolean enableBidSumBtn = false;

    private final String YOU_WON = "You Won: ";
    private final String YOU_BID = "You Bid: ";
    private final String YOU_LOSE = "You lose: ";

    //TODO some beautifull stuf with suport library

    private float currentSum ;
    private float bidSum;
    private SensorData sensorData;

    private boolean bidWasMade = false;
    private boolean pointsOK = false;
    private boolean isAccActive = false;

    //TODO: do not allow bid higher than current sum  and bid sum to be negative

    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        initViews();
        setListeners();


        sharedPreferences = getSharedPreferences(Settings.APP_DATA_PREFERENCE, 0);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean(Settings.IS_STARTED_FIRST,true)){
            Toast.makeText(this, "select pointers with touch around the circle the click poiners OK", Toast.LENGTH_LONG).show();
            editor.putBoolean(Settings.IS_STARTED_FIRST, false);
            editor.commit();
        }// TODO check again editor code
        linearLayout.setBackground(getResources().getDrawable(Settings.WHEEL_BACKGROUND_IMG));
        sensorData = new SensorData(this, this);

        btnBid.setEnabled(false);
        editTextBid.setEnabled(false);

        tvYouWon.setText("");

        GameClock gameClock = new GameClock();
        gameClock.subscribe(gameView);
    }

    // TODO: may be on resume to reset all buton status default clear all bids and set current sum


    @Override
    protected void onResume() {
        super.onResume();
        tvPlayerName.setText(sharedPreferences.getString(Settings.PLAYER_NAME, "NoName"));
        currentSum = Float.parseFloat(sharedPreferences.getString(Settings.PLAYER_TOTAL_CASH, "40000"));
        tvCurrentMoney.setText(currentSum + "$");
        if (sharedPreferences.getBoolean(Settings.GAME_OVER, false)){
            finish();
        }
        sensorData.registerListeners();//TODO if user do not want to use sensor do some flags to preven it
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorData.unRegisterListeners();
        editor.putString(Settings.PLAYER_NAME, tvPlayerName.getText().toString());
        editor.putString(Settings.PLAYER_TOTAL_CASH, String.valueOf(currentSum));
        editor.commit();
    }

    private void initViews(){
        gameView = (GameView) findViewById(R.id.gameView);
        editTextBid = (EditText) findViewById(R.id.etBidSum);
        tvYouWon = (TextView) findViewById(R.id.tvYouWon);
        tvCurrentMoney = (TextView) findViewById(R.id.tvPlayerMoney);
        tvPlayerName = (TextView) findViewById(R.id.tvPlayerName);
        btnPointersOK = (Button) findViewById(R.id.btnPointersOK);
        btnBid = (Button) findViewById(R.id.btnBid);
        btnTakeMyMoney = (Button) findViewById(R.id.btnTakeMoney);
        wheelSpeedButton = (WheelSpeedButton) findViewById(R.id.wheelSpeed);
        btnClearPoiners = (Button) findViewById(R.id.btnClearPointers);
        linearLayout = (LinearLayout) findViewById(R.id.linearGameActivityWrapper);
        checkBoxAccel = (CheckBox) findViewById(R.id.checkboxAccel);
    }

    private void setListeners(){
        wheelSpeedButton.setOnClickListener(this);
        btnPointersOK.setOnClickListener(this);
        btnBid.setOnClickListener(this);
        btnClearPoiners.setOnClickListener(this);
        btnTakeMyMoney.setOnClickListener(this);
        gameView.setOnSpinFinishListener(this);
        wheelSpeedButton.setWheelStatus(this);
        linearLayout.setOnClickListener(this);
        editTextBid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextBid.getText().length() >= 1){
                    double currBid = Double.valueOf(editTextBid.getText().toString());
                    if (currBid <= 0){
                        Toast.makeText(getApplicationContext(), "bid must not be <= 0", Toast.LENGTH_SHORT).show();
                        editTextBid.setText("");
                    }
                    if (currBid > currentSum){
                        editTextBid.setText("");
                        Toast.makeText(getApplicationContext(),"bid sum should not be grater than current", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        editTextBid.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN){
//                    //Log.i("Key", keyCode + "");
//                    if (editTextBid.getText().length() >= 1){
//                        double currBid = Double.valueOf(editTextBid.getText().toString());
//                        int size = Integer.valueOf(editTextBid.getText().toString().length());
//                        Log.i("currBidSum", ""+ currBid);
//                        Log.i("checkDeba", ""+((keyCode - 7) +  (currBid * 10)));
//                        Log.i("length", ""+ size);
//                        Log.i("curr Numv", "" + (keyCode - 7));
//                        if (currBid + ((keyCode - 7) +  (size * 10)) > currentSum){
//                            editTextBid.setText("");
//                            editTextBid.setText(String.valueOf(currBid));
//                            Log.i("ot kyhnqta", "" + currBid );
//                            return false;
//                        }
//                        // 0 - 7
//                        // 1 - 8
//                        // 9 - 16
//                    }
//                }
//                return false;
//            }
//        });
        checkBoxAccel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean checked = ((CheckBox) buttonView).isChecked();
                if (checked){
                    isAccActive = true;
                }else{
                    isAccActive = false;
                }
            }
        });

        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!pointsOK){
                    gameView.onTouch(v, event);
                }
                return false;
            }
        });
    }

    @Override
    public void onSpinFinish(float sumCoefficient) {
        if (bidWasMade) {
            float sumWon = bidSum * sumCoefficient;
            currentSum += sumWon;

            if (currentSum < 0) {
                editor.putBoolean(Settings.GAME_OVER, true);
                startActivity(new Intent(this, GameOverActivity.class));
            }
            tvCurrentMoney.setText(currentSum + "$");
            if (sumCoefficient < 0){
                tvYouWon.setText(YOU_LOSE + sumWon * -1 + "$");
            }else {
                tvYouWon.setText(YOU_WON + sumWon + "$");
            }
            bidWasMade = false;
            pointsOK = false;
            editTextBid.setText("");
            btnClearPoiners.setEnabled(true);
            btnPointersOK.setEnabled(true);
            wasSpined= false;
        }
        wasSpined= false;
    }

    @Override
    public void wheelSpeed(int speed) {
        //TODO: can ogranicha usage of whell speed with if you should taka speed data
        if(this.bidWasMade){
            gameView.setSpeed(speed);
        }
    }

    @Override
    public void wheelIsSpinning(boolean isSpinning) {

    }

    @Override
    public void wheelWasSpined(boolean isSpined) {
        wasSpined = isSpined;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == btnBid.getId()) {
            if (editTextBid.getText().length() > 0 && Float.parseFloat(editTextBid.getText().toString()) > 0){
                this.bidSum = Float.parseFloat(editTextBid.getText().toString());
                bidWasMade = true;
                tvYouWon.setText(YOU_BID + bidSum + "$");
                if (!isAccActive){
                    wheelSpeedButton.startWheelSpeedButton();
                }
                btnBid.setEnabled(false);
                editTextBid.setEnabled(false);
                sensorData.isUsed(isAccActive);
            }
        }
        if (id == btnPointersOK.getId()) {
            if (gameView.pointerCount() > 0){
                btnBid.setEnabled(true);
                btnClearPoiners.setEnabled(false);
                btnPointersOK.setEnabled(false);
                editTextBid.setEnabled(true);
                pointsOK = true;
            }
        }
        if (id == btnClearPoiners.getId()){
            gameView.clearPointers();
        }
        if (id == btnTakeMyMoney.getId()){
            startActivity(new Intent(this, TakeMyMoneyActivity.class));
            editor.putBoolean(Settings.GAME_OVER, true);
        }
        if (id == wheelSpeedButton.getId()){
            if (bidWasMade){
                wheelSpeedButton.onClick(v);
            };
        }
        if (id == linearLayout.getId()){
//            if (editTextBid.hasFocus()) {
//                InputMethodManager imm = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//            }
        }
    }
}
