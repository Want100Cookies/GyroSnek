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

    protected static int multiplier;
    protected static Class snekPartType;
    protected ISnekPart[] snekParts;
    protected Score score = new Score();
    protected Context viewContext;

    /**
     * Initialize this snek
     *
     * @param context The GameView context used for xml var en drawable retrieval
     */
    public Snek(Context context) {
        // Init the score with zero
        this.score.setScore(0);

        this.viewContext = context;

        // Set default multiplier en snekPartType
        multiplier = 1;
        snekPartType = SnekPart.class;

        // Retrieve vars from xml
        int startX = context.getResources().getInteger(R.integer.init_start_x);
        int endX = context.getResources().getInteger(R.integer.init_end_x);
        int row = context.getResources().getInteger(R.integer.init_y);

        // Calculate and initiate snekParts array
        int initLength = Math.abs(startX - endX) + 1;
        this.snekParts = new ISnekPart[initLength];

        for (int i = 1; i < initLength; i++) {
            snekParts[i] = createSnekPart(SnekPartType.Middle, startX + i, row, snekParts[i - 1]);
        }

        // Define Head and tail and first middel
        // First middel is reïnitiated because the reference to the previous is null
        snekParts[0] = createSnekPart(SnekPartType.Head, startX, row, snekParts[1]);
        snekParts[1] = createSnekPart(SnekPartType.Middle, startX + 1, row, snekParts[0]);
        snekParts[initLength - 1] = createSnekPart(SnekPartType.Tail, endX, row, snekParts[initLength - 2]);
    }

    /**
     * Initiate the snek out of a previous snek
     *
     * @param context          The GameView context used for xml var en drawable retrieval
     * @param currentSnekParts The old snekParts to be used for the new snekParts
     * @param currentScore     The current score
     */
    public Snek(Context context, ISnekPart[] currentSnekParts, Score currentScore) {
        // Copy the old vars to this snek
        score = currentScore;
        viewContext = context;
        snekParts = new ISnekPart[currentSnekParts.length];

        // Set the multiplier and snekPartType back to the defaults
        multiplier = 1;
        snekPartType = SnekPart.class;


        // Create the snekParts out of the previous ones
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

    /**
     * Move the snek in the given direction (if possible) and do all actions needed
     *
     * @param direction   The direction the snek has to move
     * @param snekBar     The food the snek can eat if he encounters it
     * @param snekContext The context of the current snek (used for state-switching)
     * @return boolean False if the snek can not move due to constraints
     */
    @Override
    public boolean move(Direction direction, ISnekFood[] snekBar, SnekContext snekContext) {
        // Get the current and previous pixel for reference of the new snekPart
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
            // Check if x and y are not the same as any other snekPart
            if (snekParts[i].getX() == snekParts[0].getX() &&
                    snekParts[i].getY() == snekParts[0].getY()) {
                return false;
            }
        }

        // Find snekFood at current pixel
        ISnekFood snekFood = getSnekFood(snekBar, currentPixel.x, currentPixel.y);

        if (snekFood != null) {
            // If there was food, process eat
            processFood(snekFood, snekContext);
        }

        return true;
    }

    /**
     * @return Score get the score of this snek
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * @return ISnekPart[] get all the snekParts of this snek
     */
    @Override
    public ISnekPart[] getSnekParts() {
        return snekParts;
    }

    /**
     * Check if the snek can move in the given direction and set current pixel in this direction
     *
     * @param currentPixel This is the pixel of the current head.
     * @param direction    The direction the currentPixel has to be moved in
     * @return the pixel moved in the given direction
     */
    private Point canSnekMove(Point currentPixel, Direction direction) {
        // Get the max x and y from XML
        int maxY = viewContext.getResources().getInteger(R.integer.rows);
        int maxX = viewContext.getResources().getInteger(R.integer.cols);

        // Check and change the currentPixel based on the direction we're headed
        switch (direction) {
            case Up:
                if ((--currentPixel.y) < 0) {
                    // Can't move outside grid
                    return null;
                }
                break;
            case Right:
                if ((++currentPixel.x) >= maxX) {
                    return null;
                }
                break;
            case Down:
                if ((++currentPixel.y) >= maxY) {
                    return null;
                }
                break;
            case Left:
                if ((--currentPixel.x) < 0) {
                    return null;
                }
                break;
        }

        // Because the currentPixel is changed in the switch statement
        // we return it here so it can be used later in the code

        return currentPixel;
    }

    /**
     * Move the snek to the currentPixel
     *
     * @param currentPixel the position of the head after the method has run
     */
    private void moveSnekForward(Point currentPixel) {
        // Remove last snekPart
        ISnekPart[] newParts = new ISnekPart[snekParts.length];

        // Rotate the array to the right
        for (int i = 0; i < snekParts.length; i++) {
            newParts[(i + 1) % snekParts.length] = snekParts[i];
        }

        // Set the snekPartType to the good ones (rotate messed with that)
        // And set the reference to the previous one again (again, rotate is the cause)
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

        // Set the snekParts to the new ones
        snekParts = newParts;
    }

    /**
     * Calculate if there is food at x and y
     *
     * @param snekBar All the food on the grid
     * @param x       The x to be searched
     * @param y       The y to be searched
     * @return ISnekFood The food found and x and y
     */
    private ISnekFood getSnekFood(ISnekFood[] snekBar, int x, int y) {
        // For every food in the snekBar
        for (int i = 0; i < snekBar.length; i++) {
            // If x and y allign
            if (snekBar[i].getX() == x && snekBar[i].getY() == y) {
                // Tmp store the food
                ISnekFood found = snekBar[i];
                // Set it to null so it will be deleted later in the controller
                snekBar[i] = null;

                return found;
            }
        }

        return null;
    }

    /**
     * Create a new SnekPart based on the snekPartType field of Snek.
     * Because of this method the other snek types can inherit of the Snek class and just
     * set snekPartType to the correct one
     *
     * @param type             The type (head, middle or tail)
     * @param x                The x position of the new snekPart
     * @param y                The y position of the new snekPart
     * @param previousSnekPart The previous snekpart used for rotation reference (see SnekPart)
     * @return ISnekPart The new SnekPart
     */
    private ISnekPart createSnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        try {
            // Find the constructor,
            // Initiate the constructor with the given parameters
            // And cast it to a ISnekPart (was of type snekPartType)
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

    /**
     * Processes the food, increases snek and score and switches states as needed
     *
     * @param snekFood    The food to be processed
     * @param snekContext The context of the Snek (used for state-switching)
     */
    private void processFood(ISnekFood snekFood, SnekContext snekContext) {
        // Increase snek length
        increaseSnek();

        // Increase score (based on the food)
        increaseScore(snekFood);

        // Hotdog causes InvisibleSnek
        // Pizza causes RainbowSnek
        // CheeseBurger causes SpeedSnek

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

    /**
     * Increase the snek size by one
     */
    private void increaseSnek() {
        // Init new ISnekPart array with +1 length
        ISnekPart[] newSnek = new ISnekPart[snekParts.length + 1];
        // Copy the contents of the original
        System.arraycopy(snekParts, 0, newSnek, 0, snekParts.length);

        // Get the x and y of the old tail
        int newX = newSnek[newSnek.length - 2].getX();
        int newY = newSnek[newSnek.length - 2].getY();

        // Calculate the newX and newY based on old tail and middle part before old tail
        // So the newY or X is increased or decreased based on row or col difference
        // between these two
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

        // Create a new tail on newX and Y
        newSnek[newSnek.length - 1] = createSnekPart(
                SnekPartType.Tail,
                newX,
                newY,
                newSnek[newSnek.length - 2]);

        // Set old tail snekPartType to middel
        newSnek[newSnek.length - 2] = createSnekPart(
                SnekPartType.Middle,
                newSnek[newSnek.length - 2].getX(),
                newSnek[newSnek.length - 2].getY(),
                newSnek[newSnek.length - 3]);

        // set the current parts to the new ones
        snekParts = newSnek;
    }

    /**
     * Increase the current score based on the score of the food and the multiplier of this snek
     *
     * @param snekFood The snekFood used for score calculation
     */
    private void increaseScore(ISnekFood snekFood) {
        score.setScore(this.score.getScore() + (int) ((double) snekFood.getScore() * multiplier));
    }
}
