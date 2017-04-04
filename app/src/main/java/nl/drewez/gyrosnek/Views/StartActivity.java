package nl.drewez.gyrosnek.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nl.drewez.gyrosnek.R;

public class StartActivity extends AppCompatActivity {

    //field for the view
    private View mContentView;

    /**
    * Sets certain aspects of the instance
    * @param savedInstanceState retrieves the state of the instance
    * @return void
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        mContentView = findViewById(R.id.fullscreen_content);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //Hides the actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //Stores the StartActivity this in _this
        final StartActivity _this = this;

        //delays the runnable, which allows to show the bootscreen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(_this, GameActivity.class);
                startActivity(intent);
            }
        }, 2000);

    }
}
