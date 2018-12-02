/**
 * This class is responsible for running the game using Grid and Ship.
 */
public class Game {	
	private char curPlayer;
	
	/**
	 * Constructor for Game objects. Will initialize curPlayer and any other future member variables.
	 */
	public Game() {
		curPlayer = 'H'; // For now, the red player can go first.
	}
	
	/**
	 * Runs the game inside of a loop, taking care of switching players and calling Grid and Ship for various functions.
	 */
	public void runGame() {
		Grid playerGrid = new Grid();
		Grid aiGrid = new Grid();
		Grid playerView = new Grid();
		
		// TODO: Allow users to choose coordinates for ship in the GUI, place the ships using the placeShip method.
		
		// TODO: Set all spaces occupied by a ship to true in the corresponding grid. 
		
		boolean playerWins = false;
		boolean aiWins = false;
		
		while(!(playerWins && aiWins)) {
			
			// TODO: Allow users to select spot to attack in the GUI.
			int x = 1; // Arbitrary values for now.
			int y = 1;
			
			if(curPlayer == 'H') {
				aiGrid.attack(x, y);
				// TODO: Add something to indicate on redView if there was a hit or not.
			}
			else {
				playerGrid.attack(x, y);
				// TODO: Add something to indicate on blueView if there was a hit or not.
			}
			
			// Switches the current player between red and blue.
			if(curPlayer == 'H') {
				curPlayer = 'A';
			}
			else {
				curPlayer = 'H';
			}
		}
		
		// If the game is won, the loop is exited and some kind of message will be printed here.
		if(playerWins) {
			// TODO: Some kind of victory message for red in the GUI.
		}
		else {
			// TODO: Some kind of victory message for blue in the GUI.
		}
	}
	
}
