package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;

public interface ISnekPart {
    Drawable getDrawable(Context context);
    int getX();
    int getY();
}
