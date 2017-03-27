package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import nl.drewez.gyrosnek.R;

public class CheeseBurger implements ISnekFood {
    private int score = 1;
    private int y;
    private int x;

    public CheeseBurger(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Drawable getDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.CheeseBurger);
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }
}