package models;

import utils.GameHelperMethods;

import java.lang.Math;

import java.util.ArrayList;

/**
 * Created by kevin on 06/04/2017.
 * Edited by Niall on 08/04/2017
 * A class used to store the methods that represent the cards from the hare deck
 */
public class HareSquare extends Square {
    private HareDeck hareDeck;

    /**
     * Constructor for the Hare Square Object
     *
     * @param name     The name of the square
     * @param position The number of the square on the board
     * @param hareDeck The hare deck class containing the hare card information
     */
    public HareSquare(String name, int position, HareDeck hareDeck) {
        super(name, position);
        this.hareDeck = hareDeck;
    }

    /**
     * Overrides the superclass method with rules that is applied to the player on the lettuce square.
     * @param allPlayers ArrayList of players in the game
     * @return Return a String message of the rules applied
     */
    @Override
    public String applyRule(ArrayList<Player> allPlayers) {
        //a string that gets the text from a hare card to be used later
        String title = hareDeck.dealCard().getTitle();
        //an empty string that will hold certain text, depending on card drawn.
        String result;
        //the player currently on the hare sqaure
        Player currentPlayer = players.get(0);

        //else if statement that calleds a certain method, depending on card drawn
        switch(title) {
            case "LOSE HALF YOUR CARROTS!":
                result = halfCarrots(currentPlayer);
                break;
            case "GIVE 10 CARROTS TO EACH PLAYER LYING BEHIND YOU IN THE RACE (IF ANY).":
                result =  tenCarrotsPerPlayer(allPlayers, currentPlayer);
                break;
            case "SHOW US YOUR CARROTS!":
                result =  showCarrots(currentPlayer);
                break;
            case "DRAW 10 CARROTS FOR EACH LETTUCE YOU STILL HOLD.":
                result =  tenCarrotsPerLettuce(currentPlayer);
                break;
            case "FREE RIDE!":
                result =  freeRide(currentPlayer);
                break;
            case "RESTORE YOUR CARROT HOLDING TO EXACTLY 65.":
                result =  resetCarrots(currentPlayer);
                break;
            case "IF THERE ARE MORE PLAYERS BEHIND YOU THAN IN FRONT OF YOU, MISS A TURN. IF NOT, PLAY AGAIN.":
                result =  missOrExtraTurn(allPlayers, currentPlayer);
                break;
            case "SHUFFLE THE HARE CARDS AND RECEIVE FROM EACH PLAYER 1 CARROT FOR DOING SO.":
                result =  shuffleCards(allPlayers, currentPlayer, hareDeck);
                break;
            default:
                result =  "HareSquare error";
        }
        return title + "\n" + result + "\n";
    }

    //=================
    //HALF YOUR CARROTS
    //=================
    /**
     * This method is for halving the players stash of carrots. setHalfCarrots() returns the required int
     * and halfCarrots() inputs it into the player's setPendingBalance()
     * @param currentPlayer Player currently on the hare square
     * @return A String message of the rules applied
     */
    private String halfCarrots(Player currentPlayer){

        //this method sets the pending balance to the amount of carrots

        int carrots = currentPlayer.getNoOfCarrots();
        //a method that divides the player's carrots in half and rounds UP the answer if answer results in X.5
        //eg: 65 / 2 = 32.5, this will make it 33
        int halfCarrots = ((int) Math.ceil(carrots / 2.0));
        //this is setting the currentPlayer's stash of carrots to half it's size
        currentPlayer.setNoOfCarrots(halfCarrots);
        //returning a string to inform the player
        return "You're carrot supply is cut in half! You now have " + halfCarrots + " Carrots!";
    }

