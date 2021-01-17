package com.jadeapps.bubbleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView orientationStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orientationStatusTextView = findViewById(R.id.orientationStatusTextView);

        int orientation = getResources().getConfiguration().orientation;
        switch (orientation)
        {
            case Configuration.ORIENTATION_UNDEFINED:
                orientationStatusTextView.setText("Screen Orientation: \n Undefined!!!.");
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                orientationStatusTextView.setText("Screen Orientation: \n Landscape.");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                orientationStatusTextView.setText("Screen Orientation: \n Portrait.");
                break;
            default:
                orientationStatusTextView.setText("Screen Orientation: \n Square.");
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientationStatusTextView.setText("Screen Orientation: \n Landscape.");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            orientationStatusTextView.setText("Screen Orientation: \n Portrait.");
        }
    }
}