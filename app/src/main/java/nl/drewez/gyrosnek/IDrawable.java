package nl.drewez.gyrosnek;

import android.content.Context;
import android.graphics.drawable.Drawable;


public interface IDrawable {
    /**
     * Get the drawable for this context
     *
     * @param context context used for the drawable retreival
     */
    Drawable getDrawable(Context context);

    int getX();

    int getY();
}
