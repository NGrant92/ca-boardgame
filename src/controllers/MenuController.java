package controllers;

import static utils.ScannerInput.validNextInt;

import utils.ShowRules;

/**
 * This class is the initial menu the user sees. The menu is displayed as below:
 *
 *        _  .----.             //
 *       (_\/______\_,        (_)___
 *        'uu----uu~'          _/--_)o
 *
 *           THE TORTOISE AND HARE
 *
 *  Select an option:
 *  1) Start Game
 *  2) View Rules
 *  3) Load Game
 *  0) Exit
 *  ==>>
 *
 * @author Kevin Fan
 * @author Niall Grant
 * @author Bernadette Murphy
 * @author Keelan Murphy
 * @version 2017.03.21
 */

public class MenuController
{

	public static void main(String[] args)
	{
		new MenuController();
	}

	public MenuController(){
		runMenu();
	}

	/**
	 * mainMenu() - This method displays the menu for the application,
	 * reads the menu option that the user entered and returns it.
	 *
	 * @return     the users menu choice
	 */
	private int mainMenu() {

		System.out.println("");
		System.out.println("       _  .----.            //");
		System.out.println("      (_|/______|_,        (_)___ ");
		System.out.println("       'uu----uu~'          _/--_)o");
		System.out.println("          THE HARE AND TORTOISE");
		System.out.println("");

		System.out.println("Game Instructions");
		System.out.println("---------");
		System.out.println(" 1) Play the Game");
		System.out.println(" 2) Show Rules");
		System.out.println(" 3) Load Game" );
		System.out.println(" 0) Exit ");
		int option = validNextInt("==>>");
		return option;
	}

	//This is the method that controls the loop
	private void runMenu(){
		int option=mainMenu();
		while(option!=0){
			switch(option){
			case 1:
				new GameController(true);
				break;
			case 2:
				System.out.println(ShowRules.viewRules());
				break;
			case 3:
				new GameController(false);
				break;
			default:
				System.out.println("Invalid option entered: " + option);
				break;
			}

			option = mainMenu();
		}
		//the user chose option 0, so exit the program
		System.out.println("Exiting... bye");
		System.exit(0);

	}
}


