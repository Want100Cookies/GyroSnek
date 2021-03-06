package nl.drewez.gyrosnek.Snek;

import android.content.Context;

public class SnekFactory implements ISnekFactory {
    /**
     * Create a new snek
     *
     * @param context The context associated with the gameView
     * @return a fresh new snek
     */
    public ISnek createSnek(Context context) {
        return new Snek(context);
    }
}
