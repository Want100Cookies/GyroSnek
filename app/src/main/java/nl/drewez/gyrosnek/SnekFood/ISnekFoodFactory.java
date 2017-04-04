package nl.drewez.gyrosnek.SnekFood;

import android.content.Context;

import nl.drewez.gyrosnek.Snek.ISnek;

//interface for the SnekFoodFactory
public interface ISnekFoodFactory {
    ISnekFood[] createSnekBar(ISnekFood[] currentSnekFood, ISnek snek, Context context);
}
