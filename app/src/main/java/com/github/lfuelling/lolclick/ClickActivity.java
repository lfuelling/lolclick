package com.github.lfuelling.lolclick;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ClickActivity extends AppCompatActivity {

    private ImageButton clickBtn;
    private TextView scoreView;
    private Integer score = 0;
    private Animation blinkAnimation;
    private Animation pressAnimation;
    private Long stopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click);

        blinkAnimation = AnimationUtils.loadAnimation(ClickActivity.this, R.anim.blink);
        pressAnimation = AnimationUtils.loadAnimation(ClickActivity.this, R.anim.press);

        scoreView = (TextView) findViewById(R.id.scoreView);

        clickBtn = (ImageButton) findViewById(R.id.click_btn);
        clickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score++;
                scoreView.setText(String.valueOf(score));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Setting the button's background only works on API 21 or above
                    if (score == 1) {
                        stopwatch = System.currentTimeMillis();
                        clickBtn.startAnimation(blinkAnimation);
                        Toast.makeText(ClickActivity.this, R.string.keep_going, Toast.LENGTH_LONG).show();
                        clickBtn.setBackground(getDrawable(R.mipmap.ic_gusta));
                    } else if (score == 50) {
                        clickBtn.startAnimation(blinkAnimation);
                        Toast.makeText(ClickActivity.this, "You needed " + String.valueOf((System.currentTimeMillis() - stopwatch) / 1000) + " seconds!", Toast.LENGTH_LONG).show();
                        // checking using '==' instead of '>=' because the background only needs to be et once.
                        clickBtn.setBackground(getDrawable(R.mipmap.ic_lol));
                    } else {
                        clickBtn.startAnimation(pressAnimation);
                    }
                }
            }
        });
    }
}
