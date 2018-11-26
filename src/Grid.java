

/**
 * This class represents the multiple grids that the player sees and interacts with while playing the game.
 */

public class Grid {
	private static final int SIDE_LENGTH = 10;
	private boolean[][] spaces;
	private Ship carrier;
	private Ship battleship;
	private Ship cruiser;
	private Ship submarine;
	private Ship destroyer;

	
	/*
	 * Constructor for Grid objects. Initializes the 2D array spaces to be empty initially. 
	 */
	public Grid() {
		destroyer = new Ship(2);
		submarine = new Ship(3);
		cruiser = new Ship(3);
		battleship = new Ship(4);
		carrier = new Ship(5);
		
		//sets each location in the array to false
		for(int i=0;i<SIDE_LENGTH;i++) {
			for(int j=0;j<SIDE_LENGTH;j++) {
				spaces[i][j] = false;
			}
		}
		
	}
	
	/**
	 * Takes in a ship object, an x coordinate, a y coordinate, and a boolean to determine if it is vertical or not.
	 * It then places the ship onto the grid at that spot.
	 * 
	 * @param S - must be an initialized Ship object.
	 * @param X - must be an int between 1 and 10, inclusive.
	 * @param Y - must be an int between 1 and 10, inclusive.
	 * @param isVertical - determines if the ship is placed vertically or horizontally at the X,Y coordinate given.
	 * @throws Exception when ship is trying to be placed off the board
	 */

	public void placeShip(Ship S, int X, int Y, boolean isVertical) throws Exception {
		int x=X;
		int y=Y;
		boolean isVert = isVertical;
		
		int size = S.getLength();
		
		//these if statements check to see if the edge of the ship would be off the board
		if(isVert && y+size>=9) {
			throw new Exception("The ship does not fit on the board at this location (off the bottom of the board)");
		}
		
		if(!isVert && x+size>=9) {
			throw new Exception("The ship does not fit on the board at this location (off the right side of the board)");
		}
		
		try {
			
			S.setLocation(x, y, isVert);
			
			//if the ship is placed vertically then it starts at the given point and goes downward
			if(isVertical) {
				for(int i=0;i<size;i++) {		//the ship takes up as many spots as its size
					spaces[x][y-i] = true;
				}
			}
			
			//if the ship is placed horizontally then it starts at the given point and moves rightward 
			if(!isVertical) {
				for(int i=0;i<size;i++) {	//the ship takes up as many spots as its size
					spaces[x+i][y] = true;
				}
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		//need to check for errors on size (out-of-bound exceptions) either in here or where this will be called
		//example: if a vertical ship is placed in the last row
	}

	

	/**
	 * Handles a players attempt to hit the opponent's ships. 
	 * It uses methods from ship like isHit and isSunk.
	 */
	//i have no idea what the point of this class is 
	//changed to have parameters
	public void attack(int X, int Y) {
		if(!isEmpty(X,Y)) {
			if(carrier.isHit(X,Y) || cruiser.isHit(X,Y) || submarine.isHit(X,Y) ||
					destroyer.isHit(X,Y) || battleship.isHit(X,Y)) {
				spaces[X][Y] = false;
			}
		}
		
		
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
		if(spaces[X][Y]==true) {
			return false;
		}
		return true;
	}

}