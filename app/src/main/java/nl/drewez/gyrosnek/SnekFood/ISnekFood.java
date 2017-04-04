package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;
import android.graphics.drawable.Drawable;

import nl.drewez.gyrosnek.IDrawable;

//In this class the food object is defined, all foods inherit from ISnekFood
public interface ISnekFood extends IDrawable {
    Drawable getDrawable(Context context);

    int getScore();

    int getX();

    int getY();
}
