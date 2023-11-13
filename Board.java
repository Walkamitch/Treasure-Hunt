import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Board {
    private Random rand;
    private ArrayList<ArrayList<Space>> board;
    private ArrayList<Treasure> remainingTreasures;
    private int gazooRow;
    private int gazooCol;
    private Explorer gazoo;
    //sub that creates a board and everything in it
    public Board(int numRows, int numCols) {
        board = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            ArrayList<Space> row = new ArrayList<>();
            board.add(row);

            for (int j = 0; j < numCols; j++) {
                row.add(new Space());
            }
        }

        gazoo = new Explorer("Gazoo", 20, ConsoleColors.GREEN);
        gazooRow = 0;
        gazooCol = 0;
        board.get(gazooRow).get(gazooCol).setOccupant(gazoo);
        instantiateTreasures();
        createMonsters();
        placeTreasures();
        createHealer();
    }
    //places treasures randomly for each treasure in treasure remaining
    public void placeTreasures() {
        for (Treasure treasure : remainingTreasures) {
            placeRandomly(treasure);
        }
    }

    //sub that provides movement controls of gazoo
    public boolean move(char m) {
        //checks if game is over and then prints game over if so
        if (isGameOver()) {
            System.out.println("Game Over!");
            return false;
        }
        int newGazooRow = gazooRow;
        int newGazooCol = gazooCol;
        //if r is used as a response it reveals the whole board
        if (m == 'r') {
            printBoard(true);
        } else if (m == 'i') {
            System.out.println("Number of treasures found: " + gazoo.getTreasures().size());
            System.out.println("Total value of treasures: " + gazoo.getTreasureValue());
        }
        //case statement for different keyboard inputs for movement of gazoo
        switch (Character.toLowerCase(m)) {
            case 'w':
                newGazooRow--;
                break;
            case 'a':
                newGazooCol--;
                break;
            case 's':
                newGazooRow++;
                break;
            case 'd':
                newGazooCol++;
                break;
            default:
                return false;
        }
        //returns false if move is not valid
        if(!isValidMove(newGazooRow, newGazooCol)){
            return false;
        }
        LivingThing occupant = board.get(newGazooRow).get(newGazooCol).getOccupant();
        //checks if gazoo touches monster if so gazoo gets hurt
        if (occupant != null) {
            if (occupant instanceof Monster) {
                int newHealth = Monster.hurt((this.gazoo));
                System.out.println("Gazoo Encountered " + occupant.getName() + " and lost " + ((Monster) occupant).getDamage() + " health and now has " + gazoo.getHealth() + " Health.");
                if (newHealth <= 0) {
                    System.out.println("Gazoo has died!");
                    endGame();
                }
            } else if (occupant instanceof Healer) {
                int newHealth = ((Healer) occupant).hurt(gazoo);
                System.out.println("Gazoo healed by " + ((Healer) occupant).getHealValue() + " health points and now has " + gazoo.getHealth() + " Health.");
            }

            board.get(newGazooRow).get(newGazooCol).setOccupant(gazoo);
        }
        //adds treasures to board
        if (board.get(newGazooRow).get(newGazooCol).getCache() != null) {
            Treasure treasure = board.get(newGazooRow).get(newGazooCol).getCache();
            gazoo.addTreasure(treasure);
            remainingTreasures.remove(treasure);
            board.get(newGazooRow).get(newGazooCol).setCache(null);

            if (remainingTreasures.isEmpty()) {
                System.out.println("Congratulations! You have found all treasures!");
                endGame();
            }
        }

        //if the move is valid it moves gazoo
        if (isValidMove(newGazooRow, newGazooCol)) {
            board.get(gazooRow).get(gazooCol).setOccupant(null);
            gazooRow = newGazooRow;
            gazooCol = newGazooCol;
            board.get(gazooRow).get(gazooCol).setOccupant(gazoo);
            return true;
        } else {
            return false;
        }



    }
    //checks if game is over and if so returns true
    public boolean isGameOver() {
        return gazoo.getHealth() <= 0 || remainingTreasures.isEmpty();
    }
    //if game ends this is called
    public void endGame() {
        System.out.println("Game Over!");
        System.out.println("Number of treasures found: " + gazoo.getTreasures().size());
        System.out.println("Total value of treasures: " + gazoo.getTreasureValue());
        System.exit(0);
    }
    //checks if the move is not out of bounds
    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < board.size() && col >= 0 && col < board.get(0).size();
    }
    //prints the board with the content as if true is passed in then it shows everything
    public void printBoard(boolean showContents) {
        for (ArrayList<Space> row : board) {
            for (Space space : row) {
                System.out.print(space.getConsoleStr(showContents) + " ");
            }
            System.out.println();
        }
    }


    //instantiates 5 treasures in the remaining treasure arraylist
    public void instantiateTreasures(){
        remainingTreasures = new ArrayList<>();
        remainingTreasures.add(new Treasure());
        remainingTreasures.add(new Treasure());
        remainingTreasures.add(new Treasure());
        remainingTreasures.add(new Treasure());
        remainingTreasures.add(new Treasure());
    }

    public void createHealer(){
        Healer healer = new Healer("Healer", 1, ConsoleColors.BLUE, 5);
        placeRandomly(healer);
    }


    //creates 5 monsters to be added to the game
    public void createMonsters() {
        Monster razorclaw = new Monster("Razorclaw", 3, ConsoleColors.RED, 9);
        Monster gloomfury = new Monster("Gloomfury", 3, ConsoleColors.RED, 8);
        Monster fangsnarl = new Monster("Fangsnarl", 3, ConsoleColors.RED, 7);
        Monster vilespike = new Monster("Vilespike", 3, ConsoleColors.RED, 6);
        Monster grimscowl = new Monster("Grimscowl", 3, ConsoleColors.RED, 5);

        ArrayList<LivingThing> monsters = new ArrayList<>(List.of(razorclaw, gloomfury, fangsnarl, vilespike, grimscowl));

        for (LivingThing monster : monsters) {
            placeRandomly(monster);
        }
    }
    //places monsters randomly on the board
    public void placeRandomly(LivingThing entity) {

        int row, col;
        do {
            row = (int) (Math.random() * board.size());
            col = (int) (Math.random() * board.get(0).size());
        } while (validSpace(row,col) == false);

        board.get(row).get(col).setOccupant(entity);
    }
    //places treasure randomly on the board
    public void placeRandomly(Treasure treasure) {
        int row, col;
        do {
            row = (int) (Math.random() * board.size());
            col = (int) (Math.random() * board.get(0).size());
        } while (validSpace(row,col) == false);

        board.get(row).get(col).setCache(treasure);
    }


    public static void main(String[] args) {
        Board board = new Board(8, 9);

        board.printBoard(false);
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a move (w/a/s/d to move Gazoo, q to quit): ");
            char move = scanner.next().charAt(0);

            if (move == 'q') {
                break;
            }

            if (board.move(move)) {
                System.out.println("Moved Gazoo " + move);
                board.printBoard(false);
            }
        }

        scanner.close();
    }
    public boolean validSpace(int row, int col){
        Space space = new Space();
        if (board.get(row).get(col).getCache() != null) {
            return false;
        }else if(board.get(row).get(col).getOccupant() != null) {
            return false;
        }else{
            return true;
        }
    }
}