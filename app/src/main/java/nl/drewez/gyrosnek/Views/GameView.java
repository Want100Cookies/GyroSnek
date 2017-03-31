package nl.drewez.gyrosnek.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import nl.drewez.gyrosnek.SnekController;

public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();

    private SnekController snekController;
    private Paint paint;
    private GestureDetector mDetector;
    private String text = "Initializing...";
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
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!paused) {
            text = snekController.getDirection().toString();
        } else {
            text = "PAUSED";
        }

        paint.setStyle(Paint.Style.FILL);
        canvas.drawColor(Color.WHITE);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.MAGENTA);
        paint.setTextSize(100);
        canvas.drawText(
                text,
                0,
                100,
                paint);
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