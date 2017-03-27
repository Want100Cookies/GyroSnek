package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import nl.drewez.gyrosnek.R;

public class HotDog implements ISnekFood {
    private int score = 1;
    private int y;
    private int x;

    public HotDog(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Drawable getDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.hog_dog);
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }
}
