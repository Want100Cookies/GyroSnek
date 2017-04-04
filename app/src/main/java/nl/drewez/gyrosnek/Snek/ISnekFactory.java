package nl.drewez.gyrosnek.Snek;

import android.content.Context;

public interface ISnekFactory {
    /**
     * Create a new snek with the given view Context
     *
     * @param context The context associated with the gameView
     * @return ISnek the new snek
     */
    ISnek createSnek(Context context);
}
