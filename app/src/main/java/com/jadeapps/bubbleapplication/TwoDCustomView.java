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

public class TwoDCustomView extends View implements SensorEventListener  {

    private static final String TAG = "TwoDCustomView";
    private final int padX2 = 100;
    private final int padY = 20;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private SensorEvent[] accelerometerSensorDataLogger;

    private int customViewWidth;
    private int customViewHeight;
    private int xPadWidth, twoDRectangleWidth, twoDRectangleHeight;

    Paint redPaint, bubblePaint;

    static private final double GRAVITY = 9.81d;
    static private final double MIN_DEGREE = -10d;
    static private final double MAX_DEGREE = 10d;


    private Rect twoDRectangle;
    private Paint twoDRectanglePaint;
    private Bitmap twoDRectangleBitmap;

    private int screenWidth = 0;

    private int rotationAngle;

    private double angleInDegreesXAxis, angleInDegreesYAxis, minXValue, maxXValue, minYValue, maxYValue;

    private Integer twoDPlaneLength;

    public TwoDCustomView(Context context) {
        super(context);
        init();
    }

    public TwoDCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoDCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        screenWidth = width;

        if (width > height) {
            Integer half = (Integer) width/2;
            customViewWidth = half;
            customViewHeight = height;
            xPadWidth =  ((Integer) ((half - twoDRectangleWidth)/2));
        } else if (width < height) {
            customViewWidth = width - padX2;
            customViewHeight = (Integer) height/2;
            xPadWidth =  ((Integer) ((width - twoDRectangleWidth)/2));
        }

        twoDRectangleHeight = 100;
        rotationAngle = 0;

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

        twoDRectangleBitmap = Bitmap.createBitmap(
                customViewWidth, // Width
                customViewHeight, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        twoDPlaneLength = (Integer) width/6;
        twoDRectangle = new Rect(twoDPlaneLength, 50, ((twoDPlaneLength*4)+twoDPlaneLength), ((twoDPlaneLength*4)+twoDPlaneLength));

        twoDRectanglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        twoDRectanglePaint.setStyle(Paint.Style.FILL);
        twoDRectanglePaint.setColor(Color.BLACK);

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(Color.RED);
        redPaint.setTextAlign(Paint.Align.CENTER);
        redPaint.setTextSize(50f);
        redPaint.setStrokeWidth(getContext().getResources().getDisplayMetrics().density * 2);
        redPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bubblePaint.setStyle(Paint.Style.FILL);
        bubblePaint.setColor(Color.GREEN);

        angleInDegreesXAxis = 0d;
        angleInDegreesYAxis = 0d;

        minXValue = 10000d;
        maxXValue = -10000d;
        minYValue = 10000d;
        maxYValue = -10000d;
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
            customViewHeight = height- 50;
            setMeasuredDimension(half, customViewHeight);
        } else {
            half = (Integer) height/2;
            customViewHeight = half - 50;
            customViewWidth = width;
            setMeasuredDimension(width, customViewHeight);
        }
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = (Integer)(((twoDPlaneLength*4))/2);
        canvas.drawCircle((twoDPlaneLength+radius), (50+radius), radius, twoDRectanglePaint);
        canvas.save();
        canvas.rotate(rotationAngle, (twoDPlaneLength+radius), (50+radius));

        int textWidth = (int)redPaint.measureText("N");
        int textHeight = (int)redPaint.measureText("yY");
        int halfScreenWidth = (twoDPlaneLength + 3 + ((Integer)(twoDPlaneLength*4)/2));
        int halfScreenTop = -20;

        for (int i = 0; i < 24; i++) {
            canvas.translate(0, textHeight);
            if (i % 6 == 0) {
                String dirString = "";
                switch (i) {
                    case(0) : dirString = "N"; break;
                    default:  dirString = "";  break;
                }
                canvas.drawText(dirString, halfScreenWidth, halfScreenTop, redPaint);
            }
            canvas.rotate(15, (twoDPlaneLength+radius), (50+radius));
        }

        canvas.drawLine(computeLineLocationOnCustomViewY(MAX_DEGREE), 50, computeLineLocationOnCustomViewY(MAX_DEGREE), ((twoDPlaneLength*4)+50), redPaint);
        canvas.drawLine(computeLineLocationOnCustomViewY(MIN_DEGREE), 50, computeLineLocationOnCustomViewY(MIN_DEGREE), ((twoDPlaneLength*4)+50), redPaint);

        canvas.drawLine(twoDPlaneLength, computeLineLocationOnCustomViewX(MAX_DEGREE),  ((twoDPlaneLength*4)+twoDPlaneLength), computeLineLocationOnCustomViewX(MAX_DEGREE), redPaint);
        canvas.drawLine(twoDPlaneLength, computeLineLocationOnCustomViewX(MIN_DEGREE),  ((twoDPlaneLength*4)+twoDPlaneLength), computeLineLocationOnCustomViewX(MIN_DEGREE), redPaint);

        canvas.drawCircle(computeLineLocationOnCustomViewY(angleInDegreesYAxis), computeLineLocationOnCustomViewX(angleInDegreesXAxis), (twoDRectangleHeight/3), bubblePaint);
    }

    private int computeLineLocationOnCustomViewX(double angle){
        Double value =  ( - angle + 190d) * 2.1d;
        return value.intValue();
    }

    private int computeLineLocationOnCustomViewY(double angle){
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

    public double getMinYValue() {
        for (int i=0; i<accelerometerSensorDataLogger.length; i++) {
            if (accelerometerSensorDataLogger[i].values[1] < minYValue) {
                minYValue = accelerometerSensorDataLogger[i].values[1];
            }
        }
        return minYValue;
    }

    public double getMaxYValue() {
        for (int i=0; i<accelerometerSensorDataLogger.length; i++) {
            if (accelerometerSensorDataLogger[i].values[1] > maxYValue) {
                maxYValue = accelerometerSensorDataLogger[i].values[1];
            }
        }
        return maxYValue;
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
        minYValue = 10000d;
        maxYValue = -10000d;
    }

    public void rotateCoordinateDirection(int angularAdjustment) {
        rotationAngle += angularAdjustment;
        if (rotationAngle >= 360) {
            rotationAngle = 0;
        }
        invalidate();
    }
}
