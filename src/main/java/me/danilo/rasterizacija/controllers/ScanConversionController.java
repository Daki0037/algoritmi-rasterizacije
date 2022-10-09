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
	public double xOffset = 0, yOffset = 0;
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
				cells[i][j] = cell;
				gridPane.getChildren().add(cell.getRectangle());
			}
		}
		
		numberTriangles.setItems(list);
	}
	
	public void onGenerateBtn(ActionEvent e) {
		int max = 500, min = 50, radiusMin = 50, radiusMax = 145, offsetMax = 100, angleMin = 60, angleMax = 120;
		int posNeg[] = {1, -1};
		
		//Cistimo sve celije
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				cells[i][j].clearCell();
			}
		}
		
		for(Polygon p : polygons) {
			if(gridPane.getChildren().contains(p))
				gridPane.getChildren().remove(p);
		}
		
		polygons.clear();
		usedColors.clear();
		polyColor.clear();
		try {
			triangleNumbers = (int) numberTriangles.getValue();
		} catch(Exception e1) {
			errorMessage.setStyle("-fx-opacity: 1.0;");
			return;
		}
		
		errorMessage.setStyle("-fx-opacity: 0.0;");
		int counter = 0, angle = 0;
		
		for(int i = 0; i < triangleNumbers; i++) {
			
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
