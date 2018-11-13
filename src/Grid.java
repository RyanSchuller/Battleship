
/**
 * This class represents the multiple grids that the player sees and interacts with while playing the game.
 */
public class Grid {
	private static final int SIDE_LENGTH = 10;
	private boolean[][] spaces;
	private Ship carrier = new Ship(5);
	private Ship battleship = new Ship(4);
	private Ship cruiser = new Ship(3);
	private Ship submarine = new Ship(3);
	private Ship destroyer = new Ship(2);
	
	
	/*
	 * Constructor for Grid objects. Initializes the 2D array spaces to be empty initially. 
	 */
	public Grid() {
		
	}
	
	/**
	 * Takes in a ship object, an x coordinate, a y coordinate, and a boolean to determine if it is vertical or not.
	 * It then places the ship onto the grid at that spot.
	 * 
	 * @param S - must be an initialized Ship object.
	 * @param X - must be an int between 1 and 10, inclusive.
	 * @param Y - must be an int between 1 and 10, inclusive.
	 * @param isVertical - determines if the ship is placed vertically or horizontally at the X,Y coordinate given.
	 */
	public void placeShip(Ship S, int X, int Y, boolean isVertical) {
		
	}
	
	/**
	 * Handles a players attempt to hit the opponent's ships. 
	 * It uses methods from ship like isHit and isSunk.
	 */
	public void attack() {
		
	}
	
	/**
	 * Checks a given spot on the grid and returns whether the spot is empty or not.
	 * 
	 * This method can be used in attack
	 * @param X - must be an int between 1 and 10, inclusive.
	 * @param Y - must be an int between 1 and 10, inclusive.
	 * @return true if the given space is empty, false if it is occupied by a ship.
	 */
	private boolean isEmpty(int X, int Y) {
		return true;
	}
	
}
