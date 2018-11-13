/**
 * This class is responsible for running the game using Grid and Ship.
 */
public class Game {	
	private char curPlayer;
	
	/**
	 * Constructor for Game objects. Will initialize curPlayer and any other future member variables.
	 */
	public Game() {
		curPlayer = 'R'; // For now, the red player can go first.
	}
	
	/**
	 * This method will allow the user to place all their ships using the placeShip method, 
	 * as well as perform other setup operations.
	 */
	public void setupGame() { 
		Grid myGrid = new Grid();
		Grid enemyGrid = new Grid();
		Grid myView = new Grid();
		Grid enemyView = new Grid();
		
	}
	
	/**
	 * Runs the game inside of a loop, taking care of switching players and calling Grid and Ship for various functions.
	 */
	public void runGame() {
		
	}
	
}
