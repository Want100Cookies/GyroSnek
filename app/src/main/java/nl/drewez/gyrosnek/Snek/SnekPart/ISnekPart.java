package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;

import nl.drewez.gyrosnek.IDrawable;

public interface ISnekPart extends IDrawable {
    /**
     * Get the drawable to draw on the canvas
     *
     * @param context the context needed for Drawable retrieval
     * @return Drawable
     */
    Drawable getDrawable(Context context);

    /**
     * @return the x-position of this SnekPart
     */
    int getX();

    /**
     * @return the y-position of this SnekPart
     */
    int getY();
}
