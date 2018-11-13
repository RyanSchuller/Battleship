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

	/**
	 * this constructor makes a ship object with parameters to determine the size
	 * @param length - Must be an int <= 5 but doesn't need to be checked since preset
	 * 		ships will be used
	 */
	public Ship(int length) {
		this.length = length;
	}
	/**
	 * @param isVertical - a boolean object to determine the direction of the ship
	 * @param X - Must be an int >= 0 & <= length - 10 for the x coordinate of the ship
	 * 		on the grid if the ship is horizontal otherwise 0 <= x < 10
	 * @param Y - Must be an int >= 0 & <= length - 10 for the y coordinate of the ship
	 * 		on the grid if the ship is vertical otherwise 0 <= x < 10
	 */
	public void setLocation(int x, int y, boolean isVertical) throws Exception{
		this.isVertical = isVertical;
		if(isVertical && x >= 0 && x < 10) {
			this.x = x;
		}
		else if(!isVertical && x >= 0 && x <= (length - 10)) {
			this.x = x;
		}
		else {
			throw new Exception("x value not in range");
		}
		if(!isVertical && y >= 0 && y < 10) {
			this.y = y;
		}
		else if(isVertical && y >= 0 && y <= (length - 10)) {
			this.y = y;
		}
		else {
			throw new Exception("y value not in range");
		}
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
	 * Checks to see if a player's attack has hit a ship or not.
	 */
	public boolean isHit(int attackX, int attackY) {
		if(isVertical && attackY == y) { //If the ship is vertical only one column matters
			for(int i = x; i < (x + length); i++) {
				//Loop through every row to see if the user entered row matches one the 
				//ship is in
				if(attackX == i) {
					hitsTaken++; //If so, hit the ship and return that it has been hit
					return true;
				}
			}
		}
		else if(!isVertical && attackX == x) {
			//If the ship is horizontal only one row matters
			for(int i = y; i < (y + length); i++) {
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

}
