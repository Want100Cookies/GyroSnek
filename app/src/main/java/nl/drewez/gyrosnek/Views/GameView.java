package nl.drewez.gyrosnek.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import nl.drewez.gyrosnek.IDrawable;
import nl.drewez.gyrosnek.R;
import nl.drewez.gyrosnek.Snek.Score;
import nl.drewez.gyrosnek.SnekController;

public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();

    private int cols;
    private int rows;

    private int blockWidth;
    private int blockHeight;

    private SnekController snekController;

    private Paint paint;

    private GestureDetector mDetector;

    private boolean paused = true;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initialize the GameView
     */
    protected void init() {
        // Get some values from xml
        cols = getResources().getInteger(R.integer.cols);
        rows = getResources().getInteger(R.integer.rows);

        // Init the controller and the paint
        snekController = new SnekController(this);
        paint = new Paint();

        // Make a new gesture listener
        class mListener extends GestureDetector.SimpleOnGestureListener {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        }

        // Use this listener to make a new gesture detector
        mDetector = new GestureDetector(getContext(), new mListener());
    }

    /**
     * Calculate the block size based on the width and height
     *
     * @param changed True if changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        calculateBlockSize(getWidth(), getHeight());
    }

    /**
     * Calculate the block size based on the width and height
     *
     * @param w    The new width
     * @param h    The new height
     * @param oldW The old width
     * @param oldH The old height
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        calculateBlockSize(w, h);
    }

    /**
     * Calculate the block size based on the width and height
     *
     * @param width  The current screen width
     * @param height The current screen height
     */
    private void calculateBlockSize(int width, int height) {
        blockWidth = width / cols;
        blockHeight = height / rows;
    }

    /**
     * Get the x-position (in pixels) of the column
     *
     * @param x The column
     * @return The pixel location of this column
     */
    private int getXForBlock(int x) {
        return x * blockWidth;
    }

    /**
     * Get the y-position (in pixels) of the row
     *
     * @param y The row
     * @return The pixel location of this row
     */
    private int getYForBlock(int y) {
        return y * blockHeight;
    }

    /**
     * Draw all drawables and text on the screen
     *
     * @param canvas The canvas to draw on
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Paint the background with the color color.colorPrimary
        paint.setStyle(Paint.Style.FILL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canvas.drawColor(getResources().getColor(R.color.colorPrimary, null));
        } else {
            canvas.drawColor(getResources().getColor(R.color.colorPrimary));
        }

        // Set default alpha to 255 (max)
        int alpha = 255;

        // Print the score on the screen in the top left
        paint.setTextSize(100);
        paint.setColor(Color.argb(100, 240, 240, 240));
        canvas.drawText("Score: " + snekController.getScore().getScore(), 10, 110, paint);

        // If the game is pause draw some more on the screen
        // Like paused and some text
        if (paused) {
            // Draw paused text
            int xPos = (canvas.getWidth() / 2);
            int yPos = (canvas.getHeight() / 2);

            String paused = getResources().getString(R.string.paused);
            String continueSnek = getResources().getString(R.string.continue_snek);
            String stopSnek = getResources().getString(R.string.stop_snek);

            paint.setColor(Color.WHITE);

            paint.setTextSize(200);
            drawTextCentered(paused, xPos, yPos, paint, canvas);

            paint.setTextSize(60);
            drawTextCentered(continueSnek, xPos, yPos + 200, paint, canvas);

            paint.setTextSize(50);
            drawTextCentered(stopSnek, xPos, yPos + 200 + 60, paint, canvas);

            alpha = 60;
        }

        // Get all drawables and draw them if they are not null
        for (IDrawable drawable : snekController.getDrawables()) {
            if (drawable == null) {
                continue;
            }

            int x = getXForBlock(drawable.getX());
            int y = getYForBlock(drawable.getY());
            Drawable image = drawable.getDrawable(getContext());
            image.setAlpha(alpha);
            image.setBounds(
                    x,
                    y,
                    blockWidth + x,
                    blockHeight + y);
            image.draw(canvas);
        }
    }

    /**
     * Method triggered when the user releases the screen
     *
     * @param event The event contains touch information
     * @return Boolean if touch is handled
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mDetector.onTouchEvent(event);
        if (!result) {
            // Only on action up
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (paused) {
                    // On right side screen, restart game
                    if (getXForBlock(cols / 2) > event.getX()) {
                        snekController.start();
                        paused = false;

                    } else {
                        // Left side, stop game
                        snekController.stop();
                    }
                } else {
                    snekController.pause();
                    paused = true;
                    invalidate();
                }

                return true;
            }
        }

        return result;
    }

    /**
     * Draw the text on the center of the screen
     *
     * @param text   The text to be centered
     * @param x      The middle of the text (x-position)
     * @param y      The middle of the text (y-position)
     * @param paint  The paint to use for the text (color etc.)
     * @param canvas The canvas to paint the text on
     */
    private void drawTextCentered(String text, int x, int y, Paint paint, Canvas canvas) {
        int xPos = x - (int) (paint.measureText(text) / 2);
        int yPos = (int) (y - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos, paint);
    }

    /**
     * Start the score activity with the score as extra data
     *
     * @param score The score object for reference
     */
    public void startScoreActivity(Score score) {
        Intent intent = new Intent(getContext(), ScoreActivity.class);
        intent.putExtra("score", score.getScore());
        getContext().startActivity(intent);
    }
}