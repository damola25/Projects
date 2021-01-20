package com.jadeapps.bubbleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    OneDCustomView oneDCustomView;
    TwoDCustomView twoDCustomView;

    LinearLayout customViewLinearLayout, informationLinearLayout;

    String screenOrientation;

    RelativeLayout.LayoutParams oneDCustomViewRelative =  new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    );

    RelativeLayout.LayoutParams twoDCustomViewRelative =  new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    );

    LinearLayout.LayoutParams  params;

    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oneDCustomView = (OneDCustomView) findViewById(R.id.oneDCustomView);
        twoDCustomView = (TwoDCustomView) findViewById(R.id.twoDCustomView);

        customViewLinearLayout = (LinearLayout) findViewById(R.id.customViewLinearLayout);
        informationLinearLayout = (LinearLayout) findViewById(R.id.informationLinearLayout);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        System.out.println(String.format("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000>>>          Start Margin value %s", oneDCustomViewRelative.getMarginStart()));
        System.out.println(String.format("0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000>>>          End Margin value %s", oneDCustomViewRelative.getMarginEnd()));


        int orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_UNDEFINED:
                screenOrientation = "Undefined";
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                screenOrientation = "Landscape";
//                try {
//                    oneDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
//                    twoDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                } catch (Exception ex) {}
//                twoDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                oneDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                oneDCustomView.setLayoutParams(oneDCustomViewRelative);
//                twoDCustomView.setLayoutParams(twoDCustomViewRelative);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                screenOrientation = "Portrait";
//                try {
//                    oneDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                    twoDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                } catch (Exception ex) {}
//                twoDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                oneDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                oneDCustomView.setLayoutParams(oneDCustomViewRelative);
//                twoDCustomView.setLayoutParams(twoDCustomViewRelative);
                break;
            default:
                screenOrientation = "Square";
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenOrientation = "Landscape";
            try {
                oneDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                twoDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } catch (Exception ex) {}
            twoDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            oneDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            customViewLinearLayout.setLayoutParams(oneDCustomViewRelative);
            informationLinearLayout.setLayoutParams(twoDCustomViewRelative);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            screenOrientation = "Portrait";
            try {
                oneDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                twoDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            } catch (Exception ex) {}
            twoDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            oneDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            customViewLinearLayout.setLayoutParams(oneDCustomViewRelative);
            informationLinearLayout.setLayoutParams(twoDCustomViewRelative);
        }
    }
}