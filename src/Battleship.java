import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Top level class responsible for running the battleship game through the Game class..
 */
public class Battleship extends Application {
	private static Stage primaryStage;
	private boolean answ;
	private Grid playerGrid;
	private Grid aiGrid;
	public Button[][] grid1;
	public Button[][] grid2;
	private boolean placedShips = false;
	private static boolean gameOver = false;
	private int numPlayerAtt;
	private int numAiAtt;
	private Label l;

	/**
	 * sets up the GUI
	 */
	@Override
	public void start(Stage ps) {
		grid1 = new Button[10][10];//The grid of buttons for where the player attacks.
		grid2 = new Button[10][10];//grid of buttons for the players board.

		playerGrid = new Grid();//players ships
		aiGrid = new Grid();//ai's ships

		aiGrid.randomSet();//placed ai's ships

		primaryStage = ps;//assigns the primary stage from the one taken in by start
		GridPane g1 = new GridPane();//creates the grid panes.
		g1 = getP1();//sets the gridpanes using their respective methods.
		GridPane g2 = new GridPane();
		g2 = getP2();
		
		display("Order", "Places Carrier, Battleship, Cruiser, Submarine, then the Destroyer.");

		l = new Label("Left click to place Horizontally to the Right, Right click \n to place vertically down \n places ships in order from largest to smallest.");
		l.setTextFill(Color.WHITE);//creates text for the GUI
		Label atB = new Label("Attack Board");
		atB.setTextFill(Color.WHITE);
		Label ownB = new Label("Your Own Board");
		ownB.setTextFill(Color.WHITE);
		VBox  v = new VBox(atB, g1, l,  ownB, g2);//initialize the Vbox for the scene.
		v.setPadding(new Insets(10, 20, 10, 20));//sets spacing around the VBox
		v.backgroundProperty().set(new Background(new BackgroundFill(Color.NAVY, CornerRadii.EMPTY, Insets.EMPTY)));//sets background color
		v.setMinSize(350, 620);//set default window sizes.
		v.setMaxSize(350, 620);
		Scene scene = new Scene(v);//sets the vbox to the scene.
		primaryStage.setScene(scene);//sets the scene to the stage.
		primaryStage.show();//displays the stage.
		primaryStage.setOnCloseRequest(e -> {//this intercepts close program requests to ask the user if they are sure.
			e.consume();//this line is to point out the fact that it is called consume.
			closeProgram();//runs closeProgram method
		});
	}

