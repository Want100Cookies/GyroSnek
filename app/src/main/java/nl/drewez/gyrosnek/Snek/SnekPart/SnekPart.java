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
    protected static int drawableHead;
    protected static int drawableBody;
    protected static int drawableTail;

    /**
     * Initialize SnekPart
     * @param type the type of this part (Head, middle or tail)
     * @param x the x-position
     * @param y the y-position
     * @param previousSnekPart the previous snekPart is used to rotate this snekPart accordingly
     */
    public SnekPart(SnekPartType type, int x, int y, ISnekPart previousSnekPart) {
        // Set the drawable id's
        drawableHead = R.drawable.snek_head;
        drawableBody = R.drawable.snek_body;
        drawableTail = R.drawable.snek_tail;

        // Init the fields
        this.type = type;
        this.x = x;
        this.y = y;

        // In case of previous, set the previousX and Y
        if (previousSnekPart != null) {
            previousX = previousSnekPart.getX();
            previousY = previousSnekPart.getY();
        }
    }

    /**
     * Return the drawable associated with this snekPart
     * @param context the context needed for Drawable retrieval
     * @return Drawable
     */
    @Override
    public Drawable getDrawable(Context context) {
        int drawableId = 0;
        int rotateAngle = 0;

        switch (type) {
            case Head:
                drawableId = drawableHead;

                // The head rotates based on the body after this part
                // If the body is further right and on the same height
                if (x > previousX && y == previousY) {
                    rotateAngle = 180; // Rotate heading to east

                } else if (x == previousX && y < previousY) {
                    // If the body is on the same col and lower row
                    rotateAngle = 90; // Rotate heading south

                } else if (x == previousX && y > previousY) {
                    // If the body is on the same col and higher row
                    rotateAngle = -90; // Rotate heading north
                }
                break;
            case Middle:
                drawableId = drawableBody;
                // No rotating needed for the body
                break;
            case Tail:
                drawableId = drawableTail;

                // The tail rotation is the opposite of the head
                if (x < previousX && y == previousY) {
                    rotateAngle = 180;
                } else if (x == previousX && y < previousY) {
                    rotateAngle = -90;
                } else if (x == previousX && y > previousY) {
                    rotateAngle = 90;
                }
                break;
        }

        // A matrix object stores the rotation angle
        // This matrix is used in the createBitmap
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateAngle);

        // Create bitmap from a drawable
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        // Rotate it with the matrix
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // Cast the bitmap to a drawable using the context resources for more accurate casting
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * @return the x-position of this snekPart
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * @return the y-position of this snekPart
     */
    @Override
    public int getY() {
        return y;
    }
}
