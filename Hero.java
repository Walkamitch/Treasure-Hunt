public class Hero extends Explorer implements Fighter {
        private Dice attackDice;

        // Constructor with Explorer's parameters and default attackDice
        public Hero(String name, int health, String color) {
            super(name, health, color);
            attackDice = new Dice(1, 6); // Default attack dice from 1 to 6
        }

        // Constructor with Explorer's parameters and a specified attackDice
        public Hero(String name, int health, String color, Dice customAttackDice) {
            super(name, health, color);
            attackDice = customAttackDice; // Set attackDice to the provided dice object
        }

        @Override
        public int hurt(LivingThing opponent) {
            int damageApplied = attackDice.getRollValue(); // Get roll value from attackDice
            int currentHealth = opponent.getHealth();
            currentHealth -= damageApplied;
            opponent.setHealth(currentHealth);
            return damageApplied;
        }

        // Other methods or customization specific to Hero can be added here
    }

