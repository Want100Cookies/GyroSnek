package nl.drewez.gyrosnek.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import nl.drewez.gyrosnek.R;


public class ScoreActivity extends AppCompatActivity {
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_score);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        View view = findViewById(R.id.scoreTextView);

        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        TextView textView = (TextView) view;

        String scoreText = getResources().getString(R.string.score);
        String restartText = getResources().getString(R.string.restart_snek);

        textView.setText(scoreText + score + "\n" + restartText);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onBackPressed() {
        // no-op
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
