package nl.drewez.gyrosnek.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import nl.drewez.gyrosnek.IDrawable;
import nl.drewez.gyrosnek.R;
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

    protected void init() {
        cols = getResources().getInteger(R.integer.cols);
        rows = getResources().getInteger(R.integer.rows);

        snekController = new SnekController(this);
        paint = new Paint();

        class mListener extends GestureDetector.SimpleOnGestureListener {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        }

        mDetector = new GestureDetector(getContext(), new mListener());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        calculateBlockSize(getWidth(), getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        calculateBlockSize(w, h);
    }

    private void calculateBlockSize(int width, int height) {
        blockWidth = width / cols;
        blockHeight = height / rows;
    }

    private int getXForBlock(int x) {
        return x * blockWidth;
    }

    private int getYForBlock(int y) {
        return y * blockHeight;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawColor(Color.WHITE);

        for (IDrawable drawable : snekController.getDrawables()) {
            if (drawable == null) {
                continue;
            }

            int x = getXForBlock(drawable.getX());
            int y = getYForBlock(drawable.getY());
            Drawable image = drawable.getDrawable(getContext());
            image.setBounds(
                    x,
                    y,
                    blockWidth + x,
                    blockHeight + y);
            image.draw(canvas);

            Log.d(TAG, "Draw " + drawable.getClass().getSimpleName() + " on " + x + ", " + y + " with blocksize: " + blockWidth + "x" + blockHeight);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mDetector.onTouchEvent(event);
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "onTouchEvent action = action up");
                if (paused) {
                    snekController.start();
                } else {
                    snekController.pause();
                    invalidate();
                }

                paused = !paused;
                result = true;
            }
        }

        Log.d(TAG, "onTouchEvent result:" + result);

        return result;
    }
}