    //=========================
    //10 CARROTS TO EACH PLAYER
    //=========================
    /**
     * A method used to give each player behind currentPlayer 10 carrots
     * If the currentPlayer can't afford it then 5 carrots each
     * If they can't afford that then 1 carrot each
     * The players also have to option of discarding the carrots
     * @param allPlayers ArrayList of players in the game
     * @param currentPlayer Player currently on the hare square
     * @return A String message of the rules applied
     */
    private String tenCarrotsPerPlayer(ArrayList<Player> allPlayers, Player currentPlayer){
        //currentPlayer.getNoOfCarrots() is tested multiple times so it's added into a variable
        int currentPlayerCarrots = currentPlayer.getNoOfCarrots();
        //this will hold the amount of carrots required to be given to each player
        int carrotsToGive;
        //this will hold the number of players who have to be given carrots
        int playersBehind = 0;

        //a for each loop to run through the players detect how many people are behind currentPlayer
        //this is to help determine how many carrots currentPlayer will have to give away
        for(Player player : allPlayers){
            if(player.getPosition() < currentPlayer.getPosition()){
                playersBehind ++;
            }
        }

        //testing to see if currentPlayer has enough carrots to give 10 to each player
        if(currentPlayerCarrots >= playersBehind*10){
            carrotsToGive = 10;
        }
        //if they dont have enough for 10 carrots per player then it checks
        //if they have enough for 5 carrots per player
        else if(currentPlayerCarrots >= playersBehind*5){
            carrotsToGive = 5;
        }
        //if both fail then they must give 1 carrot to each player
        else{
            carrotsToGive = 1;
        }

        //a for each loop to add the carrotsToGive to each players pending balance
        for(Player player : allPlayers){
            //Check to make sure the player doesn't give themself carrots. Only one player can be on one square
            //at a time so if currentPlayer position == players(i) position then it skips that player
            if(player.getPosition() < currentPlayer.getPosition()){
                player.addPendingBalance(carrotsToGive);
                currentPlayer.removeCarrots(carrotsToGive);
            }
        }
        
        if( playersBehind > 0){
            return "" + currentPlayer.getPlayerName() + " has gifted " + carrotsToGive
                    + " carrots to " +  playersBehind + " of you. Remember to say Thank You!";
        }
        else{
            return "There is no one behind you in the race, you keep all your carrots";
        }

    }

    //====================
    //SHOW US YOUR CARROTS
    //====================
    /**
     * A simple enough method to return the players number of carrots
     * @param currentPlayer Player currently on the hare square
     * @return A String message of the rules applied
     */
    private String showCarrots(Player currentPlayer){

        return "" + currentPlayer.getPlayerName() + " has " + currentPlayer.getNoOfCarrots() + " Carrots!";
    }

    //======================
    //10 CARROTS PER LETTUCE
    //======================
    /**
     * If this card it pulled the player recieves 10 Carrots per Lettuce. If player has no lettuce they skip a turn.
     * @param currentPlayer Player currently on the hare square
     * @return A String message of the rules applied
     * If this card it pulled the player gets 10 Carrots per Lettuce. If player has no lettuce they skip a turn.
     */
    private String tenCarrotsPerLettuce(Player currentPlayer){

        //player.getNoOfLettuce() is used twice to it's given a variable name
        int lettuceNum = currentPlayer.getNoOfLettuce();

        //Checks if player has at least 1 lettuce
        if(lettuceNum > 0){
            //inputs addCarrots into the player pendingBlance
            currentPlayer.addCarrots(lettuceNum * 10);
            return "You have receieved " + (lettuceNum * 10) + " Carrots!";
        }
        //if the player has 0 lettuce it will raise the flag for setSkipTurn()
        else{
            currentPlayer.setSkipTurn(true);
            return "Oh no! You have no Lettuces! You miss a turn!";
        }
    }

