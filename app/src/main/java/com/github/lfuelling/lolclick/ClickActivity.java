package com.github.lfuelling.lolclick;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ClickActivity extends AppCompatActivity {

    private static final String TAG = ClickActivity.class.getCanonicalName();
    private ImageButton clickBtn;
    private TextView scoreView;
    private Integer score = 0;
    private Animation blinkAnimation;
    private Animation pressAnimation;
    private Animation multiAnimation;
    private Long stopwatch;
    private ImageView multiView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);

        blinkAnimation = AnimationUtils.loadAnimation(ClickActivity.this, R.anim.blink);
        pressAnimation = AnimationUtils.loadAnimation(ClickActivity.this, R.anim.press);
        multiAnimation = AnimationUtils.loadAnimation(ClickActivity.this, R.anim.multi);

        scoreView = (TextView) findViewById(R.id.scoreView);
        multiView = (ImageView) findViewById(R.id.multiplierView);

        clickBtn = (ImageButton) findViewById(R.id.click_btn);
        final Snackbar unsupportedSnackbar = Snackbar.make(clickBtn, R.string.not_supported, Snackbar.LENGTH_LONG);
        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score++;
                Log.d(TAG, "There's a click! New score: " + score);
                scoreView.setText(String.valueOf(score));
                int anInt = new Random().nextInt(score);
                if (score == 1) {
                    Log.d(TAG, "First click! Starting stopwatch...");
                    stopwatch = System.currentTimeMillis();
                    clickBtn.startAnimation(blinkAnimation);
                    Toast.makeText(ClickActivity.this, R.string.keep_going, Toast.LENGTH_LONG).show();
                    setBgOrWarn(clickBtn, unsupportedSnackbar, R.mipmap.ic_gusta);

                } else if (score % 50 == 0) {
                    // checking using '==' instead of '>=' because the background only needs to be set once.
                    setBgOrWarn(clickBtn, unsupportedSnackbar, R.mipmap.ic_lol);
                    clickBtn.startAnimation(blinkAnimation);
                    String state = String.valueOf((System.currentTimeMillis() - stopwatch) / 1000);
                    Log.d(TAG, "We're at " + state + " now");
                    Toast.makeText(ClickActivity.this, "You needed " + state + " seconds!", Toast.LENGTH_LONG).show();
                } else if (score % ((anInt <= 0) ? 75 : anInt) == 0) {
                    Log.i(TAG, "Multi 2x");
                    multiply(2);
                    setBgOrWarn(clickBtn, unsupportedSnackbar, R.mipmap.ic_gusta);
                    setBgOrWarn(multiView, unsupportedSnackbar, R.mipmap.multi2x);
                    multiView.setVisibility(View.VISIBLE);
                    multiView.startAnimation(multiAnimation);
                } else if (anInt <= 0) {
                    // This will be interesting
                    Log.i(TAG, "Multi 4x");
                    multiply(4);
                    setBgOrWarn(clickBtn, unsupportedSnackbar, R.mipmap.ic_gusta);
                    setBgOrWarn(multiView, unsupportedSnackbar, R.mipmap.multi4x);
                    multiView.setVisibility(View.VISIBLE);
                    multiView.startAnimation(multiAnimation);
                    clickBtn.startAnimation(blinkAnimation);
                } else {
                    clickBtn.startAnimation(pressAnimation);
                    if (multiView.getVisibility() == View.VISIBLE) {
                        multiView.setVisibility(View.GONE);
                    }
                }
            }


        }

        );
    }

    private void multiply(int i) {
        for (int i1 = 2; i >= i1; i1++) {
            Log.d(TAG, "MULTIPLY!!!");
            score++;
        }
    }


    private void setBgOrWarn(ImageView view, Snackbar snackbar, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (view instanceof ImageButton) {
                view.setBackground(getDrawable(resId));
            } else {
                view.setImageResource(resId);

            }
        } else {
            if (!snackbar.isShown()) {
                snackbar.show();
            }
        }
    }

}
