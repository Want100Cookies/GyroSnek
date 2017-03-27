package nl.drewez.gyrosnek.SnekFood;

import java.util.Random;

public class SnekFoodFactory implements ISnekFoodFactory {
    private static final Random random = new Random();
    private static final int MinNoOfFoods = 0;
    private static final int MaxNoOfFoods = 5;

    @Override
    public ISnekFood[] createSnekBar() {

        int noOfFoods = random.nextInt(MaxNoOfFoods - MinNoOfFoods) + MinNoOfFoods;

        ISnekFood[] snekBar = new ISnekFood[noOfFoods];

        for (int i = 0; i < noOfFoods; i++) {
            int snekType = random.nextInt(3);



            switch (snekType) {
                case 0:
                    snekBar[i] = new CheeseBurger(0, 0);
                    break;
                case 1:
                    snekBar[i] = new HotDog(0, 0);
                    break;
                case 2:
                    snekBar[i] = new Pizza(0, 0);
                    break;
            }
        }

        return snekBar;
    }
}
