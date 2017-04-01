package nl.drewez.gyrosnek;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface IDrawable {
    Drawable getDrawable(Context context);
    int getX();
    int getY();
}
