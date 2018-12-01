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
	 * Runs the game inside of a loop, taking care of switching players and calling Grid and Ship for various functions.
	 */
	public void runGame() {
		Grid redGrid = new Grid();
		Grid blueGrid = new Grid();
		Grid redView = new Grid();
		Grid blueView = new Grid();
		
		// TODO: Allow users to choose coordinates for ship in the GUI, place the ships using the placeShip method.
		
		// TODO: Set all spaces occupied by a ship to true in the corresponding grid. 
		
		boolean redWins = false;
		boolean blueWins = false;
		
		while(!(redWins && blueWins)) {
			
			// TODO: Allow users to select spot to attack in the GUI.
			int x = 1; // Arbitrary values for now.
			int y = 1;
			
			if(curPlayer == 'R') {
				blueGrid.attack(x, y);
				// TODO: Add something to indicate on redView if there was a hit or not.
			}
			else {
				redGrid.attack(x, y);
				// TODO: Add something to indicate on blueView if there was a hit or not.
			}
			
			// Checks to see if either player has won the game. 
			if(redGrid.allShipsSunk()) {
				blueWins = true;
			}
			else if(blueGrid.allShipsSunk()) {
				redWins = true;
			}
			
			// Switches the current player between red and blue.
			if(curPlayer == 'R') {
				curPlayer = 'B';
			}
			else {
				curPlayer = 'R';
			}
		}
		
		// If the game is won, the loop is exited and some kind of message will be printed here.
		if(redWins) {
			// TODO: Some kind of victory message for red in the GUI.
		}
		else {
			// TODO: Some kind of victory message for blue in the GUI.
		}
	}
	
}
