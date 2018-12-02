
import javax.script.Bindings;
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
	private Stage primaryStage;
	private boolean answ;
	private Grid playerGrid;
	public Button[][] grid1;
	public Button[][] grid2;
	
	//private Grid playerGrid = new Grid();
	private Grid aiGrid;
	private Grid playerView;

	@Override
	public void start(Stage ps) {
		grid1 = new Button[10][10];
		grid2 = new Button[10][10];
		
		playerGrid = new Grid();
		aiGrid = new Grid();
		
		aiGrid.randomSet();
		
		primaryStage = ps;
		GridPane g1 = new GridPane();
		g1 = getP1();
		GridPane g2 = new GridPane();
		g2 = getP2();


		Label l = new Label("\n");

		Label atB = new Label("Attack Board");
		atB.setTextFill(Color.WHITE);
		Label ownB = new Label("Your Own Board");
		ownB.setTextFill(Color.WHITE);
		VBox  v = new VBox(atB, g1, l,  ownB, g2);
		v.setPadding(new Insets(10, 20, 10, 20));
		v.backgroundProperty().set(new Background(new BackgroundFill(Color.NAVY, CornerRadii.EMPTY, Insets.EMPTY)));
		v.setMinSize(350, 620);
		v.setMaxSize(350, 620);
		Scene scene = new Scene(v);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
	}

	private Button createButton(String text, int x, int y) {
		Button button = new Button("");
		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		button.setStyle("-fx-background-color: #0080ff");
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				MouseButton btn = event.getButton();
				if(btn==MouseButton.PRIMARY){
					if(button.getStyle() == "-fx-background-color: #FFFFFF" || button.getStyle() == "-fx-background-color: #FF0000") {
						display("Already Hit", "This tile has already been hit, please select again");
					}
					else {
						if(aiGrid.attack(x, y)) {
							button.setStyle("-fx-background-color: #FF0000");
						}
						else {
							button.setStyle("-fx-background-color: #FFFFFF");
						}
						if(aiGrid.allShipsSunk()) {
							// TODO: End game and print victory message.
						}
						 // playerGrid.aiAttack();
						if(playerGrid.allShipsSunk()) {
							// TODO: End game and print loss message.
						}
						
					}
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
				boolean placed = false;
				Ship nS = playerGrid.nextShip();
				MouseButton btn = event.getButton();
				if(btn==MouseButton.PRIMARY){
					if(!nS.isPlaced() && !nS.isLast()) {
						try{
							System.out.println("placing horiz at: " + nS + " " + x + " " + y);
							playerGrid.placeShip(nS, x, y, false);
							placed = true;
							nS.Placed();
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
						display("All placed", "All Ships have been placed");
					for(int i = 0; i < 10; i++) {
						for(int j = 0; j < 10; j++) {
							grid2[i][j].setOnMouseClicked(e -> System.out.print("pie"));
						}
					}
				}
			}
			else if(btn == MouseButton.SECONDARY){
				if(!nS.isPlaced() && !nS.isLast()) {
					try{
						System.out.println("placing vert at: " + nS + " " + x + " " + y);
						playerGrid.placeShip(nS, x, y, true);
						placed = true;
						nS.Placed();
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
					for(int i = 0; i < 10; i++) {
						for(int j = 0; j < 10; j++) {
							grid2[i][j].setOnMouseClicked(e -> System.out.print("pie"));
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
	launch(args);

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
	Button closeButton = new Button("close");
	closeButton.setOnAction(e -> window.close());

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