	/**
	 * creates the buttons for attacking.
	 * @param text the name of button, should be coordinates.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return a button with inputs and attack functionality.
	 */
	private Button createButton(String text, int x, int y) {
		Button button = new Button("");//creates the button
		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);//sets window size
		button.setStyle("-fx-background-color: #0080ff");//default color
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {//button functionality.

			/**
			 * sets what the buttons do, which is attack.
			 */
			@Override
			public void handle(MouseEvent event) {
				if(placedShips) {//*if all ships are placed.
					MouseButton btn = event.getButton();//gets what type of click it was.
					if(btn==MouseButton.PRIMARY){
						if(button.getStyle() == "-fx-background-color: #FFFFFF" || button.getStyle() == "-fx-background-color: #FF0000") {
							display("Already Hit", "This tile has already been hit, please select again");
						}//does nothing if they already attacked the tile.
						else {
							if(aiGrid.attack(x, y)) {//if its a hit.
								button.setStyle("-fx-background-color: #FF0000");//changes color.
							}
							else {//if its a miss.
								button.setStyle("-fx-background-color: #FFFFFF");
							}
							if(aiGrid.allShipsSunk()) {//checks if the player won
								gameOver = true;//so that the game ends
								display("Victory", "You sank all their ships");

							}
							numPlayerAtt++;//adds to number of times player has attacked;
						}
						if(numPlayerAtt > numAiAtt) {//if the AI has attacked less times
							if(playerGrid.aiAttack()) {//if AI hits
								grid2[playerGrid.getLastHitY()][playerGrid.getLastHitX()].setStyle("-fx-background-color: #FF0000");
							}
							else {//if Ai misses
								grid2[playerGrid.getLastHitY()][playerGrid.getLastHitX()].setStyle("-fx-background-color: #FFFFFF");
							}

							if(playerGrid.allShipsSunk()) {//if all the players ships are trunk.
								gameOver = true;//sets game to be over
								display("Defeat", "All your ships were sunk.");

							}	
							numAiAtt++;//adds to number of times the AI attacked.
						}
					}
				}
				else {//tells the user to place ships before attacking.
					display("noShips","Place ships first");
				}
			}
		});
		return button ;//returns the button.
	}
	/**
	 * Button on the grid for placing ships
	 * @param text buttons name
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return a button that can place ships and once all the ships are placed it does nothing.
	 */
	private Button createPlaceButton(String text, int x, int y) {
		Button button = new Button("");//new butto
		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		button.setStyle("-fx-background-color: #0080ff");//changes color of the button.
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {//on click

			@Override
			public void handle(MouseEvent event) {
				Ship nS = playerGrid.nextShip();//gets the next ship to be placed
				MouseButton btn = event.getButton();//button clicked
				if(btn==MouseButton.PRIMARY){//if left click
					if(!nS.isPlaced() && !nS.isLast()) {//makes sure its not placed and not the last ship which is nullship.
						try {//places
							playerGrid.placeShip(nS, x, y, false);//places the ship at x, y, and vertical is false, so horizontal.
							nS.placed();//tells the ship class that the ship is placed.
							for(int i = 0; i < nS.getLength(); i++) {//sets the grid for each tile the ship takes up.
								grid2[x+i][y].setStyle("-fx-background-color: #A9A9A9");
							}

						}
						catch(Exception e) {//catches exception thrown if they place either out of bounds or overlapping.
							display("Error", e.getMessage());//tells the user why the issue happened.
						}

					}
					Ship cS = playerGrid.nextShip();//makes a copy of the ship.
					if(cS.isLast()) {//checks if its the last.
						display("All Placed", "All ships have been placed");
						l.setTextFill(Color.NAVY);//makes the label dissapear
						placedShips = true;//if last, tells the player and sets placeShips to be true, so that they can attack
						for(int i = 0; i < 10; i++) {//makes all the buttons do nothing once ships are placed.
							for(int j = 0; j < 10; j++) {
								grid2[i][j].setOnMouseClicked(e -> {});//buttons now do nothing.
							}
						}
					}
				}
				else if(btn == MouseButton.SECONDARY){//if right click
					if(!nS.isPlaced() && !nS.isLast()) {//makes sure its not placed or nullship
						try{
							playerGrid.placeShip(nS, x, y, true);//places ship at x, y, vertical is true.
							nS.placed();//places the ship.
							for(int i = 0; i < nS.getLength(); i++) {//changes colors of tiles for the length of the ship.
								grid2[x][y+i].setStyle("-fx-background-color: #A9A9A9");//changes color.
							}

						}
						catch(Exception e) {//displays errors on how they tried to place it.
							display("Error", e.getMessage());
						}

					}
					Ship cS = playerGrid.nextShip();//gets the next ship.
					if(cS.isLast()) {//if its last tells the user all ships have been placed
						display("All Placed", "All ships have been placed");
						placedShips = true;//changes placedShips to false so they can attack.
						for(int i = 0; i < 10; i++) {
							for(int j = 0; j < 10; j++) {//makes all of the buttons do nothing
								grid2[i][j].setOnMouseClicked(e -> {});
							}
						}
					}


				}
			}

		});
		return button ;//returns these buttons.
	}
	public GridPane getP1() {//makes the pane for attacking.
		GridPane grid = new GridPane();
		int numRows = 10;//for size of Gridpane
		int numColumns = 10;
		for (int row = 0 ; row < numRows ; row++ ){
			RowConstraints rc = new RowConstraints();//Sets constraints
			rc.setFillHeight(true);//sets it to fill Height
			rc.setVgrow(Priority.ALWAYS);//sets it to prioritize the Gridpane in the VBox
			grid.getRowConstraints().add(rc);//adds constraint to the gridpane.
		}
		for (int col = 0 ; col < numColumns; col++ ) {//sets width constraints
			ColumnConstraints cc = new ColumnConstraints();//column constraints
			cc.setFillWidth(true);//Sets it to fill width
			cc.setHgrow(Priority.ALWAYS);//makes it prioritize spreading for the width to match edges.
			grid.getColumnConstraints().add(cc);//adds the constraint.
			grid.setHgap(1.5); //sets the sideways gap
			grid.setVgap(1.5); //sets vertival gap.
		}

		for (int i = 0; i < 100; i++) {//adds the 100 buttons for attacking.
			Button button = createButton(Integer.toString(i%10, i/10), i%10, i/10);
			grid1[i%10][i/10] = button;//makes rows and columns
			grid.add(button, i % 10, i / 10);
		}

		return grid;//returns the grid.
	}
	/**
	 * 
	 * @return A Grid where you can place ships and see AI attacks.
	 */
	public GridPane getP2() {//gets ship placing grid.
		GridPane grid = new GridPane();
		int numRows = 10;//rows and columns
		int numColumns = 10;
		for (int row = 0 ; row < numRows ; row++ ){
			RowConstraints rc = new RowConstraints();//sets constraints.
			rc.setFillHeight(true);//same constraints as before
			rc.setVgrow(Priority.ALWAYS);
			grid.getRowConstraints().add(rc);
		}
		for (int col = 0 ; col < numColumns; col++ ) {//sets column constrants
			ColumnConstraints cc = new ColumnConstraints();
			cc.setFillWidth(true);//same as before
			cc.setHgrow(Priority.ALWAYS);
			grid.getColumnConstraints().add(cc);
			grid.setHgap(1.5); //sets gap, same as before.
			grid.setVgap(1.5); 
		}

		for (int i = 0; i < 100; i++) {//100 buttons that you can place ships on
			Button button = createPlaceButton(Integer.toString(i%10, i/10), i%10, i/10);
			grid2[i%10][i/10] = button;//10 rows 10 columns
			grid.add(button, i % 10, i / 10);
		}

		return grid;//returns the grid
	}

	/**
	 * launches start.
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args.clone());

	}
	/**
	 * handles close requests, so it confirms they wanted to close.
	 */
	public void closeProgram() {
		Boolean an = ConfirmBox("Close?", "Yes to close, No to keep it open");
		if(an) {
			primaryStage.close();//closes window.
		}
	}
	/**
	 * displays a message
	 * @param title of message
	 * @param message displays in the middle of the box.
	 */
	public static void display(String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);//forces them to deal with the pop up before returning
		window.setTitle(title);//sets title
		window.setMinWidth(250);//sets minimum width

		Label label = new Label();//the message.
		label.setText(message);//sets the label to be the message.
		String cl;//for differentiating between end game pop up and just a normal one
		if(gameOver) {//if the games over it shows close game
			cl = "Close Game";
		}
		else {//if not it just shows close window
			cl = "Close Window";
		}
		Button closeButton = new Button(cl);//the button to close either or game
		closeButton.setOnAction(e -> {//on the button activated,
			window.close();//closes window
			if(gameOver) {//if game is over
				primaryStage.close();//closes program.
			}
		});

		VBox lay = new VBox(10);//to display the pieces of the scene
		lay.getChildren().addAll(label, closeButton);//adds the label and button
		lay.setAlignment(Pos.CENTER);//alligns the button to the center

		Scene sce = new Scene(lay);//a Scene with the layour
		window.setScene(sce);//sets window of the scene
		window.showAndWait();//shows and waits until its closed.


	}
	/**
	 * box to confirm yes or no
	 * @param title title
	 * @param message what is being asked
	 * @return true if they hit yes, false if no
	 */
	public boolean ConfirmBox(String title, String message) {
		Stage window = new Stage();//new stage
		window.initModality(Modality.APPLICATION_MODAL);//they have to do this before anything else
		window.setTitle(title);//title
		window.setMinWidth(250);//width
		Label label = new Label();
		label.setText(message);//sets label to be the message

		Button yes = new Button("Yes");//yes and no buttons
		Button no = new Button("No");

		yes.setOnAction(e -> {//if they hit yes it returns true.
			answ = true;
			window.close();//closes window
		});

		no.setOnAction(e -> {//if they hit no it returns false
			answ = false;
			window.close();//close window
		});

		VBox lay = new VBox(10);//layout with 2 buttons and label
		lay.getChildren().addAll(label, yes, no);//adds label and yes/no buttons
		lay.setAlignment(Pos.CENTER);//centers it
		Scene sce = new Scene(lay);//adds it to the scene
		window.setScene(sce);//adds the scene to the window.
		window.showAndWait();//waits for response

		return answ;//returns true if they hit yes, false if the hit no
	}

}