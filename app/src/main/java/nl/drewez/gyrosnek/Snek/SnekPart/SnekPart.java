package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;

import nl.drewez.gyrosnek.R;

public class SnekPart implements ISnekPart {

    private int x;
    private int y;
    private int previousX;
    private int previousY;
    private SnekPartType type;

    public SnekPart (SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        this.type = type;
        this.x = x;
        this.y = y;

        this.previousX = previousSnekPart.getX();
        this.previousY = previousSnekPart.getY();
    }

    @Override
    public Drawable getDrawable(Context context) {
        int drawableId = 0;
        int rotateAngle = 0;
        switch (type) {
            case Head:
                drawableId = R.drawable.snek_head;
                if (x > previousX && y == previousY) {
                    rotateAngle = 90;
                } else if (x == previousX && y < previousY) {
                    rotateAngle = -45;
                } else if (x == previousX && y > previousY) {
                    rotateAngle = 45;
                }

                break;
            case Middle:
                drawableId = R.drawable.snek_body;
                break;
            case Tail:
                drawableId = R.drawable.snek_tail;
                break;
        }

        RotateDrawable drawable =  (RotateDrawable) context.getDrawable(drawableId);

        drawable.setToDegrees(rotateAngle);

        return drawable;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
