public class Healer extends LivingThing {
    int healValue;

    public int getHealValue() {
        return healValue;
    }

    public Healer(String name, int health, String color, int healValue) {
        super(name, health, color);
        this.healValue = healValue;
    }

    public int hurt(LivingThing e) {

        int currentHealth = e.getHealth();
        currentHealth += healValue;
        e.setHealth(currentHealth);
        return currentHealth;
    }

}
