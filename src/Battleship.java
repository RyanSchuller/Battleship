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


		Label l = new Label("Left click to place Horizontally to the Right, Right click \n to place vertically down \n places ships in order from largest to smallest.");
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
				if(placedShips) {//*if all ships are placed
					MouseButton btn = event.getButton();//gets what type of click it was.
					if(btn==MouseButton.PRIMARY){
						if(button.getStyle() == "-fx-background-color: #FFFFFF" || button.getStyle() == "-fx-background-color: #FF0000") {
							display("Already Hit", "This tile has already been hit, please select again");
						}//does nothing if they already attacked the tile.
						else {
							if(aiGrid.attack(x, y)) {
								button.setStyle("-fx-background-color: #FF0000");
							}
							else {
								button.setStyle("-fx-background-color: #FFFFFF");
							}
							if(aiGrid.allShipsSunk()) {
								gameOver = true;
								display("Victory", "You sank all their ships");

							}
							numPlayerAtt++;
						}
						if(numPlayerAtt > numAiAtt) {
							if(playerGrid.aiAttack()) {
								grid2[playerGrid.getLastHitY()][playerGrid.getLastHitX()].setStyle("-fx-background-color: #FF0000");
							}
							else {
								grid2[playerGrid.getLastHitY()][playerGrid.getLastHitX()].setStyle("-fx-background-color: #FFFFFF");
							}

							if(playerGrid.allShipsSunk()) {
								gameOver = true;
								display("Defeat", "Do you want to play again?");

							}	
							numAiAtt++;
						}
					}
				}
				else {
					display("noShips","Place ships first");
				}
			}
		});
		return button ;
	}

	private Button createPlaceButton(String text, int x, int y) {
		Button button = new Button("");
		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		button.setStyle("-fx-background-color: #0080ff");
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Ship nS = playerGrid.nextShip();
				MouseButton btn = event.getButton();
				if(btn==MouseButton.PRIMARY){
					if(!nS.isPlaced() && !nS.isLast()) {
						try {
							playerGrid.placeShip(nS, x, y, false);
							nS.placed();
							for(int i = 0; i < nS.getLength(); i++) {
								grid2[x+i][y].setStyle("-fx-background-color: #A9A9A9");
							}

						}
						catch(Exception e) {
							display("Error", e.getMessage());
						}

					}
					Ship cS = playerGrid.nextShip();
					if(cS.isLast()) {
						display("All Placed", "All ships have been placed");
						placedShips = true;
						for(int i = 0; i < 10; i++) {
							for(int j = 0; j < 10; j++) {
								grid2[i][j].setOnMouseClicked(e -> System.out.print(""));
							}
						}
					}
				}
				else if(btn == MouseButton.SECONDARY){
					if(!nS.isPlaced() && !nS.isLast()) {
						try{
							playerGrid.placeShip(nS, x, y, true);
							nS.placed();
							for(int i = 0; i < nS.getLength(); i++) {
								grid2[x][y+i].setStyle("-fx-background-color: #A9A9A9");
							}

						}
						catch(Exception e) {
							display("Error", e.getMessage());
						}

					}
					Ship cS = playerGrid.nextShip();
					if(cS.isLast()) {
						display("All Placed", "All ships have been placed");
						placedShips = true;
						for(int i = 0; i < 10; i++) {
							for(int j = 0; j < 10; j++) {
								grid2[i][j].setOnMouseClicked(e -> System.out.print(""));
							}
						}
					}


				}
			}

		});
		return button ;
	}
	public GridPane getP1() {
		GridPane grid = new GridPane();
		int numRows = 10;
		int numColumns = 10;
		for (int row = 0 ; row < numRows ; row++ ){
			RowConstraints rc = new RowConstraints();
			rc.setFillHeight(true);
			rc.setVgrow(Priority.ALWAYS);
			grid.getRowConstraints().add(rc);
		}
		for (int col = 0 ; col < numColumns; col++ ) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setFillWidth(true);
			cc.setHgrow(Priority.ALWAYS);
			grid.getColumnConstraints().add(cc);
			grid.setHgap(1.5); 
			grid.setVgap(1.5); 
		}

		for (int i = 0; i < 100; i++) {
			Button button = createButton(Integer.toString(i%10, i/10), i%10, i/10);
			grid1[i%10][i/10] = button;
			grid.add(button, i % 10, i / 10);
		}

		return grid;
	}
	public GridPane getP2() {
		GridPane grid = new GridPane();
		int numRows = 10;
		int numColumns = 10;
		for (int row = 0 ; row < numRows ; row++ ){
			RowConstraints rc = new RowConstraints();
			rc.setFillHeight(true);
			rc.setVgrow(Priority.ALWAYS);
			grid.getRowConstraints().add(rc);
		}
		for (int col = 0 ; col < numColumns; col++ ) {
			ColumnConstraints cc = new ColumnConstraints();
			cc.setFillWidth(true);
			cc.setHgrow(Priority.ALWAYS);
			grid.getColumnConstraints().add(cc);
			grid.setHgap(1.5); 
			grid.setVgap(1.5); 
		}

		for (int i = 0; i < 100; i++) {
			Button button = createPlaceButton(Integer.toString(i%10, i/10), i%10, i/10);
			grid2[i%10][i/10] = button;
			grid.add(button, i % 10, i / 10);
		}

		return grid;
	}

	public static void main(String[] args) {
		launch(args.clone());

	}
	public void closeProgram() {
		Boolean an = ConfirmBox("Close?", "");
		if(an) {
			primaryStage.close();
		}
	}
	public static void display(String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);

		Label label = new Label();
		label.setText(message);
		String cl;
		if(gameOver) {
			cl = "Close Game";
		}
		else {
			cl = "Close Window";
		}
		Button closeButton = new Button(cl);
		closeButton.setOnAction(e -> {
			window.close();
			if(gameOver) {
				primaryStage.close();
			}
		});

		VBox lay = new VBox(10);
		lay.getChildren().addAll(label, closeButton);
		lay.setAlignment(Pos.CENTER);

		Scene sce = new Scene(lay);
		window.setScene(sce);
		window.showAndWait();


	}
	public boolean ConfirmBox(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		Label label = new Label();
		label.setText(message);

		Button yes = new Button("Yes");
		Button no = new Button("No");

		yes.setOnAction(e -> {
			answ = true;
			window.close();
		});

		no.setOnAction(e -> {
			answ = false;
			window.close();
		});

		VBox lay = new VBox(10);
		lay.getChildren().addAll(label, yes, no);
		lay.setAlignment(Pos.CENTER);
		Scene sce = new Scene(lay);
		window.setScene(sce);
		window.showAndWait();

		return answ;
	}

}