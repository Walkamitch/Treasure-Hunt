public class LivingThing {

    private String name;
    private Integer health;
    private String pieceColor;

    public LivingThing() {
        this(new Hero("DefaultHero", 20, ConsoleColors.GREEN));
    }

    public LivingThing(String name, int health, String color) {
        this.name = name;
        setHealth(health);
        setPieceColor(color);

    }
    public LivingThing(Explorer explorer) {
        this.name = explorer.getName();
        this.health = explorer.getHealth();
        this.pieceColor = explorer.getPieceColor();
    }

    public boolean isDead() {
        if(health <= 0) {
            return true;
        }else{
            return false;
        }
    }
    public LivingThing(String name, int health) {
        this.name = name;
        setHealth(health);
        setPieceColor(ConsoleColors.YELLOW);

    }

    public void setName(String name) {
        if (name.trim() == ""){
            name = "Undefined";
        }
        this.name = name.trim();
    }

    public void setHealth(Integer health) {
        if (health < 0) {
            health = 0;

        }
        this.health = health;
    }

    public void setPieceColor(String pieceColor) {
        this.pieceColor = pieceColor;
    }

    public String getName() {
        return name;
    }

    public Integer getHealth() {
        return health;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public String getConsoleStr() {
        return pieceColor + name.charAt(0) + ConsoleColors.RESET;
    }
}