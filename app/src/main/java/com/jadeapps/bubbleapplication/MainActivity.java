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

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    OneDCustomView oneDCustomView;
    TwoDCustomView twoDCustomView;

    LinearLayout customViewLinearLayout, informationLinearLayout;
    TextView oneDMinValue, oneDMaxValue,twoDMinXValue, twoDMaxXValue, twoDMinYValue, twoDMaxYValue;

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

    Timer timer;

    String oneDMin, oneDMax, twoDXMin, twoDXMax, twoDYMin, twoDYMax;

    int timeCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oneDCustomView = (OneDCustomView) findViewById(R.id.oneDCustomView);
        twoDCustomView = (TwoDCustomView) findViewById(R.id.twoDCustomView);

        oneDMinValue = (TextView) findViewById(R.id.oneDMinValue);
        oneDMaxValue = (TextView) findViewById(R.id.oneDMaxValue);
        twoDMinXValue = (TextView) findViewById(R.id.twoDMinXValue);
        twoDMaxXValue = (TextView) findViewById(R.id.twoDMaxXValue);
        twoDMinYValue = (TextView) findViewById(R.id.twoDMinYValue);
        twoDMaxYValue = (TextView) findViewById(R.id.twoDMaxYValue);

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
                try {
                    oneDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                    twoDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                } catch (Exception ex) {}
                twoDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                oneDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                customViewLinearLayout.setLayoutParams(oneDCustomViewRelative);
                informationLinearLayout.setLayoutParams(twoDCustomViewRelative);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                screenOrientation = "Portrait";
                try {
                    oneDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    twoDCustomViewRelative.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                } catch (Exception ex) {}
                twoDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                oneDCustomViewRelative.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                customViewLinearLayout.setLayoutParams(oneDCustomViewRelative);
                informationLinearLayout.setLayoutParams(twoDCustomViewRelative);
                break;
            default:
                screenOrientation = "Square";
                break;
        }

        oneDMin = "0.00";
        oneDMax = "0.00";
        twoDXMin = "0.00";
        twoDXMax = "0.00";
        twoDYMin = "0.00";
        twoDYMax = "0.00";

        oneDMinValue.setText(oneDMin);
        oneDMaxValue.setText(oneDMax);
        twoDMinXValue.setText(twoDXMin);
        twoDMaxXValue.setText(twoDXMax);
        twoDMinYValue.setText(twoDYMin);
        twoDMaxYValue.setText(twoDYMax);

        timer = new Timer();
        updateInformationOnUIComponents();
    }


    private void updateInformationOnUIComponents() {
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DecimalFormat df = new DecimalFormat("####0.00");

                        if (!oneDMin.equals(getFormatedString(oneDCustomView.getMinXValue()))) {
                            oneDMin = getFormatedString(oneDCustomView.getMinXValue());
                            oneDMinValue.setText(oneDMin);
                        }

                        if (!oneDMax.equals(getFormatedString(oneDCustomView.getMaxXValue()))) {
                            oneDMax = getFormatedString(oneDCustomView.getMaxXValue());
                            oneDMaxValue.setText(oneDMax);
                        }

                        if (!twoDXMin.equals(getFormatedString(twoDCustomView.getMinXValue()))) {
                            twoDXMin = getFormatedString(twoDCustomView.getMinXValue());
                            twoDMinXValue.setText(twoDXMin);
                        }

                        if (!twoDXMax.equals(getFormatedString(twoDCustomView.getMaxXValue()))) {
                            twoDXMax = getFormatedString(twoDCustomView.getMaxXValue());
                            twoDMaxXValue.setText(twoDXMax);
                        }

                        if (!twoDYMin.equals(getFormatedString(twoDCustomView.getMinYValue()))) {
                            twoDYMin = getFormatedString(twoDCustomView.getMinYValue());
                            twoDMinYValue.setText(twoDYMin);
                        }

                        if (!twoDYMax.equals(getFormatedString(twoDCustomView.getMaxYValue()))) {
                            twoDYMax = getFormatedString(twoDCustomView.getMaxYValue());
                            twoDMaxYValue.setText(twoDYMax);
                        }
                    }
                });
            }
        },0,1000L);
    }

    private String getFormatedString(double value) {
        return String.format("%.5f", value);
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