package com.jadeapps.bubbleapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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

public class OneDCustomView extends View implements SensorEventListener {

    private static final String TAG = "OneDCustomView";
    private final int padX2 = 100;
    private final int padY = 20;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private SensorEvent[] accelerometerSensorDataLogger;

    private int customViewWidth;
    private int customViewHeight;
    private int xPadWidth, oneDRectangleWidth, oneDRectangleHeight;


    Paint redPaint, bubblePaint;

    static private final double GRAVITY = 9.81d;
    static private final double MIN_DEGREE = -10d;
    static private final double MAX_DEGREE = 10d;

    private Rect oneDRectangle;
    private Paint oneDRectanglePaint;
    private Bitmap oneDRectangleBitmap;

    private double angleInDegreesXAxis, angleInDegreesYAxis, minXValue, maxXValue;

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

        minXValue = 10000d;
        maxXValue = -10000d;

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

        oneDRectangleBitmap = Bitmap.createBitmap(
                oneDRectangleWidth, // Width
                oneDRectangleHeight, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        oneDPlaneLength = (Integer) width/6;
        oneDRectangle = new Rect(oneDPlaneLength, padY, ((oneDPlaneLength*4)+oneDPlaneLength), (oneDRectangleHeight+padY));


        oneDRectanglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oneDRectanglePaint.setStyle(Paint.Style.FILL);
        oneDRectanglePaint.setColor(Color.YELLOW);

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(this.getResources().getColor(R.color.design_default_color_error));
        redPaint.setTextAlign(Paint.Align.CENTER);
        redPaint.setTextSize(50f);
        redPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bubblePaint.setStyle(Paint.Style.FILL);
        bubblePaint.setColor(Color.GREEN);

        angleInDegreesXAxis = 0d;
        angleInDegreesYAxis = 0d;
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

        canvas.drawRect(oneDRectangle, oneDRectanglePaint);

        canvas.drawLine(computeLineLocationOnCustomView(MAX_DEGREE), padY, computeLineLocationOnCustomView(MAX_DEGREE), (oneDRectangleHeight+padY), redPaint);
        canvas.drawLine(computeLineLocationOnCustomView(MIN_DEGREE), padY, computeLineLocationOnCustomView(MIN_DEGREE), (oneDRectangleHeight+padY), redPaint);

        canvas.drawCircle(computeLineLocationOnCustomView(angleInDegreesYAxis), (((Integer)(oneDRectangleHeight/2))+padY), (oneDRectangleHeight/3), bubblePaint);
    }

    public double getMinXValue() {
        for (int i=0; i<accelerometerSensorDataLogger.length; i++) {
            if (accelerometerSensorDataLogger[i].values[0] < minXValue) {
                minXValue = accelerometerSensorDataLogger[i].values[0];
            }
        }
        return minXValue;
    }

    public double getMaxXValue() {
        for (int i=0; i<accelerometerSensorDataLogger.length; i++) {
            if (accelerometerSensorDataLogger[i].values[0] > maxXValue) {
                maxXValue = accelerometerSensorDataLogger[i].values[0];
            }
        }
        return maxXValue;
    }

    private int computeLineLocationOnCustomView(double angle){
        Double value =  ( - angle + 260d) * 2.1d;
        return value.intValue();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onSensorChanged: ACCELEROMETER sensor data has been updated ...");
            accelerometerSensorDataLogger = updateSensorDataLoggerInstance(accelerometerSensorDataLogger.length, accelerometerSensorDataLogger, sensorEvent);
        }

        double gravityXCoords = sensorEvent.values[0] > GRAVITY ? GRAVITY : sensorEvent.values[0];
        double gravityYCoords = sensorEvent.values[1] > GRAVITY ? GRAVITY : sensorEvent.values[1];
        double gravityZCoords = sensorEvent.values[2];

        gravityXCoords = gravityXCoords < -GRAVITY ? -GRAVITY : gravityXCoords;
        gravityYCoords = gravityYCoords < -GRAVITY ? -GRAVITY : gravityYCoords;

        angleInDegreesXAxis = Math.toDegrees(Math.asin(gravityYCoords/GRAVITY));
        angleInDegreesYAxis = Math.toDegrees(Math.asin(gravityXCoords/GRAVITY));

        invalidate();
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

    public void reset() {
        minXValue = 10000d;
        maxXValue = -10000d;
    }
}
