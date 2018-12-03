import java.util.ArrayList;
import java.util.Random;

/** 
 * This class represents the multiple grids that the player sees and interacts with while playing the game.
 */

public class Grid {
	private static final int SIDE_LENGTH = 10;
	private boolean[][] spaces;
	private boolean[][] hitAlready;
	public Ship carrier;
	public Ship battleship;
	public Ship cruiser;
	public Ship submarine;
	public Ship destroyer;
	private Ship nullship;
	private ArrayList<int[]> hitCoord;
	private boolean aiLowerHit;
	public int lastHitX;
	public int lastHitY;



	/**
	 * Constructor for Grid objects. Initializes the 2D array spaces to be empty initially. 
	 */
	public Grid() {
		spaces = new boolean[SIDE_LENGTH][SIDE_LENGTH];
		hitAlready = new boolean[SIDE_LENGTH][SIDE_LENGTH];
		destroyer = new Ship(2);
		submarine = new Ship(3);
		cruiser = new Ship(3);
		battleship = new Ship(4);
		carrier = new Ship(5);
		nullship = new Ship(100);
		hitCoord = new ArrayList<>();
		aiLowerHit = false;

		//sets each location in the array to false
		for(int i=0;i<SIDE_LENGTH;i++) {
			for(int j=0;j<SIDE_LENGTH;j++) {
				spaces[i][j] = false;
				hitAlready[i][j] = false;
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
	 * Takes in a ship object, an y coordinate, a x coordinate, and a boolean to determine if it is vertical or not.
	 * It then places the ship onto the grid at that spot.
	 * 
	 * @param S - must be an initialized Ship object.
	 * @param y - must be an int between 0 and 9, inclusive.
	 * @param x - must be an int between 0 and 9, inclusive.
	 * @param isVertical - determines if the ship is placed vertically or horizontally at the y,x coordinate given.
	 * @throws Exception when ship is trying to be placed off the board
	 */

	public void placeShip(Ship S, int Y, int X, boolean isVertical) throws Exception {
		int y=Y;
		int x=X;
		boolean isVert = isVertical;

		int size = S.getLength() -1;

		//these if statements check to see if the edge of the ship would be off the board
		if(isVert && x+size>=10) {
			throw new Exception("The ship does not fit on the board at this location (off the bottom of the board) + size");
		}

		if(!isVert && y+size>=10) {
			throw new Exception("The ship does not fit on the board at this location (off the right side of the board)" + size);
		}
		boolean overlap = false;
		if(isVert) {
			for(int i = 0; i < size+1; i++) {
				if(spaces[y][x+i]) {
					overlap = true;
				}
			}
		}
		else {
			for(int i = 0; i < size+1; i++) {
				if(spaces[y+i][x]) {
					overlap = true;
				}
			}
		}
		if(overlap) {
			throw new Exception("Ships Overlap");
		}

		try {

			S.setLocation(y, x, isVert);

			//if the ship is placed vertically then it starts at the given point and goes downward
			if(isVertical) {
				for(int i=0;i<size+1;i++) {		//the ship takes up as many spots as its size
					spaces[y][x+i] = true;
				}
			}

			//if the ship is placed horizontally then it starts at the given point and moves rightward 
			if(!isVertical) {
				for(int i=0;i<size+1;i++) {	//the ship takes up as many spots as its size
					spaces[y+i][x] = true;
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

	public boolean attack(int y, int x) {
		boolean hit = false;
			if(carrier.isHit(y,x) || cruiser.isHit(y,x) || submarine.isHit(y,x) ||
					destroyer.isHit(y,x) || battleship.isHit(y,x)) {
				hit = true;
			}
			hitAlready[y][x] = true;
			return hit;
	}
	
	
	public boolean aiAttack() {		
		
		int y;
		int x;		
		Random rand = new Random();
		boolean attacked = false;
		boolean hit = false;
		
		while(!attacked) {
			
			if(hitCoord.size()==0) {	// If there isn't a known ship nearby.
				
				int a = rand.nextInt(10);
				int b = rand.nextInt(10);
				if(!hitAlready[a][b]) { 
					hit = attack(a,b);
					lastHitX = a;
					lastHitY = b;
					hitAlready[a][b] = true;
					if(hit) {
						int[] coordinates = {a,b};
						hitCoord.add(coordinates);
					}
					attacked = true;
				}
			}
			else if(hitCoord.size()==1) {		//if they just got a successful hit
				y = hitCoord.get(0)[0];
				x = hitCoord.get(0)[1];
				
				if(y+1<10 && !hitAlready[y+1][x]) {		//hits below the successful hit
					hit = attack(y+1,x);
					lastHitX = y+1;
					lastHitY = x;
					hitAlready[y+1][x] = true;
					if(hit) {
						int[] coordinates = {y+1,x};
						hitCoord.add(coordinates);
					}
					attacked = true;
				}
				else if(x-1>=0 && !hitAlready[y][x-1]) {	//hits left of the successful hit
					hit = attack(y,x-1);
					lastHitX = y;
					lastHitY = x-1;
					hitAlready[y][x-1] = true;
					if(hit) {
						int[] coordinates = {y,x-1};
						hitCoord.add(coordinates);
					}
					attacked = true;
				}
				else if(y-1>=0 && !hitAlready[y-1][x]) {	//hits above the successful hit
					hit = attack(y-1,x);
					lastHitX = y-1;
					lastHitY = x;
					hitAlready[y-1][x] = true;
					if(hit) {
						int[] coordinates = {y-1,x};
						hitCoord.add(coordinates);
					}
					attacked = true;
				}
				else if(x+1<10 && !hitAlready[y][x+1]) {	//hits right of the successful hit
					hit = attack(y,x+1);
					lastHitX = y;
					lastHitY = x+1;
					hitAlready[y][x+1] = true;
					if(hit) {
						int[] coordinates = {y,x+1};
						hitCoord.add(coordinates);
					}
					attacked = true;
				}
				else {
					hitCoord= new ArrayList<int[]>();
				}
			}
			
			else if(hitCoord.size()>1) {		// if the user hits the ship multiple times
				
				y = hitCoord.get(hitCoord.size()-1)[0];
				x = hitCoord.get(hitCoord.size()-1)[1];
				
				int lowerY;
				int lowerX;
				if(!aiLowerHit) {
					lowerY = hitCoord.get(0)[0];
					lowerX = hitCoord.get(0)[1];
				}
				else {
					lowerY = hitCoord.get(hitCoord.size()-1)[0];
					lowerX = hitCoord.get(hitCoord.size()-1)[1];

				}
				
				if(hitCoord.get(0)[0]==hitCoord.get(1)[0]) {	//if the y values are the same
																//means horizontal
		
		//if the second hit is right of the first hit
					if(hitCoord.get(0)[1]<hitCoord.get(1)[1]) {
						
						//only for horizontal guessing
						if(x+1<10 && !hitAlready[y][x+1]) {	//checks right of the most recent hit
							hit = attack(y,x+1);
							lastHitX = y;
							lastHitY = x+1;
							hitAlready[y][x+1] = true;
							if(hit) {
								int[] coordinates = {y,x+1};
								hitCoord.add(coordinates);
							}
							attacked = true;
						}
						
						else if(lowerX-1>=0 && !hitAlready[lowerY][lowerX-1]) {	//checks left of the original hit
							hit = attack(lowerY,lowerX-1);
							lastHitX = lowerY;
							lastHitY = lowerX-1;
							hitAlready[lowerY][lowerX-1] = true;
							if(hit) {
								int[] coordinates = {lowerY,lowerX-1};
								hitCoord.add(coordinates);
								lowerY = hitCoord.get(hitCoord.size()-1)[0];
								lowerX = hitCoord.get(hitCoord.size()-1)[1];
								aiLowerHit = true;
							}
							attacked = true;
						}
						else {
							hitCoord= new ArrayList<int[]>();
						}
						
					}

		//if the second hit is left of the first hit				
					else if(hitCoord.get(0)[1]>hitCoord.get(1)[1]) {	
						
						//only for horizontal guessing
						if(x-1>=0 && !hitAlready[y][x-1]) {	//checks left of the most recent hit
							hit = attack(y,x-1);
							lastHitX = y;
							lastHitY = x-1;
							hitAlready[y][x-1] = true;
							if(hit) {
								int[] coordinates = {y,x-1};
								hitCoord.add(coordinates);
							}
							attacked = true;
						}
						
						else if(lowerX+1<10 && !hitAlready[lowerY][lowerX+1]) {	//checks right of the original hit
							hit = attack(lowerY,lowerX+1);
							lastHitX = lowerY;
							lastHitY = lowerX+1;
							hitAlready[lowerY][lowerX+1] = true;
							if(hit) {
								int[] coordinates = {lowerY,lowerX+1};
								hitCoord.add(coordinates);
								lowerY = hitCoord.get(hitCoord.size()-1)[0];
								lowerX = hitCoord.get(hitCoord.size()-1)[1];
								aiLowerHit = true;
							}
							attacked = true;
						}	
						else {
							hitCoord= new ArrayList<int[]>();
						}
					}
				}
				else {		//if it is vertical
					
		//if the second hit is below the first hit
					if(hitCoord.get(0)[0]<hitCoord.get(1)[0]) {	
						
						//only for vertical guessing
						if(y+1<10 && !hitAlready[y+1][x]) {	//checks below the most recent hit
							hit = attack(y+1,x);
							lastHitX = y+1;
							lastHitY = x;
							hitAlready[y+1][x] = true;
							if(hit) {
								int[] coordinates = {y+1,x};
								hitCoord.add(coordinates);
							}
							attacked = true;
						}
						
						else if(lowerY-1>=0 && !hitAlready[lowerY-1][lowerX]) {	//checks above the original hit
							hit = attack(lowerY-1,lowerX);
							lastHitX = lowerY-1;
							lastHitY = lowerX;
							hitAlready[lowerY-1][lowerX] = true;
							if(hit) {
								int[] coordinates = {lowerY-1,lowerX};
								hitCoord.add(coordinates);
								lowerY = hitCoord.get(hitCoord.size()-1)[0];
								lowerX = hitCoord.get(hitCoord.size()-1)[1];
								aiLowerHit = true;
							}
							attacked = true;
						}
						else {
							hitCoord= new ArrayList<int[]>();
						}
					}
					
		//if the second hit is above the first hit
					else if(hitCoord.get(0)[0]>hitCoord.get(1)[0]) {	
						
						//only for vertical guessing
						if(y-1>=0 && !hitAlready[y-1][x]) {	//checks above the recent hit
							hit = attack(y-1,x);
							lastHitX = y-1;
							lastHitY = x;
							hitAlready[y-1][x] = true;
							if(hit) {
								int[] coordinates = {y-1,x};
								hitCoord.add(coordinates);
							}
							attacked = true;
						}
						
						else if(lowerY-1>=0 && !hitAlready[lowerY-1][lowerX]) {	//checks below the original hit
							hit = attack(lowerY-1,lowerX);
							lastHitX = lowerY-1;
							lastHitY = lowerX;
							hitAlready[lowerY-1][lowerX] = true;
							if(hit) {
								int[] coordinates = {lowerY-1,lowerX};
								hitCoord.add(coordinates);
								lowerY = hitCoord.get(hitCoord.size()-1)[0];
								lowerX = hitCoord.get(hitCoord.size()-1)[1];
								aiLowerHit = true;
							}
							attacked = true;
						}	
						else { 
							hitCoord= new ArrayList<int[]>();
						}
					}
				}
			}
		}	
		return hit;
	}

	/**
	 * Checks a given spot on the grid and returns whether the spot is empty or not.
	 * 
	 * This method can be used in attack
	 * @param y - must be an int between 1 and 10, inclusive.
	 * @param x - must be an int between 1 and 10, inclusive.
	 * @return true if the given space is empty, false if it is occupied by a ship.
	 */
	public boolean isEmpty(int y, int x) {
		if(spaces[y][x]==true) {
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