package nl.drewez.gyrosnek.Snek;

import android.content.Context;
import android.graphics.Point;
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

public class Snek implements ISnek {
    private static final String TAG = Snek.class.getSimpleName();

    protected static double multiplier = 1;
    protected static Class snekPartType = SnekPart.class;
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

        for (int i = 1; i < initLength; i++) {
            snekParts[i] = createSnekPart(SnekPartType.Middle, startX + i, row, snekParts[i - 1]);
        }

        snekParts[0] = createSnekPart(SnekPartType.Head, startX, row, snekParts[1]);
        snekParts[1] = createSnekPart(SnekPartType.Middle, startX + 1, row, snekParts[0]);
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
        Point previousPixel = new Point(snekParts[1].getX(), snekParts[1].getY());

        // Check if we can move (returns null if not)
        currentPixel = canSnekMove(currentPixel, direction);

        if (currentPixel == null) {
            return false;
        }

        if (currentPixel.equals(previousPixel)) {
            // If try to move if opposite direction, don't...
            return move(direction.getOpposite(), snekBar, snekContext);
        }

        moveSnekForward(currentPixel);

        // Can't eat yourself yo
        for (int i = 1; i < snekParts.length; i++) {
            if (snekParts[i].getX() == snekParts[0].getX() &&
                    snekParts[i].getY() == snekParts[0].getY()) {
                return false;
            }
        }

        // Process food
        ISnekFood snekFood = getSnekFood(snekBar, currentPixel.x, currentPixel.y);

        if (snekFood != null) {
            processFood(snekFood, snekContext);
        }

        return true;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public ISnekPart[] getSnekParts() {
        return snekParts;
    }

    private Point canSnekMove(Point currentPixel, Direction direction) {
        int maxY = viewContext.getResources().getInteger(R.integer.rows);
        int maxX = viewContext.getResources().getInteger(R.integer.cols);

        switch (direction) {
            case Up:
                if ((currentPixel.y--) < 0) {
                    // Can't move outside grid
                    return null;
                }
                break;
            case Right:
                if ((currentPixel.x++) > maxX) {
                    return null;
                }
                break;
            case Down:
                if ((currentPixel.y++) > maxY) {
                    return null;
                }
                break;
            case Left:
                if ((currentPixel.x--) < 0) {
                    return null;
                }
                break;
        }

        return currentPixel;
    }

    private void moveSnekForward(Point currentPixel) {
        // Remove last snekPart
        ISnekPart[] newParts = new ISnekPart[snekParts.length];

        for (int i = 0; i < snekParts.length; i++) {
            newParts[(i + 1) % snekParts.length] = snekParts[i];
        }

        newParts[0] = createSnekPart(
                SnekPartType.Head,
                currentPixel.x,
                currentPixel.y,
                newParts[1]
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
    }

    private ISnekFood getSnekFood(ISnekFood[] snekBar, int x, int y) {
        for (int i = 0; i < snekBar.length; i++) {
            if (snekBar[i].getX() == x && snekBar[i].getY() == y) {
                ISnekFood found = snekBar[i];
                snekBar[i] = null;

                return found;
            }
        }

        return null;
    }

    private ISnekPart createSnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        try {
            return (ISnekPart) snekPartType
                    .getConstructor(SnekPartType.class, int.class, int.class, ISnekPart.class)
                    .newInstance(type, x, y, previousSnekPart);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            Log.e(TAG, "Could not init ISnekPart of type " + snekPartType.getSimpleName() + " (" + e.toString() + ")");
        } catch (InvocationTargetException e) {
            Log.e(TAG, "Could not init ISnekPart of type " + snekPartType.getSimpleName() + " (" + e.getCause().toString() + ")");
        }

        return null;
    }

    private void processFood(ISnekFood snekFood, SnekContext snekContext) {
        increaseSnek();

        increaseScore(snekFood);

        if (snekContext.getSnek() instanceof Snek) { // Only change state if current state is Snek
            if (snekFood instanceof HotDog) {
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

    private void increaseSnek() {
        ISnekPart[] newSnek = new ISnekPart[snekParts.length + 1];
        System.arraycopy(snekParts, 0, newSnek, 0, snekParts.length);

        int newX = newSnek[newSnek.length - 2].getX();
        int newY = newSnek[newSnek.length - 2].getY();

        if (newSnek[newSnek.length - 2].getX() == newSnek[newSnek.length - 3].getX() &&
                newSnek[newSnek.length - 2].getY() < newSnek[newSnek.length - 3].getY()) {
            newY--;

        } else if (newSnek[newSnek.length - 2].getX() == newSnek[newSnek.length - 3].getX() &&
                newSnek[newSnek.length - 2].getY() > newSnek[newSnek.length - 3].getY()) {
            newY++;

        } else if (newSnek[newSnek.length - 2].getX() < newSnek[newSnek.length - 3].getX() &&
                newSnek[newSnek.length - 2].getY() == newSnek[newSnek.length - 3].getY()) {
            newX--;

        } else if (newSnek[newSnek.length - 2].getX() > newSnek[newSnek.length - 3].getX() &&
                newSnek[newSnek.length - 2].getY() == newSnek[newSnek.length - 3].getY()) {
            newX++;
        }

        newSnek[newSnek.length - 1] = createSnekPart(
                SnekPartType.Tail,
                newX,
                newY,
                newSnek[newSnek.length - 2]);

        newSnek[newSnek.length - 2] = createSnekPart(
                SnekPartType.Middle,
                newSnek[newSnek.length - 2].getX(),
                newSnek[newSnek.length - 2].getY(),
                newSnek[newSnek.length - 3]);

        snekParts = newSnek;
    }

    private void increaseScore(ISnekFood snekFood) {
        score.setScore(this.score.getScore() + (int) ((double) snekFood.getScore() * multiplier));
    }
}
