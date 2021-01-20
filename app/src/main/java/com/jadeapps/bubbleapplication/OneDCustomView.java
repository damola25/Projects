package com.jadeapps.bubbleapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class OneDCustomView extends View  implements SensorEventListener {

    private static final String TAG = "OneDCustomView";
    private final int padX2 = 100;
    private final int padY = 20;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private SensorEvent[] accelerometerSensorDataLogger;

    Paint redPaint;

    private int customViewWidth;
    private int customViewHeight;
    private int xPadWidth, oneDRectangleWidth, oneDRectangleHeight;

    private Integer oneDPlaneLength;

    public OneDCustomView(Context context) {
        super(context);
        init();
    }

    public OneDCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OneDCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if (width > height) {
            Integer half = (Integer) width/2;
            oneDRectangleWidth = half - padX2;
            xPadWidth =  ((Integer) ((half - oneDRectangleWidth)/2));
        } else if (width < height) {
            oneDRectangleWidth = width - padX2;
            xPadWidth =  ((Integer) ((width - oneDRectangleWidth)/2));
        }

        oneDRectangleHeight = 100;

        Log.d(TAG, "init: Initializing Sensor Services");
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "init: Registered accelerometer listener");
        } else {
            Log.d(TAG, "init: Accelerometer NOT supported on this device!!!");
            Toast.makeText(getContext(), "Accelerometer NOT supported on this device!!!", Toast.LENGTH_LONG).show();
        }
        accelerometerSensorDataLogger = new SensorEvent[]{};

        oneDPlaneLength = (Integer) width/6;

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(this.getResources().getColor(R.color.design_default_color_error));
        redPaint.setTextAlign(Paint.Align.CENTER);
        redPaint.setTextSize(50f);
        redPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Integer half = 0;

        if (width > height) {
            half = (Integer) width/2;
            customViewWidth = half;
            customViewHeight = height;
            oneDRectangleWidth = half - padX2;
            xPadWidth =  ((Integer) ((half - oneDRectangleWidth)/2));
            setMeasuredDimension(half, 150);
        } else {
            half = (Integer) height/2;
            customViewHeight = half;
            customViewWidth = width;
            oneDRectangleWidth = width - padX2;
            xPadWidth =  ((Integer) ((width - oneDRectangleWidth)/2));
            setMeasuredDimension(width, 150);
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText("1D Custom View", oneDPlaneLength*3, 100, redPaint);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerSensorDataLogger = updateSensorDataLoggerInstance(accelerometerSensorDataLogger.length, accelerometerSensorDataLogger, sensorEvent);
            Log.d(TAG, "onSensorChanged: Custom View 1D ACCELEROMETER sensor data length " + accelerometerSensorDataLogger.length);
        }
    }

    private SensorEvent[] updateSensorDataLoggerInstance(int loggerLength, SensorEvent[] selectedSensorDataLogger, SensorEvent event) {
        if (loggerLength >= 500) {
            return updateSensorDataByShiftOperations(selectedSensorDataLogger, event);
        } else {
            return updateSensorDataByAddOperations(loggerLength, selectedSensorDataLogger, event);
        }
    }

    private SensorEvent[] updateSensorDataByShiftOperations(SensorEvent[] selectedSensorDataLogger, SensorEvent event) {
        SensorEvent[] newSelectedSensorDataLogger = new SensorEvent[500];

        for (int i=499; i>=0; i--) {
            if (i > 0) {
                newSelectedSensorDataLogger[i] = selectedSensorDataLogger[i-1];
            } else if (i == 0) {
                newSelectedSensorDataLogger[i] = event;
            }
        }

        return newSelectedSensorDataLogger;
    }

    private SensorEvent[] updateSensorDataByAddOperations(int loggerLength, SensorEvent[] selectedSensorDataLogger, SensorEvent event) {
        SensorEvent[] newSelectedSensorDataLogger = new SensorEvent[loggerLength+1];
        for (int i=0; i<loggerLength; i++) {
            newSelectedSensorDataLogger[i] = selectedSensorDataLogger[i];
        }
        newSelectedSensorDataLogger[loggerLength] = event;
        return newSelectedSensorDataLogger;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
