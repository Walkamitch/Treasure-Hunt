import java.util.ArrayList;

public class Monster extends LivingThing implements Fighter {

    private static int damage;

    public Monster(String name, int health, String color, int damage) {
        super(name, health, color);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
    @Override
    public int hurt(LivingThing opponent) {
        int currentHealth = opponent.getHealth();
        int damageApplied = getDamage(); // Assuming getDamage() retrieves the damage value for this monster
        currentHealth -= damageApplied;
        opponent.setHealth(currentHealth);
        return damageApplied;
    }



}
