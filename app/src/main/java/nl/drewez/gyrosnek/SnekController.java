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

    //the snekcontext en gameview field
    private SnekContext snekContext;
    private GameView view;

    //the snek food fields
    private ISnekFoodFactory foodFactory;
    private ISnekFactory snekFactory;
    private ISnekFood[] snekBar;

    //fields for the tick/timer or gametime
    private Handler tickHandler;
    private Runnable tick;

    private static final int speedSnekTickTime = 250;
    private static final int normalSnekTickTime = 500;

    private static int tickTime = normalSnekTickTime; // Tick time in ms

    private static final int foodTime = 20; // Generate food every x ticks
    private int currentTick = 0;

    //fields for the gyroscope in your phone
    private SensorManager mSensorManager;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    /**
     * The gamecontroller class, which is used to update the view
     *
     * @param view used to update the view when changes occur
     */
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

    /**
     * Start instance of the game, which makes the tick run and enables the sensors
     *
     * @return void
     */
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

    /**
     * pauses the game and disables the gyrosensors
     */
    public void pause() {
        mSensorManager.unregisterListener(this);
        tickHandler.removeCallbacks(tick);
    }

    /**
     * Start instance of the game, which makes the tick run and enables the sensors
     */
    public void stop() {
        pause();

        Score score = getScore();

        snekContext.setSnek(snekFactory.createSnek(view.getContext()));

        view.startScoreActivity(score);
    }

    /**
     * retrieve snek information
     *
     * @return the current snek
     */
    public ISnek getSnek() {
        return snekContext.getSnek();
    }

    /**
     * retrieve snek food information
     *
     * @return snekBar
     */
    public ISnekFood[] getSnekBar() {
        return snekBar;
    }

    /**
     * retrieves the objects that need to be drawn on the canvas
     *
     * @return drawables, the objects need to be drawn on canvas/screen
     */
    public IDrawable[] getDrawables() {
        ISnekPart[] snekParts = snekContext.getSnek().getSnekParts();
        IDrawable[] drawables = new IDrawable[snekParts.length + snekBar.length];

        System.arraycopy(snekParts, 0, drawables, 0, snekParts.length);
        System.arraycopy(snekBar, 0, drawables, snekParts.length, snekBar.length);

        return drawables;
    }

    /**
     * retrieves the player score
     *
     * @return the score of the player
     */
    public Score getScore() {
        return snekContext.getSnek().getScore();
    }

    /**
     * a repeated action used for events in the game
     *
     * @return true or false, depending if the game continues
     */
    private boolean tick() {
        // Get the current snek from the snekContext
        ISnek snek = this.snekContext.getSnek();

        // Move the snek in the direction
        boolean canMove = snek.move(
                this.getDirection(),
                this.snekBar,
                this.snekContext);

        // If you can not move
        if (!canMove) {
            // Stop the game and return false to the ticker
            stop();
            return false;
        }

        // Remove the null foods (those are eaten)
        this.snekBar = removeNulls(this.snekBar);

        // Increase speed in case the current snek is speedSnek
        if (snek instanceof SpeedSnek) {
            tickTime = speedSnekTickTime;
        } else {
            tickTime = normalSnekTickTime;
        }

        // Update the every foodTime ticks
        if ((++this.currentTick % foodTime) == 0) {
            this.snekBar = this.foodFactory.createSnekBar(
                    this.snekBar,
                    snek,
                    this.view.getContext());
        }

        // Redraw screen
        view.invalidate();
        return true;
    }

    /**
     * returns the direction of the snek
     *
     * @return direction of the snek
     */
    private Direction getDirection() {
        // Update local field based on gyros updates
        updateOrientationAngles();

        // index 2 = negative -> down, positive -> up
        // index 1 = negative -> right, positive -> left

        // First get the biggest motion
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

    /**
     * onSensorChanged triggered if the sensor picked up a new handling/action
     *
     * @param event, retrieves the events caused by handling the sensor
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Based on the sensor type copy the values of event to the corresponding array
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

    /**
     * doesn't serve a purpose, but is required for the sensorEventListener interface
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // no-op
    }

    /**
     * updates the angles in which the snek can move
     *
     * @return void
     */
    private void updateOrientationAngles() {
        SensorManager.getRotationMatrix(
                mRotationMatrix,
                null,
                mAccelerometerReading,
                mMagnetometerReading);

        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
    }

    /**
     * Null values in the snekBar represent eaten food and should be deleted
     *
     * @return new array with foods
     */
    private ISnekFood[] removeNulls(ISnekFood[] objects) {
        // Backbone is list because we don't know how many foods are eaten
        List<ISnekFood> newList = new ArrayList<>();

        // For every non-null obj, add it to newList
        for (ISnekFood obj : objects) {
            if (obj != null) {
                newList.add(obj);
            }
        }

        // Cast the newList to an array of type ISnekFood
        return newList.toArray(new ISnekFood[0]);
    }
}
