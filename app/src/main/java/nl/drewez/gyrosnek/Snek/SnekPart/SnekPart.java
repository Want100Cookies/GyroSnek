package nl.drewez.gyrosnek.Snek.SnekPart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import nl.drewez.gyrosnek.R;

public class SnekPart implements ISnekPart {

    private int x;
    private int y;
    private int previousX = 0;
    private int previousY = 0;
    private SnekPartType type;

    public SnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        this.type = type;
        this.x = x;
        this.y = y;

        if (previousSnekPart != null) {
            this.previousX = previousSnekPart.getX();
            this.previousY = previousSnekPart.getY();
        }
    }

    @Override
    public Drawable getDrawable(Context context) {
        int drawableId = 0;
        int rotateAngle = 0;
        switch (type) {
            case Head:
                drawableId = R.drawable.snek_head;

                if (x > previousX && y == previousY) {
                    rotateAngle = 180;
                } else if (x == previousX && y < previousY) {
                    rotateAngle = 90;
                } else if (x == previousX && y > previousY) {
                    rotateAngle = -90;
                }
                break;
            case Middle:
                drawableId = R.drawable.snek_body;
                break;
            case Tail:
                drawableId = R.drawable.snek_tail;

                if (x < previousX && y == previousY) {
                    rotateAngle = 180;
                } else if (x == previousX && y < previousY) {
                    rotateAngle = -90;
                } else if (x == previousX && y > previousY) {
                    rotateAngle = 90;
                }
                break;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(rotateAngle);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return new BitmapDrawable(context.getResources(), bitmap);
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
