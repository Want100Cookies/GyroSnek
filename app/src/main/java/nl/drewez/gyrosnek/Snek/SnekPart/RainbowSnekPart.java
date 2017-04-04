package nl.drewez.gyrosnek.Snek.SnekPart;

import nl.drewez.gyrosnek.R;

public class RainbowSnekPart extends SnekPart implements ISnekPart {

    public RainbowSnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        super(type, x, y, previousSnekPart);

        drawableHead = R.drawable.rainbow_head;
        drawableBody = R.drawable.rainbow_body;
        drawableTail = R.drawable.rainbow_tail;
    }
}
