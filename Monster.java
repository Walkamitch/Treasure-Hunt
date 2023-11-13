import java.util.ArrayList;

public class Monster extends LivingThing {

    private static int damage;

    public Monster(String name, int health, String color, int damage) {
        super(name, health, color);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public static int hurt(LivingThing e) {

        int currentHealth = e.getHealth();
        currentHealth -= damage;
        e.setHealth(currentHealth);

        return currentHealth;
    }

}
