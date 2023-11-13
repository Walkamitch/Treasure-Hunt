import java.util.ArrayList;
public class Explorer extends LivingThing {

    public ArrayList<Treasure> treasures;

    public Explorer(String name, int health, String color) {
        super(name, health, color);
        treasures = new ArrayList<>();
    }

    public ArrayList<Treasure> getTreasures() {
        return treasures;
    }

    public void addTreasure(Treasure loot){
        treasures.add(loot);

    }
    public int getTreasureValue(){
        int sum = 0;
        for (Treasure T: treasures){
            sum += T.getValue();
        }
        return sum;
    }

}
