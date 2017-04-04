package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;
import android.graphics.drawable.Drawable;

import nl.drewez.gyrosnek.IDrawable;

public interface ISnekFood extends IDrawable {
    /**
     * Get the drawable for this snekFood
     *
     * @param context The context used for Drawable retrieval
     * @return A snekFood drawable
     */
    Drawable getDrawable(Context context);

    /**
     * @return the score of this snekFood
     */
    int getScore();

    /**
     * @return the x-position of this snekFood
     */
    int getX();

    /**
     * @return the y-position of this snekFood
     */
    int getY();
}
