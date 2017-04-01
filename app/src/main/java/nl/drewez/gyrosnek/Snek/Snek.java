package nl.drewez.gyrosnek.Snek;

import android.content.Context;
import android.graphics.Point;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import nl.drewez.gyrosnek.Direction;
import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.SnekPart.ISnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SnekPart;
import nl.drewez.gyrosnek.Snek.SnekPart.SnekPartType;
import nl.drewez.gyrosnek.SnekFood.CheeseBurger;
import nl.drewez.gyrosnek.SnekFood.HotDog;
import nl.drewez.gyrosnek.SnekFood.ISnekFood;
import nl.drewez.gyrosnek.SnekFood.Pizza;
import nl.drewez.gyrosnek.Views.GameView;

public class Snek implements ISnek {
    private static final String TAG = Snek.class.getSimpleName();

    protected static final double multiplier = 1;
    protected static final Class snekPartType = SnekPart.class;
    protected ISnekPart[] snekParts;
    protected Score score = new Score();
    protected Context viewContext;

    public Snek(Context context) {
        this.score.setScore(0);

        this.viewContext = context;

        int startX = context.getResources().getInteger(R.integer.init_start_x);
        int endX = context.getResources().getInteger(R.integer.init_end_x);
        int row = context.getResources().getInteger(R.integer.init_y);

        int initLength = Math.abs(startX - endX) + 1;
        this.snekParts = new ISnekPart[initLength];
        Log.d(TAG, "Init snek length: " + initLength);

        for (int i = 1; i < initLength; i++) {
            // Todo: fix NullPointerException due to snekParts[0] not existing yet...
            snekParts[i] = createSnekPart(SnekPartType.Middle, startX + i, row, snekParts[i - 1]);
        }

        snekParts[0] = createSnekPart(SnekPartType.Head, startX, row, snekParts[1]);
        snekParts[initLength - 1] = createSnekPart(SnekPartType.Tail, endX, row, snekParts[initLength - 2]);
    }

    public Snek(Context context, ISnekPart[] currentSnekParts, Score currentScore) {
        score = currentScore;
        viewContext = context;
        snekParts = new ISnekPart[currentSnekParts.length];

        snekParts[0] = createSnekPart(
                SnekPartType.Head,
                currentSnekParts[0].getX(),
                currentSnekParts[0].getY(),
                currentSnekParts[1]);

        for (int i = 1; i < currentSnekParts.length - 1; i++) {
            snekParts[i] = createSnekPart(
                    SnekPartType.Middle,
                    currentSnekParts[i].getX(),
                    currentSnekParts[i].getY(),
                    snekParts[i - 1]);
        }

        snekParts[currentSnekParts.length - 1] = createSnekPart(
                SnekPartType.Tail,
                currentSnekParts[currentSnekParts.length - 1].getX(),
                currentSnekParts[currentSnekParts.length - 1].getY(),
                snekParts[snekParts.length - 2]);
    }

    @Override
    public boolean move(Direction direction, ISnekFood[] snekBar, SnekContext snekContext) {
        Point currentPixel = new Point(snekParts[0].getX(), snekParts[0].getY());
        int maxY = viewContext.getResources().getInteger(R.integer.rows);
        int maxX = viewContext.getResources().getInteger(R.integer.cols);

        switch (direction) {
            case Up:
                if ((currentPixel.y--) < 0) {
                    // Can't move outside grid
                    return false;
                }
                break;
            case Right:
                if ((currentPixel.x++) > maxX) {
                    return false;
                }
                break;
            case Down:
                if ((currentPixel.y++) > maxY) {
                    return false;
                }
                break;
            case Left:
                if ((currentPixel.x--) < 0) {
                    return false;
                }
                break;
        }


        // Remove last snekPart
        ISnekPart[] newParts = new ISnekPart[snekParts.length];

        for (int i = 0; i < snekParts.length; i++) {
            // Todo: test this, not sure it works
            newParts[(i + 1) % snekParts.length] = snekParts[i];
        }

        newParts[0] = createSnekPart(
                SnekPartType.Head,
                currentPixel.x,
                currentPixel.y,
                snekParts[snekParts.length - 1]
        );
        newParts[1] = createSnekPart(
                SnekPartType.Middle,
                newParts[1].getX(),
                newParts[1].getY(),
                newParts[0]
        );
        newParts[snekParts.length - 1] = createSnekPart(
                SnekPartType.Tail,
                newParts[snekParts.length - 1].getX(),
                newParts[snekParts.length - 1].getY(),
                newParts[newParts.length - 2]
        );

        snekParts = newParts;

        for (int i = 1; i < snekParts.length; i++) {
            if (snekParts[i].getX() == snekParts[0].getX() &&
                    snekParts[i].getY() == snekParts[0].getY()) {
                return false;
            }
        }

        // Calculate score
        ISnekFood snekFood = getSnekFood(snekBar, currentPixel.x, currentPixel.y);

        if (snekFood != null) {
            processFood(snekFood, direction, snekContext);
        }

        return true;
    }

