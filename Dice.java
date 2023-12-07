import java.util.ArrayList;
import java.util.Random;

public class Dice {
    private ArrayList<Integer> values;
    private static Random roller = new Random();

    public Dice(int min, int max) {
        values = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            values.add(i);
        }
    }

    public Dice(ArrayList<Integer> values) {
        this.values = values;
    }

    public int getRollValue() {
        if (values.isEmpty()) {
            return 0;
        }
        int randomIndex = roller.nextInt(values.size());
        return values.get(randomIndex);
    }
}