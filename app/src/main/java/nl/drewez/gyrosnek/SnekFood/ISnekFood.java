package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface ISnekFood {
    Drawable getDrawable(Context context);

    int getScore();

    int getX();

    int getY();
}