    @Override
    public Score getScore() {
        return this.score;
    }

    @Override
    public ISnekPart[] getSnekParts() {
        return this.snekParts;
    }

    protected ISnekFood getSnekFood(ISnekFood[] snekBar, int x, int y) {
        for (ISnekFood snekFood : snekBar) {
            if (snekFood.getX() == x && snekFood.getY() == y) {
                return snekFood;
            }
        }

        return null;
    }

    protected ISnekPart createSnekPart(@NonNull SnekPartType type, int x, int y, @NonNull ISnekPart previousSnekPart) {
        if (previousSnekPart == null) {
            throw new NullPointerException("Can't pass null to createSnekPart!");
        }


        try {
            return (ISnekPart) snekPartType
                    .getConstructor(SnekPartType.class, int.class, int.class, ISnekPart.class)
                    .newInstance(type, x, y, previousSnekPart);
        } catch (InstantiationException e) {
            Log.e(TAG, "Could not init ISnekPart of type " + snekPartType.getSimpleName() + " (" + e.toString() + ")");
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Could not init ISnekPart of type " + snekPartType.getSimpleName() + " (" + e.toString() + ")");
        } catch (InvocationTargetException e) {
            Log.e(TAG, "Could not init ISnekPart of type " + snekPartType.getSimpleName() + " (" + e.getCause().toString() + ")");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Could not init ISnekPart of type " + snekPartType.getSimpleName() + " (" + e.toString() + ")");
        }

        return null;
    }

    protected void processFood(ISnekFood snekFood, Direction direction, SnekContext snekContext) {
        ISnekPart[] newSnek = new ISnekPart[snekParts.length + 1];
        System.arraycopy(snekParts, 0, newSnek, 0, snekParts.length);

        int newX, newY;

        if (newSnek[newSnek.length - 2].getX() == newSnek[newSnek.length - 3].getX() &&
                newSnek[newSnek.length - 2].getY() < newSnek[newSnek.length - 3].getY()) {
            newX = newSnek[newSnek.length - 2].getX();
            newY = newSnek[newSnek.length - 2].getY() - 1;
        } else if (newSnek[newSnek.length - 2].getX() == newSnek[newSnek.length - 3].getX() &&
                newSnek[newSnek.length - 2].getY() > newSnek[newSnek.length - 3].getY()) {
            newX = newSnek[newSnek.length - 2].getX();
            newY = newSnek[newSnek.length - 2].getY() + 1;
        } else if (newSnek[newSnek.length - 2].getX() < newSnek[newSnek.length - 3].getX() &&
                newSnek[newSnek.length - 2].getY() == newSnek[newSnek.length - 3].getY()) {
            newX = newSnek[newSnek.length - 2].getX() - 1;
            newY = newSnek[newSnek.length - 2].getY();
        } else {
            newX = newSnek[newSnek.length - 2].getX() + 1;
            newY = newSnek[newSnek.length - 2].getY();
        }

        newSnek[newSnek.length - 1] = createSnekPart(
                SnekPartType.Tail,
                newX,
                newY,
                newSnek[newSnek.length - 2]);

        newSnek[newSnek.length - 2] = createSnekPart(
                SnekPartType.Middle,
                newSnek[newSnek.length - 2].getX(),
                newSnek[newSnek.length - 2].getX(),
                newSnek[newSnek.length - 3]);

        this.score.setScore(this.score.getScore() + (int) ((double) snekFood.getScore() * this.multiplier));

        if (snekContext.getSnek() instanceof Snek) { // Only change state if current state is Snek
            if (snekFood instanceof HotDog) {
                // Todo: give current snekParts and score to next snek
                snekContext.setSnek(new InvisibleSnek(this.viewContext, this.snekParts, this.score));
            }

            if (snekFood instanceof Pizza) {
                snekContext.setSnek(new RainbowSnek(this.viewContext, this.snekParts, this.score));
            }

            if (snekFood instanceof CheeseBurger) {
                snekContext.setSnek(new SpeedSnek(this.viewContext, this.snekParts, this.score)); // Kek, cheezburger makes fast snek
            }
        }
    }
}
