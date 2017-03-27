package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by ljvan on 27-3-2017.
 */

public class InvisibleSnekPart implements ISnekPart {
    private int x;
    private int y;
    private SnekPartType type;

    public InvisibleSnekPart (SnekPartType type, int x, int y) {

    }

    @Override
    public Drawable getDrawable(Context context) {
        return null;
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