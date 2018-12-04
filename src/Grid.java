import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * This class represents the grids that the player sees and interacts with while playing the game.
*/
public class Grid {
	
	// Creating a final int SIDE_LENGTH as 10 to be used when initializing the arrays. 
	private static final int SIDE_LENGTH = 10;
	
	// Creating two boolean 2D arrays, one for whether or not a ship is present in a space, 
	//	 and one for whether or not a space has been hit already.
	private boolean[][] spaces;
	private boolean[][] hitAlready;
	
	// Creating a ship object for each ship in the game of Battleship, 
	//	 plus another ship, nullship, for use in the nextShip() method.
	public Ship carrier;
	public Ship battleship;
	public Ship cruiser;
	public Ship submarine;
	public Ship destroyer;
	private Ship nullship;
	
	// Creating an arraylist to hold coordinates of recently hit spots on the player's grid,
	//	 as well as a boolean variable, both to be used in aiAttack().
	private ArrayList<int[]> hitCoord;
	private boolean aiLowerHit;
	
	// Creating two variables to hold the last hit coordinates from the aiAttack method.
	private int lastHitX;
	private int lastHitY;

	/**
	 * Constructor for Grid objects. 
	 * Initializes the 2D arrays, ship objects, and data members for aiAttack(). 
	 * Also sets all spaces in the 2D arrays to be false.
	 * 
	 * @author DrayDR
	 */
	public Grid() {
		
		// Initializing the two boolean 2D arrays.
		spaces = new boolean[SIDE_LENGTH][SIDE_LENGTH];
		hitAlready = new boolean[SIDE_LENGTH][SIDE_LENGTH]; 
		
		// Initializing the ship objects.
		destroyer = new Ship(2);
		submarine = new Ship(3);
		cruiser = new Ship(3);
		battleship = new Ship(4);
		carrier = new Ship(5);
		nullship = new Ship(100);
		
		// Initializing the list of hit coordinates for the aiAttack() method.
		hitCoord = new ArrayList<>();
		
		// Initializing a boolean variable used in the aiAttack() method.
		aiLowerHit = false;

		// Sets each location in two arrays to false.
		for(int i=0;i<SIDE_LENGTH;i++) {
			for(int j=0;j<SIDE_LENGTH;j++) {
				spaces[i][j] = false;
				hitAlready[i][j] = false;
			}
		}

	}
	
	/**
	 * Getter method for lastHitX. 
	 * 
	 * @returns The integer variable lastHitX.
	 * @Author SchullerRJ
	 */
	public int getLastHitX() {
		return lastHitX;
	}
	
	/**
	 * Getter method for lastHitY. 
	 * 
	 * @returns The integer variable lastHitY.
	 * @Author SchullerRJ
	 */
	public int getLastHitY() {
		return lastHitY;
	}
	
