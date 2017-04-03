package nl.drewez.gyrosnek;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.drewez.gyrosnek.Snek.ISnek;
import nl.drewez.gyrosnek.Snek.ISnekFactory;
import nl.drewez.gyrosnek.Snek.InvisibleSnek;
import nl.drewez.gyrosnek.Snek.RainbowSnek;
import nl.drewez.gyrosnek.Snek.Score;
import nl.drewez.gyrosnek.Snek.SnekContext;
import nl.drewez.gyrosnek.Snek.SnekFactory;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.Snek.SpeedSnek;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;
import nl.drewez.gyrosnek.SnekFood.ISnekFoodFactory;
import nl.drewez.gyrosnek.SnekFood.SnekFoodFactory;
import nl.drewez.gyrosnek.Views.GameView;

public class SnekController implements SensorEventListener {
    private static final String TAG = SnekController.class.getSimpleName();

    private SnekContext snekContext;
    private GameView view;

    private ISnekFoodFactory foodFactory;
    private ISnekFactory snekFactory;
    private ISnekFood[] snekBar;

    private Handler tickHandler;
    private Runnable tick;
    private static int tickTime = 1000; // Tick time in ms
    private static final int foodTime = 20; // Generate food every x ticks
    private int currentTick = 0;

    private SensorManager mSensorManager;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    public SnekController(GameView view) {
        this.view = view;

        mSensorManager = (SensorManager) view.getContext().getSystemService(Context.SENSOR_SERVICE);

        foodFactory = new SnekFoodFactory();
        snekFactory = new SnekFactory();

        snekContext = new SnekContext(snekFactory.createSnek(view.getContext()));

        snekBar = foodFactory.createSnekBar(
                new ISnekFood[0],
                snekContext.getSnek(),
                view.getContext());

        tickHandler = new Handler();
        tick = new Runnable() {
            @Override
            public void run() {
                if (tick()) {
                    tickHandler.postDelayed(tick, tickTime);
                }
                // if !tick, stop ticking
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

        tick.run();
    }

    public void pause() {
        mSensorManager.unregisterListener(this);
        tickHandler.removeCallbacks(tick);
    }

    public void stop() {
        pause();
        view.startScoreActivity();
    }

    public ISnek getSnek() {
        return snekContext.getSnek();
    }

    public ISnekFood[] getSnekBar() {
        return snekBar;
    }

    public IDrawable[] getDrawables() {
        ISnekPart[] snekParts = snekContext.getSnek().getSnekParts();
        IDrawable[] drawables = new IDrawable[snekParts.length + snekBar.length];

        System.arraycopy(snekParts, 0, drawables, 0, snekParts.length);
        System.arraycopy(snekBar, 0, drawables, snekParts.length, snekBar.length);

        return drawables;
    }

    public Score getScore() {
        return snekContext.getSnek().getScore();
    }

    private boolean tick() {
        ISnek snek = this.snekContext.getSnek();

        boolean canMove = snek.move(
                this.getDirection(),
                this.snekBar,
                this.snekContext);

        this.snekBar = removeNulls(this.snekBar);

        if (snekContext.getSnek() instanceof SpeedSnek) {
            tickTime = 500;
        }
        else  {
            tickTime = 1000;
        }

        if (!canMove) {
            stop();
            return false;
        }

        if ((++this.currentTick % foodTime) == 0) {
            // Only make food every <this.foodTime> ticks
            this.snekBar = this.foodFactory.createSnekBar(
                    this.snekBar,
                    snek,
                    this.view.getContext());
        }

        // Redraw screen
        view.invalidate();
        return true;
    }

    private Direction getDirection() {
        updateOrientationAngles();

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
        // no-op
    }

    private void updateOrientationAngles() {
        SensorManager.getRotationMatrix(
                mRotationMatrix,
                null,
                mAccelerometerReading,
                mMagnetometerReading);

        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
    }

    private ISnekFood[] removeNulls(ISnekFood[] objects) {
        List<ISnekFood> newList = new ArrayList<>();

        for (ISnekFood obj : objects) {
            if (obj != null) {
                newList.add(obj);
            }
        }

        return newList.toArray(new ISnekFood[0]);
    }
}
