public class Treasure {

    private Integer value;
    private String description;
    private String pieceColor;


    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPieceColor(String pieceColor) {
        this.pieceColor = pieceColor;
    }

    public Treasure(Integer value, String description, String color) {
        this.value = value;
        setDescription(description);
        setPieceColor(color);
    }

    public Treasure() {
        this.value = 5;
        setPieceColor(ConsoleColors.YELLOW);
        setDescription("T");
    }

    public String getConsoleStr() {
        return pieceColor + description.trim() + ConsoleColors.RESET;
    }

}