    //=========
    //FREE RIDE
    //=========
    /**
     *A method to return the players previously expended amount of carrots
     *To do this the player's previous position is deducted from their current position
     * That result is put into a formula that calculates the required carrots for moving that distance
     *which will add the amount to the player's pendingBalance
     * @param currentPlayer Player currently on the hare square
     * @return A String message of the rules applied
     */
    private String freeRide(Player currentPlayer){

        //this subtracts player's previous position from their current position and feeds it into a variable name
        int distance = (currentPlayer.getPosition() - currentPlayer.getPreviousPosition());
        int carrotsReturned = GameHelperMethods.carrotsRequired(distance);
        //int distance is used to calculate the amount of carrots the players previous move cost them
        //and sends it into their pending balance
        currentPlayer.addCarrots(carrotsReturned);

        return "You've been given back " + carrotsReturned + " Carrots!";
    }

    //==================
    //BACK TO 65 CARROTS
    //==================
    /**
     * A simple enough method to return the player to 65 carrots
     * @param currentPlayer Player currently on the hare square
     * @return A String message of the rules applied
     */
    private String resetCarrots(Player currentPlayer){

        currentPlayer.setNoOfCarrots(65);

        return "Your carrot supply is reset to 65!";
    }

    //=========================================================
    //MORE PEOPLE BEHIND YOU: SKIP TURN. MORE AHEAD: EXTRA TURN
    //=========================================================
    /**
     * If the player has more people behind than ahead of them then you skip a turn
     * If there's more ahead of you, you gain an extra turn
     * If there's an equal amount behind and ahead of you then you gain an extra turn
     * @param currentPlayer Player currently on the hare square
     * @param allPlayers ArrayList of players in the game
     * @return  A String message of the rules applied
     */
    private String missOrExtraTurn(ArrayList<Player> allPlayers, Player currentPlayer){
        //Variable that will count how many people are behind currentPlayer
        int playersBehind = 0;
        //Variable that will count how many people are ahead of currentPlayer
        int playersAhead = 0;

        //For each loop that will run through the players array and
        //compare currentPlayer's position against the other players
        for(Player player : allPlayers){
            //if a player's position is less than currentPlayer's then it will increment playersBehind variable
            if(player.getPosition() < currentPlayer.getPosition()){
                playersBehind++;
            }
            //Same as right above only that if player is ahead of currentPlayer then playersAhead increments
            else if(player.getPosition() > currentPlayer.getPosition()){
                playersAhead++;
            }
        }
        //If more players are behind then they will skip a turn
        if(playersBehind > playersAhead){
            currentPlayer.setSkipTurn(true);
            return "Uh oh! " + currentPlayer.getPlayerName() + " you're too far ahead! You skip a turn.";
        }
        //if more are ahead or equal amounts ahead and behind then they get an extra turn
        else{
            for(Player player : allPlayers){
                if(player.getPosition() != currentPlayer.getPosition()){
                    player.setSkipTurn(true);
                }
            }
            return "Congratulations " + currentPlayer + " you get a free turn!";
        }
    }

    //=================
    //SHUFFLE CARD DECK
    //=================
    /**
     * The player must shuffle the card deck and recieve 1 carrot from each player
     * @param allPlayers ArrayList of players in the game
     * @param currentPlayer Player currently on the hare square
     * @param hareDeck Hare Deck is passed in to be shuffled
     * @return A String message of the rules applied
     */
    private String shuffleCards(ArrayList<Player> allPlayers, Player currentPlayer, HareDeck hareDeck){

        //hareDeck is passed in and shuffled
        hareDeck.shuffle();

        //players Array is passed in and used to determine how many carrots they are given. The -1  is there
        //because the currentPlayer is also in the players array and you only receive carrots from other players
        currentPlayer.addCarrots(allPlayers.size() - 1);

        //For each loop to run through the list of players so 1 carrot is removed from their supply
        for(Player player : allPlayers){
            //the player's position is tested against the current player position to make sure a carrot
            // is not removed from the currentPlayer and if the person has 0 carrots then they are ignored
            if(player.getPosition() != currentPlayer.getPosition() && player.getNoOfCarrots() > 0) {
                player.removeCarrots(1);
            }
        }
        return "The deck has been shuffled and you have been given 1 carrot from everyone.";
    }
}
