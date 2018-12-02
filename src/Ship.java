/**
 * 
 * This class is responsible for storing the information about the ship 
 *
 */
public class Ship {

	private int hitsTaken;
	private int x;
	private int y;
	private int length;
	private boolean isVertical;
	private boolean placed;

	/**
	 * this constructor makes a ship object with parameters to determine the size
	 * @param length - Must be an int <= 5 but doesn't need to be checked since preset
	 * 	ships will be used
	 */
	public Ship(int length) {
		this.length = length;
	}
	 /**
	  * @return the length of the ship
	  */
	public int getLength() {
		return this.length;
	}
	/**
	 * @param isVertical - a boolean object to determine the direction of the ship
	 * @param X - x coordinate of the ship's leading point
	 * @param Y - y coordinate of the ship's leading point
	 */
	public void setLocation(int x, int y, boolean isVertical) throws Exception{
		this.isVertical = isVertical;
		this.x = x;
		this.y = y;
	}

	/**
	 * Checks to see if the ship is completely sunk. 
	 * 
	 * @return true if the ship has been sunk.
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
	
	public void Placed() {
		placed = true;
	}
	public boolean isPlaced() {
		return placed;
	}
	public boolean isLast() {
		return (length == 100);
	}

}
