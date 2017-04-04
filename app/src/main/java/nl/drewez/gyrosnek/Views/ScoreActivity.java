package nl.drewez.gyrosnek.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import nl.drewez.gyrosnek.R;


public class ScoreActivity extends AppCompatActivity {
    private int score;

    /**
     * Set some default values for this activity
     *
     * @param savedInstanceState the savedInstance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the correct xml layout
        setContentView(R.layout.activity_score);

        // Get the extra intent value (the score)
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        // Get the textView and make it full screen
        View view = findViewById(R.id.scoreTextView);

        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Cast the view to a text view
        TextView textView = (TextView) view;

        // Set the score text and integer
        String scoreText = getResources().getString(R.string.score);
        String restartText = getResources().getString(R.string.restart_snek);

        textView.setText(scoreText + score + "\n" + restartText);
    }

    /**
     * Override the onBackPressed so the back button does not crash the game
     */
    @Override
    public void onBackPressed() {
        // no-op
    }

    /**
     * Send the player back to the game after touch on the text
     *
     * @param view The view that triggered the click
     */
    public void onClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
