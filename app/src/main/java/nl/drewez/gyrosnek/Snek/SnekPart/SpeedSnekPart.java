package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.SpeedSnek;

public class SpeedSnekPart extends SnekPart implements ISnekPart {
    private int x;
    private int y;
    private int previousX = 0;
    private int previousY = 0;
    private SnekPartType type;
    protected static final int drawableHead = R.drawable.speed_head;
    protected static final int drawableBody = R.drawable.speed_body;
    protected static final int drawableTail = R.drawable.speed_tail;


    public SpeedSnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        super(type, x, y, previousSnekPart);
    }
}
