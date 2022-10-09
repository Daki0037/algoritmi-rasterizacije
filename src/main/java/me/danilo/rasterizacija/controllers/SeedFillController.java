package me.danilo.rasterizacija.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SeedFillController {
	
	@FXML
	private AnchorPane ap;
	@FXML
	private Pane gridPane;
	@FXML
	private Slider canvasNumber;
	@FXML
	private Pane selectedColor;
	@FXML
	private Pane titleBar;
	
	
	private int WIDTH = 500, HEIGHT = 500, rows = 20, collumns = 20;
	int sizeX = WIDTH / rows, sizeY = HEIGHT / collumns;
	private Color color, currentColor;
	private Cell cells[][] = new Cell[rows][collumns];
	boolean holdingClick = false;
	public double xOffset = 0, yOffset = 0;
	
	//Inicijalizacija gridPane mreze celija
	@FXML
	public void initialize() {
		
		titleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
		
		titleBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ap.getScene().getWindow().setX(event.getScreenX() - xOffset);
                ap.getScene().getWindow().setY(event.getScreenY() - yOffset);
            }
        });
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				Cell cell = new Cell(i*sizeX, j*sizeY, sizeX, sizeY);
				cell.listenerSetup();
				cell.setController(this);
				cells[i][j] = cell;
				cells[i][j].setX(i);
				cells[i][j].setY(j);
				gridPane.getChildren().add(cell.getRectangle());
			}
		}
		
		gridPane.setOnDragDetected(new EventHandler<Event>() {

			@Override
			public void handle(Event e) {
				gridPane.startFullDrag();
			}
			
		});
		
	}
	
	public void ukloniBoju(ActionEvent e) {
		setActive(false);
		selectedColor.setStyle("-fx-background-color: black");
	}
	
	//Nakon svakog klika u celiji, ova funkcija ce biti pozvana
	public void afterClick() {
		
		int globalX, globalY;
		
		globalX = Cell.getGlobalX();
		globalY = Cell.getGlobalY();
		currentColor = cells[globalX][globalY].getColor();
		if(currentColor == color)
			return;
		seedFill(globalX, globalY);
	}
	
	//Funkcija za postavljanje celija u aktivno stanje, koje ce omoguciti bojenje putem SeedFill
	public void setActive(boolean active) {
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				cells[i][j].setActive(active);
			}
		}
		
	}
	
	//SeedFill algoritam za bojenje celija
	public void seedFill(int x, int y) {
		if(x > collumns-1 || y > rows-1 || x < 0 || y < 0)
			return;
		if(cells[x][y].getColor() != currentColor)
			return;
		
		cells[x][y].setColor(color);
		seedFill(x+1, y);
		seedFill(x-1, y);
		seedFill(x, y+1);
		seedFill(x, y-1);
	}
	
	//Dugme za selektovanje boja
	public void onRedButton(ActionEvent e) {
		color = Color.RED;
		selectedColor.setStyle("-fx-background-color: red");
		setActive(true);
	}
	
	public void onGreenButton(ActionEvent e) {
		color = Color.GREEN;
		selectedColor.setStyle("-fx-background-color: green");
		setActive(true);
	}
	
	public void onBlueButton(ActionEvent e) {
		color = Color.BLUE;
		selectedColor.setStyle("-fx-background-color: blue");
		setActive(true);
	}
	
	public void onYellowButton(ActionEvent e) {
		color = Color.YELLOW;
		selectedColor.setStyle("-fx-background-color: yellow");
		setActive(true);
	}
	
	public void onPurpleButton(ActionEvent e) {
		color = Color.PURPLE;
		selectedColor.setStyle("-fx-background-color: purple");
		setActive(true);
	}
	
	public void onOrangeButton(ActionEvent e) {
		color = Color.ORANGE;
		selectedColor.setStyle("-fx-background-color: orange");
		setActive(true);
	}
	
	public void onBlackButton(ActionEvent e) {
		color = Color.BLACK;
		selectedColor.setStyle("-fx-background-color: black");
		setActive(true);
	}
	
	public void onWhiteButton(ActionEvent e) {
		color = Color.WHITE;
		selectedColor.setStyle("-fx-background-color: #DADCE0");
		setActive(true);
	}
	
	//Funkcija za brisanje canvasa
	public void clearCanvas(ActionEvent e) {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				cells[i][j].clearCell();
				color = color.WHITE;
			}
		}
		selectedColor.setStyle("-fx-background-color: black");
	}
	
	//Podesavanje broja redova i kolona
	public void onCanvasSetup(ActionEvent e) {
		int sliderValue = (int) canvasNumber.getValue();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				cells = null;
			}
		}
		
		gridPane.getChildren().clear();
		
		switch(sliderValue) {
		case 0:
			rows = 10;
			collumns = 10;
			break;
		case 25:
			rows = 20;
			collumns = 20;
			break;
		case 50:
			rows = 50;
			collumns = 50;
			break;
		}
		
		cells = new Cell[rows][collumns];
		sizeX = WIDTH / rows;
		sizeY = HEIGHT / collumns;
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				Cell cell = new Cell(i*sizeX, j*sizeY, sizeX, sizeY);
				cell.listenerSetup();
				cell.setController(this);
				cells[i][j] = cell;
				cells[i][j].setX(i);
				cells[i][j].setY(j);
				gridPane.getChildren().add(cell.getRectangle());
			}
		}
		selectedColor.setStyle("-fx-background-color: black");
		
	}
	
	public void onReturnButton(ActionEvent e) {
		try {
			Node node = (Node) e.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
			Scene scene = new Scene(root);
			
			stage.hide();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void onExitButton(ActionEvent e) {
		System.exit(0);
	}
}
