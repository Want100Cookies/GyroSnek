package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import nl.drewez.gyrosnek.R;

public class HotDog implements ISnekFood {
    private static final int score = 30;
    private int y;
    private int x;

    /**
     * Initialize this HotDog on the given x and y
     *
     * @param x the x-position of this snekFood
     * @param y the y-position of this snekFood
     */
    public HotDog(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the drawable for this snekFood
     *
     * @param context The context used for Drawable retrieval
     * @return A snekFood drawable
     */
    @Override
    public Drawable getDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.hot_dog);
    }

    /**
     * @return the score of this snekFood
     */
    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * @return the x-position of this snekFood
     */
    @Override
    public int getX() {
        return this.x;
    }

    /**
     * @return the y-position of this snekFood
     */
    @Override
    public int getY() {
        return this.y;
    }
}
