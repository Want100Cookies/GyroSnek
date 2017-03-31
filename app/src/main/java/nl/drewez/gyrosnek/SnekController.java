package nl.drewez.gyrosnek;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;

import java.io.Console;

import nl.drewez.gyrosnek.Snek.ISnek;
import nl.drewez.gyrosnek.Snek.ISnekFactory;
import nl.drewez.gyrosnek.Snek.SnekContext;
import nl.drewez.gyrosnek.Snek.SnekFactory;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;
import nl.drewez.gyrosnek.SnekFood.ISnekFoodFactory;
import nl.drewez.gyrosnek.SnekFood.SnekFoodFactory;
import nl.drewez.gyrosnek.Views.GameView;

public class SnekController implements SensorEventListener{
    private static final String TAG = SnekController.class.getSimpleName();

    private SnekContext snekContext;
    private GameView view;

    private ISnekFoodFactory foodFactory;
    private ISnekFactory snekFactory;
    private ISnekFood[] snekBar;

    private Handler tickHandler;
    private Runnable tick;
    private static final int tickTime = 100; // Tick time in ms
    private static final int foodTime = 10; // Generate food every x ticks
    private int currentTick = 0;

    private SensorManager mSensorManager;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    public SnekController(GameView view) {
        this.view = view;

        this.mSensorManager = (SensorManager) view.getContext().getSystemService(Context.SENSOR_SERVICE);

        this.foodFactory = new SnekFoodFactory();
        this.snekFactory = new SnekFactory();

        this.snekContext = new SnekContext(this.snekFactory.createSnek(this.view.getContext()));

        this.snekBar = this.foodFactory.createSnekBar(
                new ISnekFood[0],
                this.snekContext.getSnek(),
                this.view.getContext());

        this.tickHandler = new Handler();
        this.tick = new Runnable() {
            @Override
            public void run() {
                tick();

                start();
            }
        };
    }

    public void start() {
        mSensorManager.registerListener(
                this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(
                this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI);

        this.tickHandler.postDelayed(this.tick, this.tickTime);
    }

    public void pause() {
        mSensorManager.unregisterListener(this);
        tickHandler.removeCallbacks(this.tick);
    }

    public void stop() {
        this.pause();



        // Todo: get score
        // Todo: sent score to score view
    }

    public ISnek getSnek() {
        return this.snekContext.getSnek();
    }

    public ISnekFood[] getSnekBar() {
        return this.snekBar;
    }

    private void tick() {
//        ISnek snek = this.snekContext.getSnek();
//
//        snek.move(
//                this.getDirection(),
//                this.snekBar,
//                this.snekContext);
//
//        if ((++this.currentTick % this.foodTime) == 0) {
//            // Only make food every <this.foodTime> ticks
//            this.snekBar = this.foodFactory.createSnekBar(
//                    this.snekBar,
//                    snek,
//                    this.view.getContext());
//        }

        // Redraw screen
        this.view.invalidate();
    }

    public Direction getDirection() {
        this.updateOrientationAngles();

        // index 2 = negative -> down, positive -> up
        // index 1 = negative -> right, positive -> left

        if (Math.abs(mOrientationAngles[1]) > Math.abs(mOrientationAngles[2])) {
            // index 1 is biggest value
            if (mOrientationAngles[1] < 0) {
                return Direction.Right;
            } else {
                return Direction.Left;
            }

        } else {
            // index 2 is biggest value
            if (mOrientationAngles[2] < 0) {
                return Direction.Down;
            } else {
                return Direction.Up;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values,
                    0,
                    mAccelerometerReading,
                    0,
                    mAccelerometerReading.length);

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values,
                    0,
                    mMagnetometerReading,
                    0,
                    mMagnetometerReading.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateOrientationAngles() {
        mSensorManager.getRotationMatrix(
                mRotationMatrix,
                null,
                mAccelerometerReading,
                mMagnetometerReading);

        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
    }
}
