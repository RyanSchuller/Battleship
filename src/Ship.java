/**
 * 
 * This class is responsible for storing the information about the ship 
 * 
 */
public class Ship {

	private int hitsTaken; //The number of hits the ship has sustained
	private int x; //x-coordinate of the ship's leading point
	private int y; //y-coordinate of the ship's leading point
	private int length; //the length (number of squares occupied) of the ship
	private boolean isVertical; //Whether or not the ship is vertical
	private boolean isPlaced; //Whether or not the ship has been placed on the board

	/**
	 * this constructor makes a ship object with parameters to determine the size
	 * @param length - Number of squares that a ship occupies
	 * @author SevertsenJD
	 */
	public Ship(int length) {
		this.length = length;
	}
	 /**
	  * @return the length of the ship
	  * @author DavidDR
	  */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * @param isVertical - a boolean object to determine the direction of the ship
	 * @param X - x coordinate of the ship's leading point
	 * @param Y - y coordinate of the ship's leading point
	 * @author SevertsenJD
	 */
	public void setLocation(int x, int y, boolean isVertical) {
		this.isVertical = isVertical;
		this.x = x;
		this.y = y;
	}

	/**
	 * Checks to see if the ship is completely sunk. 
	 * @return true if the ship has been sunk.
	 * @author SevertsenJD
	 */
	public boolean isSunk() {
		return length == hitsTaken;
	}

	/**
	 * Checks to see if the ship is hit and if so adds to the counter recording the number
	 * of hits
	 * @param attackX the x coordinate of the attack
	 * @param attackY the y coordinate of the attack
	 * @return whether or not the ship has been hit
	 * @author SevertsenJD
	 */
	public boolean isHit(int attackX, int attackY) {
		if(isVertical && attackY == x) { //If the ship is vertical only one column matters
			for(int i = y; i < (y + length); i++) {
				//Loop through every row to see if the user entered row matches one the 
				//ship is in
				if(attackX == i) {
					hitsTaken++; //If so, hit the ship and return that it has been hit
					return true;
				}
			}
		}
		else if(!isVertical && attackX == y) {
			//If the ship is horizontal only one row matters
			for(int i = x; i < (x + length); i++) {
				//Loop through every column to see if the user entered column matches
				//one the ship is in
				if(attackY == i) {
					hitsTaken++; //If so, hit the ship and return that it has been hit
					return true;
				}
			}
		}
		return false; //If this line is reached the ship wasn't hit
	}
	/**
	 * Sets a ship as having been placed so the isPlaced() method can be called to check if
	 * a ship has been placed before moving on to the next ship
	 * @author BoerJR
	 */
	public void placed() {
		isPlaced = true;
	}
	
	/**
	 * Checks if the ship has been placed
	 * @return whether or not the ship has been placed.
	 * @author BoerJR
	 */
	public boolean isPlaced() {
		return isPlaced;
	}
	
	/**
	 * Checks to see if the current ship is the last ship (last ship is the null ship
	 * who's length = 100). Used to prompt the all ships have been placed method
	 * @return if the ship is the last ship
	 * @author BoerJR
	 */
	public boolean isLast() {
		return (length == 100);
	}

}