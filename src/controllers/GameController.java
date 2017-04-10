package controllers;
import models.*;
import java.util.ArrayList;

import static utils.ScannerInput.*;

/**
 * This class
 *
 * @author Kevin Fan
 * @author Niall Grant
 * @author Bernadette Murphy
 * @author Keelan Murphy
 * @version 2017.03.28
 */
public class GameController {
    ArrayList<Player> players;
    HareDeck hareDeck = new HareDeck();
    
    ArrayList<Square> board;
    
    int currentTurn = 0;
    
    public GameController(){
        players = new ArrayList<>();
        startNewGame();
        
        runMenu();
    }
    
    public void startNewGame () {
        createBoard();
    
        System.out.println("Welcome to The Hare and Tortoise");
        
        int j = validNextInt("Enter the number of players you want to play: ");
        for (int i = 0; i < j; i++) {
            String name = retrieveText("Enter player " + (i + 1) + " name");
            addPlayer(name);
        }
        for (int i = 0 ; i < players.size(); i++) {
            board.get(0).setPlayer(players.get(i));
        }
        runMenu();
    }
    
    public void createBoard() {
        board = new ArrayList<>();
        //TODO Confirm we need to input a position and name
        board.add(new StartSquare("Start", 0));
        board.add(new CarrotSquare("Carrots", 1));
        board.add(new CarrotSquare("Carrots", 2));
        board.add(new CarrotSquare("Carrots", 3));
        board.add(new CarrotSquare("Tortoise", 4));
        board.add(new CarrotSquare("Carrots", 5));
        board.add(new CarrotSquare("Carrots", 6));
        board.add(new CarrotSquare("Carrots", 7));
        board.add(new CarrotSquare("Carrots", 8));
        board.add(new CarrotSquare("Carrots", 9));
        board.add(new CarrotSquare("Carrots", 10));
        board.add(new TortoiseSquare("Tortoise", 11));
        board.add(new StartSquare("Finish (Temporary)", 12));
    }
    
    public void addPlayer (String name) {
        players.add(new Player(name));
    }
    
    public void listPlayers () {
        for (int i = 0 ; i < players.size() ; i++) {
            System.out.println(players.get(i).toString());
        }
    }
    
    private void runMenu(){
        while (!isFinished()) {
            takeTurn();
    
            printBoard();
    
            listPlayers();
            nextTurn();
        }
        System.out.println("The game is finished, here is the final standings:");
    }
    
    public void takeTurn() {
        if (!canMoveBackward() && !canStay() && !canMoveForward()) {
            System.out.println("Sorry, there were no available moves, you have been reset to the beginning");
            movePlayer(getCurrentPlayer(),0);
            getCurrentPlayer().setNoOfCarrots(65);
        }
        if (canMoveBackward()) {
            System.out.println("Player can move backwards");
        }
        if (canStay()) {
            System.out.println("Player can stay");
        }
        if (canMoveForward()) {
            System.out.println("Player can move forward");
        }
        
    
        int distance = validNextInt("Enter the number of squares you wish to move " + getCurrentPlayer().getPlayerName());
        int newSquareIndex = getCurrentPlayer().getPosition() + distance;
        if (newSquareIndex < board.size()) {
            while (!board.get(newSquareIndex).isAvailable() || calculateMaxDistance(getCurrentPlayer().getNoOfCarrots()) < distance) {
                distance = validNextInt("Invalid option entered " + getCurrentPlayer().getPlayerName());
                newSquareIndex = getCurrentPlayer().getPosition() + distance;
            }
            movePlayer(getCurrentPlayer(), newSquareIndex);
            getCurrentPlayer().removeCarrots(carrotsRequired(distance));
        } else {
            System.out.println("Square does not exist");
        }
    }
    
    
    public int findPreviousTortoise() {
        int i = getCurrentPlayer().getPosition();
        if (i != 0) {
            while (i > 0 && !board.get(i).getName().equals("Tortoise")) {
                i--;
            }
        }
        return i;
    }
    
