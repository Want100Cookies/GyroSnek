package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;

import nl.drewez.gyrosnek.Snek.ISnek;

public interface ISnekFoodFactory {
    /**
     * Create a new SnekBar based on several parameters
     *
     * @param currentSnekFood The current snekBar
     * @param snek            The current snek (so we don't draw food on the snek)
     * @param context         The view context used for drawable and xml value retrieval
     * @return An array with snekFoods
     */
    ISnekFood[] createSnekBar(ISnekFood[] currentSnekFood, ISnek snek, Context context);
}
