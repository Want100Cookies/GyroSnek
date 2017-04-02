package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;

import nl.drewez.gyrosnek.IDrawable;

public interface ISnekPart extends IDrawable {
    Drawable getDrawable(Context context);

    int getX();

    int getY();
}
