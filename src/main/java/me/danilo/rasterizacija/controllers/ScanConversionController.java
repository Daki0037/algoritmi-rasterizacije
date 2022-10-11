package me.danilo.rasterizacija.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class ScanConversionController {
	
	@FXML
	public AnchorPane ap;
	@FXML
	public Pane gridPane;
	@FXML
	public ComboBox numberTriangles;
	@FXML
	public Label errorMessage;
	@FXML
	public Slider canvasNumber;
	@FXML
	public Pane titleBar;
	
	public final int WIDTH = 500, HEIGHT = 500;
	public int rows = 20, collumns = 20, time = 1000,
			sizeX = WIDTH / collumns, sizeY = HEIGHT / rows, triangleNumbers = 0;

	public double xCoordinateOfScene = 0, yCoordinateOfScene = 0;
	private double xCoordinateOfScreen = 0, yCoordinateOfScreen = 0;

	public Cell cells[][] = new Cell[rows][collumns];

	public Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.PURPLE, Color.AQUA, Color.LIME};

	Random random = new Random();
	double points[], toRadians = Math.PI / 180;

	LinkedList<Polygon> polygons = new LinkedList<Polygon>();
	HashMap<Polygon, Color> polyColor = new HashMap<Polygon, Color>();
	LinkedList<Color> usedColors = new LinkedList<Color>();
	
	ObservableList<Integer> list = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7);
	
	@FXML
	public void initialize() {
		initializeScreen();
		initializeCellsToScreen();
		
		numberTriangles.setItems(list);
	}

	public void initializeScreen() {
		onTitleBarPressed();
		onTitleBarDragged();
	}

	public void initializeCellsToScreen() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				createNewCell(i, j);
			}
		}
	}

	public void createNewCell(int i, int j) {
		Cell cell = new Cell(i*sizeX, j*sizeY, sizeX, sizeY);
		cells[i][j] = cell;
		gridPane.getChildren().add(cell.getRectangle());
	}

	public void onTitleBarPressed() {
		titleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setSceneCoordinates(event);
			}
		});
	}

	public void onTitleBarDragged() {
		titleBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setCurrentScreenCoordinates(event);
				moveScreenOnDragging();
			}
		});
	}

	public void setSceneCoordinates(MouseEvent event) {
		this.xCoordinateOfScene = event.getSceneX();
		this.yCoordinateOfScene = event.getSceneY();
	}

	public void setCurrentScreenCoordinates(MouseEvent event) {
		xCoordinateOfScreen = event.getScreenX();
		yCoordinateOfScreen = event.getScreenY();
	}

	public void moveScreenOnDragging() {
		ap.getScene().getWindow().setX(xCoordinateOfScreen - xCoordinateOfScene);
		ap.getScene().getWindow().setY(yCoordinateOfScreen - yCoordinateOfScene);
	}

	int max = 500, min = 50, radiusMin = 50, radiusMax = 145, offsetMax = 100, angleMin = 60, angleMax = 120;
	int posNeg[] = {1, -1};

	public void onGenerateBtn(ActionEvent e) {
		clearScreen();
		generateTriangles();
	}

	public void clearScreen() {
		clearAllCells();
		removePolygonsFromScreen();
	}

	public void generateTriangles() {
		setTriangleNumbers();
		for(int i = 0; i < triangleNumbers; i++) {
			generateTrianglesToScreen();
		}
	}

	public void generateTrianglesToScreen() {
		int counter = 0, angle = 0;

		int centerX = 250 + (random.nextInt(offsetMax)*posNeg[random.nextInt(posNeg.length)]), centerY = 250 + (random.nextInt(offsetMax)*posNeg[random.nextInt(posNeg.length)]);
		points = new double[6];

		counter = 0;
		angle = 0;

		for(int j = 0; j < 3; j++) {
			int radius = random.nextInt((radiusMax-radiusMin)+1)+radiusMin;
			double x = centerX + radius * Math.cos(angle*toRadians);
			double y = centerY + radius * Math.sin(angle*toRadians);
			angle += random.nextInt((angleMax-angleMin)+1)+angleMin;
			points[counter++] = x;
			points[counter++] = y;
		}
		Polygon p = new Polygon(points);
		Color randomC;
		do {
			randomC = colors[random.nextInt(colors.length)];
		} while(usedColors.contains(randomC));
		p.setFill(randomC);
		usedColors.add(randomC);
		polygons.add(p);
		polyColor.put(p, randomC);
		gridPane.getChildren().add(p);
	}

	public void setTriangleNumbers() {
		try {
			getTriangleNumbersFromList();
			hideErrorMessage();
		} catch(Exception e1) {
			showErrorMessage();
			return;
		}
	}

	public void getTriangleNumbersFromList() throws Exception {
		triangleNumbers = (int) numberTriangles.getValue();
	}

	public void clearAllCells() {
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < collumns; j++)
				cells[i][j].clearCell();
	}

	public void removePolygonsFromScreen() {
		for(Polygon p : polygons) {
			if(gridPane.getChildren().contains(p))
				gridPane.getChildren().remove(p);
		}
		polygons.clear();
		clearColorsFromArray();
	}

	public void clearColorsFromArray() {
		usedColors.clear();
		polyColor.clear();
	}

	public void showErrorMessage() {
		errorMessage.setStyle("-fx-opacity: 1.0;");
	}

	public void hideErrorMessage() {
		errorMessage.setStyle("-fx-opacity: 0.0;");
	}

	public void onScanConversion(ActionEvent e) {
		
		if(triangleNumbers == 0 || polygons.isEmpty()) {
			System.out.println("Nema trouglova.");
			return;
		}
		
		for(int k = 0; k < triangleNumbers; k++) {
			for(int i = 0; i < collumns; i++) {
				for(int j = 0; j < rows; j++) {
					Polygon poly = polygons.get(k);
					if(checkIntersection(poly, cells[i][j])) {
						cells[i][j].setColor(polyColor.get(poly));
						gridPane.getChildren().remove(poly);
					}
					
				}
			}
		}
		
	}
	
	public boolean checkIntersection(Polygon poly, Cell cell) {
		boolean intersects = false;
		
		for(int i = cell.getX(); i < cell.getX() + sizeX && !intersects; i += sizeX/10) {
			for(int j = cell.getY(); j < cell.getY() + sizeY && !intersects; j += sizeY/10) {
				if(poly.contains(i, j)) {
					intersects = true;
				}
			}
		}
		
		return intersects;
	}
	
	public void onClearCanvas(ActionEvent e) {
		
		for(int i = 0; i < polygons.size(); i++) {
			if(gridPane.getChildren().contains(polygons.get(i)))
				gridPane.getChildren().remove(polygons.get(i));
		}
		
		polygons.clear();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				cells[i][j].clearCell();
			}
		}
	}
	
	//Postavljanje broja celija
	public void onCanvasSetup(ActionEvent e) {
		
		int sliderValue = (int) canvasNumber.getValue();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				cells[i][j] = null;
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
				cells[i][j] = cell;
				cells[i][j].setX(i*sizeX);
				cells[i][j].setY(j*sizeY);
				gridPane.getChildren().add(cell.getRectangle());
			}
		}
		
	}
	
	public void onReturnButton(ActionEvent e) {
		try {
			returnToMenu(e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private Node node;
	private Stage stage;
	private Scene scene;

	public void returnToMenu(ActionEvent e) throws IOException {
		node = (Node) e.getSource();
		stage = (Stage) node.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Menu.fxml"));
		scene = new Scene(root);

		changeScene();
	}

	public void changeScene() {
		stage.hide();
		stage.setScene(scene);
		stage.show();
	}
	
	public void onExitButton(ActionEvent e) {
		System.exit(0);
	}
	
}
