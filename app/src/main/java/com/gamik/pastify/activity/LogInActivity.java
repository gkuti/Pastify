package com.gamik.pastify.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gamik.pastify.R;
import com.gamik.pastify.store.DataStore;

public class LogInActivity extends AppCompatActivity {
    private int clicks = 0;
    private String passcode = "";
    private ImageView fieldOne;
    private ImageView fieldTwo;
    private ImageView fieldThree;
    private ImageView fieldFour;
    private DataStore dataStore;
    private String userPass;
    private Button bt_one, bt_two, bt_three, bt_four, bt_five, bt_six, bt_seven, bt_eight, bt_nine, bt_zero, bt_back, bt_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        dataStore = new DataStore(this);
        userPass = dataStore.getData("passCode");
        fieldOne = (ImageView) findViewById(R.id.field_1);
        fieldTwo = (ImageView) findViewById(R.id.field_2);
        fieldThree = (ImageView) findViewById(R.id.field_3);
        fieldFour = (ImageView) findViewById(R.id.field_4);
        TextView textView = (TextView) findViewById(R.id.tv_login);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "century_gothic.ttf");
        textView.setTypeface(typeface);
        //changeFont();
    }

    public void numberClick(View view) {
        Button button = (Button) view;
        clicks++;
        passcode += button.getText();
        fillImage(clicks);
        if (clicks == 4) {
            checkPasscode();
        }
    }

    public void fillImage(int i) {
        switch (i) {
            case 1:
                fieldOne.setImageDrawable(getResources().getDrawable(R.drawable.shadow_filled_circle));
                break;
            case 2:
                fieldTwo.setImageDrawable(getResources().getDrawable(R.drawable.shadow_filled_circle));
                break;
            case 3:
                fieldThree.setImageDrawable(getResources().getDrawable(R.drawable.shadow_filled_circle));
                break;
            case 4:
                fieldFour.setImageDrawable(getResources().getDrawable(R.drawable.shadow_filled_circle));
                break;
        }
    }

    public void checkPasscode() {
        if (passcode.equals(userPass)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            restart();
        }
    }

    private void restart() {
        YoYo.with(Techniques.Shake).duration(750).playOn(findViewById(R.id.circle_frame_log));
        fieldOne.setImageDrawable(getResources().getDrawable(R.drawable.circle));
        fieldTwo.setImageDrawable(getResources().getDrawable(R.drawable.circle));
        fieldThree.setImageDrawable(getResources().getDrawable(R.drawable.circle));
        fieldFour.setImageDrawable(getResources().getDrawable(R.drawable.circle));
        clicks = 0;
        passcode = "";
    }

    private void changeFont() {
        bt_one = (Button) findViewById(R.id.button_one);
        bt_two = (Button) findViewById(R.id.button_two);
        bt_three = (Button) findViewById(R.id.button_three);
        bt_four = (Button) findViewById(R.id.button_four);
        bt_five = (Button) findViewById(R.id.button_five);
        bt_six = (Button) findViewById(R.id.button_six);
        bt_seven = (Button) findViewById(R.id.button_seven);
        bt_eight = (Button) findViewById(R.id.button_eight);
        bt_nine = (Button) findViewById(R.id.button_nine);
        bt_zero = (Button) findViewById(R.id.button_zero);
        bt_back = (Button) findViewById(R.id.button_back);
        bt_clear = (Button) findViewById(R.id.button_clear);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "arial_rounded_bold.ttf");
        bt_one.setTypeface(typeface);
        bt_two.setTypeface(typeface);
        bt_three.setTypeface(typeface);
        bt_four.setTypeface(typeface);
        bt_five.setTypeface(typeface);
        bt_six.setTypeface(typeface);
        bt_seven.setTypeface(typeface);
        bt_eight.setTypeface(typeface);
        bt_nine.setTypeface(typeface);
        bt_zero.setTypeface(typeface);
        bt_back.setTypeface(typeface);
        bt_clear.setTypeface(typeface);
    }
}