package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;

import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.InvisibleSnek;

public class InvisibleSnekPart extends SnekPart implements ISnekPart {
    private int x;
    private int y;
    private int previousX = 0;
    private int previousY = 0;
    private SnekPartType type;
    protected static final int drawableHead = R.drawable.invisible_head;
    protected static final int drawableBody = R.drawable.invisible_body;
    protected static final int drawableTail = R.drawable.invisible_tail;

    public InvisibleSnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        super(type, x, y, previousSnekPart);
    }
}
