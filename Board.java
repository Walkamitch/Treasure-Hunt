import java.util.*;

public class Board {
    private Random rand;
    private ArrayList<ArrayList<Space>> board;
    private ArrayList<Treasure> remainingTreasures;
    private int gazooRow;
    private int gazooCol;
    private LivingThing gazoo;
    private boolean isHero;

    //sub that creates a board and everything in it
    public Board(int numRows, int numCols) {
        board = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.print("type E for Explorer and H For Hero: ");
        char c = scanner.next().charAt(0);
        switch (Character.toLowerCase(c)) {
            case 'e':
                gazoo = new Explorer("Gazoo", 20, ConsoleColors.GREEN);
                isHero = false;
                break;
            case 'h':
                gazoo = new Hero("Gazoo", 20, ConsoleColors.GREEN);
                isHero = true;
                break;
            default:
                gazoo = new Explorer("Gazoo", 20, ConsoleColors.GREEN);
                isHero = false;
                break;
        }
        for (int i = 0; i < numRows; i++) {
            ArrayList<Space> row = new ArrayList<>();
            board.add(row);

            for (int j = 0; j < numCols; j++) {
                row.add(new Space());
            }
        }



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
    public LivingThing fight(LivingThing lt1, LivingThing lt2) {
        // Check if lt1 and lt2 are not null and both are alive
        while (lt1 != null && lt2 != null && !lt1.isDead() && !lt2.isDead()) {
            int damage = 0;

            if (lt1 instanceof Fighter) {
                damage = ((Fighter) lt1).hurt(lt2);
                System.out.println(lt1.getName() + " applies " + damage + " damage to " + lt2.getName());
                // Pause for half a second here
            }

            if (!lt2.isDead() && lt2 instanceof Fighter) {
                damage = ((Fighter) lt2).hurt(lt1);
                System.out.println(lt2.getName() + " applies " + damage + " damage to " + lt1.getName());
                // Pause for half a second here
            }
        }

        LivingThing winner = lt1.isDead() ? lt2 : lt1;
        LivingThing loser = lt1.isDead() ? lt1 : lt2;
        System.out.println(winner.getName() + " had defeated " + loser.getName());
        if(!lt1.isDead() && lt1 instanceof Fighter){
            Treasure treasure = new Treasure(10, lt2.getName() + "'s Bounty", ConsoleColors.YELLOW);
            ((Explorer)gazoo).addTreasure(treasure);
            System.out.println("Gazoo gained " + treasure.getDescription() + " worth " + treasure.getValue());
        }

        return winner;
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
            ArrayList<Treasure> treasures = ((Explorer)gazoo).getTreasures();
            for (int i = 0; i < treasures.size(); i++) {
                System.out.println(treasures.get(i).getDescription() + ": " + treasures.get(i).getValue());
            }
            System.out.println("Number of treasures found: " + treasures.size());
            System.out.println("Total value of treasures: " + ((Explorer)gazoo).getTreasureValue());
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
                Monster monster = (Monster) occupant; // Cast occupant to Monster
                if(gazoo instanceof Hero){

                    fight(gazoo, monster);
                }else if(gazoo instanceof Explorer) {
                    int newHealth = monster.hurt(gazoo); // Call hurt method on the Monster instance
                    System.out.println("Gazoo Encountered " + occupant.getName() + " and lost " + monster.getDamage() + " health and now has " + gazoo.getHealth() + " Health.");
                    if (newHealth <= 0) {
                        System.out.println("Gazoo has died!");
                        endGame();
                    }
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
            ((Explorer)gazoo).addTreasure(treasure);
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
        System.out.println("Number of treasures found: " + ((Explorer)gazoo).getTreasures().size());
        System.out.println("Total value of treasures: " + ((Explorer)gazoo).getTreasureValue());
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