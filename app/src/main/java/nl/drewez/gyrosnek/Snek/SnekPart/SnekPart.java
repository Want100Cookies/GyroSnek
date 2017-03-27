package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class SnekPart implements ISnekPart {

    private int x;
    private int y;
    private SnekPartType type;

    public SnekPart (SnekPartType type, int x, int y) {

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
