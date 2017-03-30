package nl.drewez.gyrosnek;

import android.content.Context;
import android.os.Handler;

import nl.drewez.gyrosnek.Snek.ISnek;
import nl.drewez.gyrosnek.Snek.ISnekFactory;
import nl.drewez.gyrosnek.Snek.SnekContext;
import nl.drewez.gyrosnek.Snek.SnekFactory;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;
import nl.drewez.gyrosnek.SnekFood.ISnekFoodFactory;
import nl.drewez.gyrosnek.SnekFood.SnekFoodFactory;
import nl.drewez.gyrosnek.Views.GameView;

public class SnekController {
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

    public SnekController(GameView view) {
        this.view = view;

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
        this.tickHandler.postDelayed(this.tick, this.tickTime);
    }

    public void pause() {
        this.tickHandler.removeCallbacks(this.tick);
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
        ISnek snek = this.snekContext.getSnek();

        snek.move(
                this.getDirection(),
                this.snekBar,
                this.snekContext);

        if ((++this.currentTick % this.foodTime) == 0) {
            // Only make food every <this.foodTime> ticks
            this.snekBar = this.foodFactory.createSnekBar(
                    this.snekBar,
                    snek,
                    this.view.getContext());
        }

        // Redraw screen
        this.view.invalidate();
    }

    private Direction getDirection() {
        return null;
    }
}
