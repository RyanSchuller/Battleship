import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents the multiple grids that the player sees and interacts with while playing the game.
 */

public class Grid {
	private static final int SIDE_LENGTH = 10;
	private boolean[][] spaces;
	public Ship carrier;
	public Ship battleship;
	public Ship cruiser;
	public Ship submarine;
	public Ship destroyer;
	private Ship nullship;


	/**
	 * Constructor for Grid objects. Initializes the 2D array spaces to be empty initially. 
	 */
	public Grid() {
		spaces = new boolean[SIDE_LENGTH][SIDE_LENGTH];
		destroyer = new Ship(2);
		submarine = new Ship(3);
		cruiser = new Ship(3);
		battleship = new Ship(4);
		carrier = new Ship(5);
		nullship = new Ship(100);

		//sets each location in the array to false
		for(int i=0;i<SIDE_LENGTH;i++) {
			for(int j=0;j<SIDE_LENGTH;j++) {
				spaces[i][j] = false;
			}
		}

	}
	/**
	 * Checks every ship on the grid to see if they've all been sunk.
	 * 
	 * @return Whether a player has lost all of their ships or not. 
	 */
	public boolean allShipsSunk() {
		return destroyer.isSunk() && submarine.isSunk() 
				&& cruiser.isSunk() && battleship.isSunk() && carrier.isSunk();
	}

	
	/**
	 * Randomly places each of the five ships for the given grid.
	 */
	public void randomSet() {
		ArrayList<Ship> shipsList = new ArrayList<>();
		shipsList.add(carrier);
		shipsList.add(battleship);
		shipsList.add(cruiser);
		shipsList.add(submarine);
		shipsList.add(destroyer);
		
		Random rand = new Random();
		boolean placed = false;
		
		for(Ship s : shipsList) {
			placed = false;
			while(!placed) {
				try {
					placeShip(s,rand.nextInt(10), rand.nextInt(10), rand.nextBoolean());
					placed = true;
				}
				catch(Exception e) {
				}
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

		int size = S.getLength() -1;

		//these if statements check to see if the edge of the ship would be off the board
		if(isVert && y+size>=10) {
			throw new Exception("The ship does not fit on the board at this location (off the bottom of the board) + size");
		}

		if(!isVert && x+size>=10) {
			throw new Exception("The ship does not fit on the board at this location (off the right side of the board)" + size);
		}
		boolean overlap = false;
		if(isVert) {
			for(int i = 0; i < size; i++) {
				if(spaces[x][y+i]) {
					overlap = true;
				}
			}
		}
		else {
			for(int i = 0; i < size; i++) {
				if(spaces[x+i][y]) {
					overlap = true;
				}
			}
		}
		if(overlap) {
			throw new Exception("Ships Overlap");
		}

		try {

			S.setLocation(x, y, isVert);

			//if the ship is placed vertically then it starts at the given point and goes downward
			if(isVertical) {
				for(int i=0;i<size;i++) {		//the ship takes up as many spots as its size
					spaces[x][y+i] = true;
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
	 * Handles a players attempt to hit the opponent's ships.. 
	 * It uses methods from ship like isHit and isSunk.
	 */

	public boolean attack(int X, int Y) {
		boolean hit = false;
		if(!isEmpty(X,Y)) {
			if(carrier.isHit(X,Y) || cruiser.isHit(X,Y) || submarine.isHit(X,Y) ||
					destroyer.isHit(X,Y) || battleship.isHit(X,Y)) {
				spaces[X][Y] = false;
				hit = true;
			}
		}
		return hit;

	}

	/**
	 * Checks a given spot on the grid and returns whether the spot is empty or not.
	 * 
	 * This method can be used in attack
	 * @param X - must be an int between 1 and 10, inclusive.
	 * @param Y - must be an int between 1 and 10, inclusive.
	 * @return true if the given space is empty, false if it is occupied by a ship.
	 */
	public boolean isEmpty(int X, int Y) {
		if(spaces[X][Y]==true) {
			return false;
		}
		return true;
	}

	public Ship nextShip(){
		if(!carrier.isPlaced()) {
			return carrier;
		}
		else if(!battleship.isPlaced()) {
			return battleship;
		}
		else if(!cruiser.isPlaced()) {
			return cruiser;
		}
		else if(!submarine.isPlaced()) {
			return submarine;
		}
		else if(!destroyer.isPlaced()) {
			return destroyer;
		}
		else {
			return nullship;
		}
			
	}

}