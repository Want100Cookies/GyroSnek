package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.SpeedSnek;

public class SpeedSnekPart extends SnekPart implements ISnekPart {

    public SpeedSnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        super(type, x, y, previousSnekPart);

        drawableHead = R.drawable.speed_head;
        drawableBody = R.drawable.speed_body;
        drawableTail = R.drawable.speed_tail;
    }
}
