package nl.drewez.gyrosnek.Snek;

import android.content.Context;

//SnekFactory makes a new Snek object
public class SnekFactory implements ISnekFactory {
    public ISnek createSnek(Context context) {
        return new Snek(context);
    }
}
