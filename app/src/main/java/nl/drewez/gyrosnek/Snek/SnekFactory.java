package nl.drewez.gyrosnek.Snek;

import android.content.Context;

public class SnekFactory implements ISnekFactory {
    public ISnek createSnek(Context context) {
        return new Snek(context);
    }
}
