package nl.drewez.gyrosnek.Snek.SnekPart;

import nl.drewez.gyrosnek.R;

public class InvisibleSnekPart extends SnekPart implements ISnekPart {

    public InvisibleSnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        super(type, x, y, previousSnekPart);

        drawableHead = R.drawable.invisible_head;
        drawableBody = R.drawable.invisible_body;
        drawableTail = R.drawable.invisible_tail;
    }
}