    public boolean canMoveBackward() {
        int prevTortoise = findPreviousTortoise();
        if (prevTortoise == 0 || !board.get(prevTortoise).isAvailable()){
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean canStay() {
        return board.get(getCurrentPlayer().getPosition()).canStay();
    }
    
    public boolean canMoveForward() {
        for (int i = getCurrentPlayer().getPosition() ; i < board.size() ; i++) {
            if (board.get(i).isAvailable()) {
                return true;
            }
        }
        return false;
    }
    
    public void movePlayer (Player player, int position) {
        board.get(player.getPosition()).removePlayer(player);
        board.get(position).setPlayer(player);
    }
    
    //TODO Implement Nialls board display instead
    public void printBoard() {
        String str = "";
        String playerName = "";
        for (int i = 0 ; i < board.size(); i++) {
        
            for (int j = 0 ; j < board.get(i).players.size() ; j++) {
                playerName += board.get(i).players.get(j).getPlayerName() + ", ";
            }
           
            str = str + i + ": " + board.get(i).name + " " + playerName + ": "+ "\n";
            playerName = "";
        }
        System.out.print(str);
    }
    
    /**
     * Calculates the carrots required to move a distance
     *
     * The number of carrots required is calculated using a triangular number sequence
     * formula. The alternative was to use a for loop to add every number
     * up to the distance required. This has been commented out.
     *
     * @param distance The distance that the user wishes to move
     * @return The number of carrots required to move the inputted distance
     */
    public int carrotsRequired(int distance) {
		/* Alternative way to find the carrots required
		* int carrots = 0;
		*  for (int index = 1; index <= distance; index++)
		*  {
		*   	carrots = carrots + index;
		*  }
		*  return carrots;
		*/
        return distance*(distance + 1)/2;
    }
    
    /**
     * Calculates what would be the input to the triangular number sequence
     * equation from the number of carrots available. The method then rounds
     * down to find the maximum number of moves.
     *
     * @param carrots The players balance of carrots
     * @return the maximum number of moves a player can make
     */
    private double calculateMaxDistance(int carrots) {
        //Find the natural number (input)
        double distance = (Math.sqrt((8 * carrots) + 1) - 1)/2;
        //Round down to the whole number
        int roundedDown = (int) Math.floor(distance);
        return roundedDown;
    }
    
    
    public void nextTurn() {
        currentTurn++;
        
        if(currentTurn >= players.size()){
            currentTurn = 0;
        }
        
        //Skip if players flag is set to skip turn or if player is finished
        if (getCurrentPlayer().getSkipTurn() || isPlayerFinished(getCurrentPlayer())) {
            getCurrentPlayer().setSkipTurn(false);
            nextTurn();
        }
    }
    
    
    public Player getCurrentPlayer() {
        return players.get(currentTurn);
    }
    
    /**
     * Returns whether the passed player is on the final square
     * @param player the player that is checked to see if they are finished
     * @return a boolean representing if the player is on the final square
     */
    public boolean isPlayerFinished (Player player) {
        if (player.getPosition() != board.size()) {
            return false;
        }
        else {
            return true;
        }
    }
    
    /**
     * Returns whether the game is finished for the while loop, returns false if any user is on any
     * square but the last one.
     * @return a boolean representing whether all players are on the final square
     */
    private boolean isFinished () {
        for ( int i = 0 ; i < players.size() ; i++ ) {
            if (!isPlayerFinished(players.get(i))){
                return false;
            }
        }
        return true;
    }

    //===================
    //Random Methods
    //===================

    /**
     * Method to return the player position in race. Possibly can be used in number square or lettuce square
     * calculation.
     */
    public int getPlayerPositionInRace(Player player) {
        // local variable to store the players current position - not really need
        int currentPlayerPositionOnBoard = player.getPosition();
        // Local temporary store for playerPositionInRace
        int playerPositionInRace = 1;

        // Check each player position in players array, and increment playerPosition by 1 if the player position
        // is less than currentPlayerPositionInRace
        for (int i = 0; i < players.size(); i++) {
            if (currentPlayerPositionOnBoard < players.get(i).getPosition()) {
                playerPositionInRace++;
            }
        }
        return playerPositionInRace;
    }

    /**
     * Method to remove lettuce from player and add carrots according to player position in race
     * Checks does Player have at least 1 lettuce
     */
    public void chewLuttuce(Player player) {
        if (player.getNoOfLettuce() > 0) {
            player.removeLettuce();
            player.addCarrots(getPlayerPositionInRace(player) * 10);
        }
    }

    /**
     * Move back tortoise square calculation
     * No check is done to check for is square a tortoise square or is the tortoise square the nearest one
     * @param player
     * @param move
     */
    public void moveBackToTortoise(Player player, int move) {
        int oldPosition = player.getPosition();
        if(notValidMove(player, move) == false) {
            player.setPosition(player.getPosition() - move);
            player.addCarrots(oldPosition - player.getPosition());
        }
    }

    /**
     * Check is a move for a player is invalid. Checks if player have enough carrots for move and is the position
     * already occupied another player
     * @param player
     * @param move
     * @return
     */
    public boolean notValidMove(Player player, int move) {
        // If statement to check is player has enough carrots for move
        if (carrotsRequired(move) > player.getNoOfCarrots()) {
            return true;
        }

        //Check is square already occupied by another player by checking for each player position in players array
        //Problem: Assuming finish is a tile - players can't move to finish after first player
        for (int i = 0; i < players.size(); i++) {
            // If the new position is a current position of another player
            if ((player.getPosition() + move) == players.get(i).getPosition()) {
                return true;
            }
        }
        return false;
    }
}