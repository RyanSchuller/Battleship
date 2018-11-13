/**
 * 
 * This class is responsible for storing the information about the ship 
 *
 */
public class Ship {
	
	private int hitsTaken;
	private int X;
	private int Y;
	private int length;
	private boolean isVertical;
	private String name;
	
	/**
	 * this constructor makes a ship object with parameters to determine the size, name, and location of the ship.
	 * @param name - Must be one of the names of the ships from traditional battleship rules.
	 * @param length - Must be an int smaller than 6
	 * @param X - Must be an int less than 10 for the x coordinate of the ship on the grid
	 * @param Y - Must be an int less than 10 for the y coordinate of the ship on the grid
	 * @param isVertical - a boolean object to determine the direction of the ship
	 */
	public Ship(String name, int X, int Y, int length, boolean isVertical) {
		
	}
	
	/**
	 * Checks to see if the ship is completely sunk. 
	 * 
	 * @return true if the ship has been sunk.
	 */
	public boolean isSunk() {
		return false;
	}
	
	/**
	 * Checks to see if a player's attack has hit a ship or not. May return something, not sure yet.
	 */
	public void isHit() {
		
	}
	
}