	/**
	 * Checks every ship on the grid to see if they've all been sunk.
	 * 
	 * @return Whether a player has lost all of their ships or not. 
	 * @author SchullerRJ
	 */
	public boolean allShipsSunk() {
		return destroyer.isSunk() && submarine.isSunk() 
				&& cruiser.isSunk() && battleship.isSunk() && carrier.isSunk();
	}

	
	/**
	 * Randomly places each of the five ships for the given grid.
	 * Used to place ships for the AI player.
	 * 
	 * @author SchullerRJ
	 */
	public void randomSet() {
		
		// Creating a set of the five ships to be used in the for each loop below.
		HashSet<Ship> shipsList = new HashSet<>();
		shipsList.add(carrier);
		shipsList.add(battleship);
		shipsList.add(cruiser);
		shipsList.add(submarine);
		shipsList.add(destroyer);
		
		// Creating a random object to generate the coordinates for placing ships.
		Random rand = new Random();
		
		// Creating a boolean to keep track of if the ship was placed successfully or not.
		boolean placed = false;
		
		// For each ship in the set (each of the ships in Battleship), 
		//	 keep trying to randomly place the ship until it can be placed without overlap.
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
	 * @throws Exception when ship is trying to be placed off the board.
	 * @author DrayDR
	 */
	public void placeShip(Ship S, int Y, int X, boolean isVertical) throws Exception {
		
		// Creating an x and a y variable to hold the coordinates of the head of the ship.
		int y = Y;
		int x = X;
		
		// Creating a boolean variable to hold the parameter isVertical. 
		boolean isVert = isVertical;

		// Creating a size variable to hold the size of the ship minus the initial spot.
		int size = S.getLength()-1;

		// These if statements check to see if the edge of the ship would be off the board
		if(isVert && x+size>=10) {
			throw new Exception("The ship does not fit on the board at this location (off the bottom of the board)");
		}
		if(!isVert && y+size>=10) {
			throw new Exception("The ship does not fit on the board at this location (off the right side of the board)");
		}
		
		// The following section checks for potential overlap between ships when placing. 
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

		// Tries to place the ship at the specified location, catches and prints any exceptions that occur.
		try {
			S.setLocation(y, x, isVert);

			//if the ship is placed vertically then it starts at the given point and goes downward
			if(isVertical) {
				for(int i=0;i<size+1;i++) {	//the ship takes up as many spots as its size
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
	}

	/**
	 * Handles a players attempt to hit the opponent's ships.
	 * 
	 * @return If the attack was successful or not.
	 * @author DrayDR
	 */
	public boolean attack(int y, int x) {
		
		// Creating a boolean variable to keep track of whether the hit was successful or not.
		boolean hit = false;
		
			// If any of the ships have been hit, set the hit variable to true.
			if(carrier.isHit(y,x) || cruiser.isHit(y,x) || submarine.isHit(y,x) ||
					destroyer.isHit(y,x) || battleship.isHit(y,x)) {
				hit = true;
			}
			hitAlready[y][x] = true;	//updates the array of locations that have been hit
			return hit;	//returns if the attack hit any ship
	}
		
	/** 
	 * Attacks the player's grid using random and logic based attacks.
	 * 
	 * @return If the attack was successful or not.
	 * @author DrayDR
	 */
	public boolean aiAttack() {		
		
		int y;	//a y coordinate for the ai's attack
		int x;	//an x coordinate for the ai's attack
		Random rand = new Random();	//random used for a randomized attack
		boolean attacked = false;	//if the ai ended up attacking or not
		boolean hit = false;		//if the attack resulted in a hit ship
		
		while(!attacked) {		//while the ai has not attacked on his turn
			
			if(hitCoord.size()==0) {	// If there isn't a known ship nearby.
				
				//creates an attack at a random x and y coordinate between 0 and 9
				int a = rand.nextInt(10);
				int b = rand.nextInt(10);
				
				if(!hitAlready[a][b]) { 	//if the location that was randomly generated has not already been attacked
					hit = attack(a,b);	//attacks at the randomized location using the attack method
					//adds the newly hit location to the list of hit locations
					lastHitX = a;
					lastHitY = b;
					hitAlready[a][b] = true;
					
					//if the attack was successful it adds the coordinates of the successful hit to an arraylist
					if(hit) {
						int[] coordinates = {a,b};
						hitCoord.add(coordinates);
					}
					attacked = true;	//attacked is set to true to end the loop since the ai was able to attack
				}
			}
			else if(hitCoord.size()==1) {		//if they just got a successful hit
				//uses the coordinates from the successful hit
				y = hitCoord.get(0)[0];
				x = hitCoord.get(0)[1];
				
				//hits below the successful hit
				if(y+1<10 && !hitAlready[y+1][x]) {		//if the hit will be on the board and has not been hit yet
					hit = attack(y+1,x);		//attacks the location below the successful hit
					lastHitX = y+1;
					lastHitY = x;
					hitAlready[y+1][x] = true;	//adds the coordinates to the list of hit locations
					if(hit) {
						int[] coordinates = {y+1,x};	
						hitCoord.add(coordinates);	//if it was a successful hit, adds the coordinates to the arraylist of hit ships' locations
					}
					attacked = true;	//attacked is set to true to end the loop since the ai was able to attack
				}
				
				//hits left of the successful hit
				else if(x-1>=0 && !hitAlready[y][x-1]) {	//if the hit will be on the board and has not been hit yet
					hit = attack(y,x-1);		//attacks the location left of the successful hit
					lastHitX = y;
					lastHitY = x-1;
					hitAlready[y][x-1] = true;	//adds the coordinates to the list of hit locations
					if(hit) {
						int[] coordinates = {y,x-1};
						hitCoord.add(coordinates);	//if it was a successful hit, adds the coordinates to the arraylist of hit ships' locations
					}
					attacked = true;	//attacked is set to true to end the loop since the ai was able to attack
				}
				
				//hits above the successful hit
				else if(y-1>=0 && !hitAlready[y-1][x]) {	//if the hit will be on the board and has not been hit yet
					hit = attack(y-1,x);		//attacks the location above the successful hit
					lastHitX = y-1;
					lastHitY = x;
					hitAlready[y-1][x] = true;	//adds the coordinates to the list of hit locations
					if(hit) {
						int[] coordinates = {y-1,x};
						hitCoord.add(coordinates);	//if it was a successful hit, adds the coordinates to the arraylist of hit ships' locations
					}
					attacked = true;	//attacked is set to true to end the loop since the ai was able to attack
				}
				
				//hits right of the successful hit
				else if(x+1<10 && !hitAlready[y][x+1]) {	//if the hit will be on the board and has not been hit yet
					hit = attack(y,x+1);		//attacks the location right of the successful hit
					lastHitX = y;
					lastHitY = x+1;
					hitAlready[y][x+1] = true;	//adds the coordinates to the list of hit locations
					if(hit) {
						int[] coordinates = {y,x+1};
						hitCoord.add(coordinates);	//if it was a successful hit, adds the coordinates to the arraylist of hit ships' locations
					}
					attacked = true;	//attacked is set to true to end the loop since the ai was able to attack
				}
				else {	//if there was no hit in any of the 4 locations around the successful hit
					hitCoord= new ArrayList<int[]>();	//makes the arraylist of hits a new arraylist to clear any hits
					//the ai will then go back to random guessing
				}
			}
			
			else if(hitCoord.size()>1) {		// if the user hits the ship multiple times
				
				//sets the x and y coordinates equal to the most recent successful hit
				y = hitCoord.get(hitCoord.size()-1)[0];
				x = hitCoord.get(hitCoord.size()-1)[1];
				
				//used initially as the original hit, but changed to the newest hit if the ai switches 
				//directions for which way it is attacking a ship (if it guesses in the middle of a 
				//ship and needs to check both sides)
				int lowerY;	
				int lowerX;
				
				if(!aiLowerHit) {		//if the ai has not hit another spot since switching directions yet
					//the lower values are set to the original hit
					lowerY = hitCoord.get(0)[0];
					lowerX = hitCoord.get(0)[1];
				}
				else {	//if the ai has hit a spot successfully after switching directions
					//the lower coordinates are updated to be the most recent hit and the ai begins
					//to attack more in the new direction
					lowerY = hitCoord.get(hitCoord.size()-1)[0];
					lowerX = hitCoord.get(hitCoord.size()-1)[1];

				}
				
				if(hitCoord.get(0)[0]==hitCoord.get(1)[0]) {	//if the y values are the same
																//(horizontal)
		
		//if the second hit is right of the first hit
					if(hitCoord.get(0)[1]<hitCoord.get(1)[1]) {
						//only for horizontal guessing
						
						//checks right of the most recent hit
						if(x+1<10 && !hitAlready[y][x+1]) {	//if the next guess is on the board and has not been attacked yet
							hit = attack(y,x+1);		//it attacks the next location
							lastHitX = y;
							lastHitY = x+1;
							hitAlready[y][x+1] = true;	//updates the list of attacked locations
							if(hit) {
								int[] coordinates = {y,x+1};
								hitCoord.add(coordinates);		//if the attack was successful, it adds the coordinates of the hits to the arraylist
							}
							attacked = true;  //attacked is set to true to end the loop since the ai was able to attack
						}
						
						//checks left of the original hit (or the previous hit if the ai has already switched directions
						else if(lowerX-1>=0 && !hitAlready[lowerY][lowerX-1]) {	//if the next guess is on the board and has not been attacked yet
							hit = attack(lowerY,lowerX-1);		//it attacks the next location
							lastHitX = lowerY;
							lastHitY = lowerX-1;
							hitAlready[lowerY][lowerX-1] = true;	//updates the list of attacked locations
							if(hit) {
								int[] coordinates = {lowerY,lowerX-1};
								hitCoord.add(coordinates);	//if the attack was successful, it adds the coordinates of the hits to the arraylist
								lowerY = hitCoord.get(hitCoord.size()-1)[0];
								lowerX = hitCoord.get(hitCoord.size()-1)[1];	//changes the lower coordinates to the most recent successful hit
								aiLowerHit = true;	//makes it so that the lower coordinates will not be set to the original coordinates every time it enters the loop
							}
							attacked = true;	//attacked is set to true to end the loop since the ai was able to attack
						}
						
						//if the ai doesn't get any more successful hits
						else {
							// the arraylist of successful hits will set to a new arraylist causing the ai to go back to random guessing
							hitCoord= new ArrayList<int[]>();
						}
						
					}

		//if the second hit is left of the first hit				
					else if(hitCoord.get(0)[1]>hitCoord.get(1)[1]) {	
						//only for horizontal guessing
						
						//checks left of the most recent hit
						if(x-1>=0 && !hitAlready[y][x-1]) {	//if the next attack is still on the board and it hasnt been attacked yet
							hit = attack(y,x-1);		//attacks at this new location
							lastHitX = y;
							lastHitY = x-1;
							hitAlready[y][x-1] = true;		//updates the array storing all attacked locations
							if(hit) {
								int[] coordinates = {y,x-1};
								hitCoord.add(coordinates);	//adds the successful hit to the arraylist if there was one
							}
							attacked = true;	//attacked is set to true to end the loop since the ai was able to attack
						}
						
						//checks right of the original hit
						else if(lowerX+1<10 && !hitAlready[lowerY][lowerX+1]) {	//if the next attack is still on the board and it hasnt been attacked yet
							hit = attack(lowerY,lowerX+1);		//attacks at that location
							lastHitX = lowerY;
							lastHitY = lowerX+1;
							hitAlready[lowerY][lowerX+1] = true;		//updates the array of all attacked locations
							if(hit) {
								int[] coordinates = {lowerY,lowerX+1};
								hitCoord.add(coordinates);	//adds the successful hit to the arraylist if there was one
								lowerY = hitCoord.get(hitCoord.size()-1)[0];
								lowerX = hitCoord.get(hitCoord.size()-1)[1];	//updates the lower coordinates to the most recent hit if it was successful
								aiLowerHit = true;	//now the lower coordinates will not be set to the original coordinates every time it enters the loop
							}
							attacked = true;	//attacked is set to true to end the loop since the ai was able to attack
						}	
						//if the ai doesn't get any more successful hits
						else {
							// the arraylist of successful hits will set to a new arraylist causing the ai to go back to random guessing
							hitCoord= new ArrayList<int[]>();
						}
					}
				}
				else {		//if it is vertical
					
		//if the second hit is below the first hit
					if(hitCoord.get(0)[0]<hitCoord.get(1)[0]) {	
						//only for vertical guessing
						
						//checks below the most recent hit if its on the board and hasnt been attacked yet
						if(y+1<10 && !hitAlready[y+1][x]) {	
							hit = attack(y+1,x);	//attacks at new locations
							lastHitX = y+1;
							lastHitY = x;
							hitAlready[y+1][x] = true;	//updates array of attacks
							if(hit) {
								int[] coordinates = {y+1,x};
								hitCoord.add(coordinates); //updates arraylist of successful attacks if necessary
							}
							attacked = true;	//updates boolean attacked to switch turns
						}
						
						//checks above the original hit if its in bounds and hasnt been hit yet
						else if(lowerY-1>=0 && !hitAlready[lowerY-1][lowerX]) {	
							hit = attack(lowerY-1,lowerX);	//attacks at new location
							lastHitX = lowerY-1;
							lastHitY = lowerX;
							hitAlready[lowerY-1][lowerX] = true;	//updates array of attacks
							if(hit) {
								int[] coordinates = {lowerY-1,lowerX};
								hitCoord.add(coordinates);	//updates arraylist of successes if the attack was successful
								lowerY = hitCoord.get(hitCoord.size()-1)[0];
								lowerX = hitCoord.get(hitCoord.size()-1)[1];
								aiLowerHit = true;//updates this boolean to prevent the lower coordinates from always being the original hit
							}
							attacked = true;	//updates this boolean to indicate the ai's turn is over
						}
						//if the ai doesn't get any more successful hits
						else {
							// the arraylist of successful hits will set to a new arraylist causing the ai to go back to random guessing
							hitCoord= new ArrayList<int[]>();
						}
					}
					
		//if the second hit is above the first hit
					else if(hitCoord.get(0)[0]>hitCoord.get(1)[0]) {	
						//only for vertical guessing
						
						//checks above the recent hit if its on the board and hasnt been attacked yet
						if(y-1>=0 && !hitAlready[y-1][x]) {	
							hit = attack(y-1,x);	//attacks at this location
							lastHitX = y-1;
							lastHitY = x;
							hitAlready[y-1][x] = true;	//updates array storing all attacks to have this attacked location
							if(hit) {
								int[] coordinates = {y-1,x};
								hitCoord.add(coordinates);	//if the attack was a success it updates the arraylist
							}
							attacked = true;	//updates the boolean to indicate its the player's turn
						}
						
						//checks below the original hit if that location is on the board and has yet to be attacked
						else if(lowerY-1>=0 && !hitAlready[lowerY-1][lowerX]) {	
							hit = attack(lowerY-1,lowerX);	//attacks this location
							lastHitX = lowerY-1;
							lastHitY = lowerX;
							hitAlready[lowerY-1][lowerX] = true;	//adds this location to the list of attacked locations
							if(hit) {
								int[] coordinates = {lowerY-1,lowerX};
								hitCoord.add(coordinates);	//adds the coordinates of the attack to the arraylist if the attack was successful
								lowerY = hitCoord.get(hitCoord.size()-1)[0];
								lowerX = hitCoord.get(hitCoord.size()-1)[1];
								aiLowerHit = true;	//changes the boolean to true to prevent the coordinates from being at the original hit location again
							}
							attacked = true;	//updates the boolean to indicate the ai's turn is over
						}	
						//if the ai doesn't get any more successful hits
						else {
							// the arraylist of successful hits will set to a new arraylist causing the ai to go back to random guessing
							hitCoord= new ArrayList<int[]>();
						}
					}
				}
			}
		}	
		return hit;	//returns if the attack was successful or not
	}

	/**
	 * Checks a given spot on the grid and returns whether the spot is empty or not.
	 * 
	 * This method can be used in attack
	 * @param y - must be an int between 1 and 10, inclusive.
	 * @param x - must be an int between 1 and 10, inclusive.
	 * @return true if the given space is empty, false if it is occupied by a ship.
	 * @author DrayDr
	 */
	public boolean isEmpty(int y, int x) {
		if(spaces[y][x]) {		//if the location from the parameters in the array is true
			return false;		//if  it is true then the spot is not empty (it has a ship)
		}
		return true;		//otherwise it is empty (there's no ship at the location)
	}

	/** 
	 * Checks to see what the next ship to place should be and returns it.
	 * in Order
	 * 
	 * @return The next ship that should be placed.
	 * @author BoerJR
	 */
	public Ship nextShip(){
		if(!carrier.isPlaced()) {//returns the carrier if thats next.
			return carrier;
		}
		else if(!battleship.isPlaced()) {//returns battleship if thats next.
			return battleship;
		}
		else if(!cruiser.isPlaced()) {//returns cruiser if thats next
			return cruiser;
		}
		else if(!submarine.isPlaced()) {//returns submarine if thats next
			return submarine;
		}
		else if(!destroyer.isPlaced()) {//returns destroyer if thats next
			return destroyer;
		}
		else {
			return nullship;//returns nullship, so that it can know its out of ships to be placed.
		}
			
	}

}