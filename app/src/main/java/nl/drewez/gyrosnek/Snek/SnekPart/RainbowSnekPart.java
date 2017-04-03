package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;

import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.RainbowSnek;

public class RainbowSnekPart extends SnekPart implements ISnekPart {
    private int x;
    private int y;
    private int previousX = 0;
    private int previousY = 0;
    private SnekPartType type;
    protected static final int drawableHead = R.drawable.rainbow_head;
    protected static final int drawableBody = R.drawable.rainbow_body;
    protected static final int drawableTail = R.drawable.rainbow_tail;

    public RainbowSnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        super(type, x, y, previousSnekPart);
    }
}
