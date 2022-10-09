package me.danilo.rasterizacija.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class ScanlineController {

	@FXML
	private AnchorPane ap;
	@FXML
	private Pane gridPane;
	@FXML
	private Slider canvasNumber;
	@FXML
	private Label errorLabel;
	@FXML
	private Pane selectedColor;
	@FXML
	private Pane titleBar;
	
	Random random = new Random();
	public Polygon poly;
	
	public final int WIDTH = 500, HEIGHT = 500;
	public int rows = 20, collumns = 20, time = 1000,
			sizeX = WIDTH / collumns, sizeY = HEIGHT / rows;
	public Cell cells[][] = new Cell[rows][collumns];
	double toRadians = Math.PI/ 180.0;
	public double points[], xOffset = 0, yOffset = 0;
	boolean running = false;
	public Color color = Color.RED;
	public Thread thread;
	LinkedList<Line> lines = new LinkedList<Line>();
	
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
		
	}
	
	//Pri pokretanju Scanline algoritma
	public void onStartButton(ActionEvent e) {
		
		if(!running) {
			running = true;
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					
					for(int j = collumns-1; j >= 0; j--) {
						for(int i = 0; i < rows; i++) {
							if(poly != null) {
								if(cells[i][j].getColor() == Color.BLACK)
									continue;
								if(checkIntersection(poly, cells[i][j])) {
									cells[i][j].setColor(color);
									try {
										Thread.sleep(10);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									continue;
								}
								
							}
							cells[i][j].setColor(Color.YELLOW);
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							cells[i][j].setColor(Color.WHITE);
						}
					}
					
					running = false;
					
				}
			});
			thread.setDaemon(true);
			thread.start();
			return;
		}
		
		System.out.println("Jos uvek radi");
		
	}
	
	//Ova funkcija proverava da li postoji presek izmedju poligona i celije u gridu
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
	
	
	//Funkcija za ciscenje kanvasa
	public void onClearCanvas(ActionEvent e) {
		if(poly != null)
			gridPane.getChildren().remove(poly);
		
		poly = null;
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				cells[i][j].clearCell();
			}
		}
	}
	
	//Novi algoritam
	public void onGenerateBtn(ActionEvent e) {
		
		if(running) {
			errorLabel.setStyle("-fx-opacity: 1.0");
			return;
		}
		
		errorLabel.setStyle("-fx-opacity: 0.0");
		
		int pointMin = 3, pointMax = 7, pointNum = 0, radiusMax = 180, radiusMin = 50;
		int centerX = 250, centerY = 250, angle = 0, currentAngle, radius;
		
		//Brisanje canvasa pre crtanje poligona
		if(poly != null)
			gridPane.getChildren().remove(poly);
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				cells[i][j].clearCell();
			}
		}
		
		
		if(gridPane.getChildren().contains(poly))
			gridPane.getChildren().remove(poly);
		
		pointNum = random.nextInt((pointMax-pointMin)+1)+pointMin;
		
		int angleMax = 360/pointNum, angleMin = angleMax - (angleMax/5);
		
		points = new double[2*pointNum];
		
		int counter = 0;
		
		for(int i = 0; i < pointNum; i++) {
			currentAngle = random.nextInt((angleMax-angleMin)+1)+angleMin;
			radius = random.nextInt((radiusMax-radiusMin)+1)+radiusMin;
			angle += currentAngle;
			double x = centerX + radius * Math.cos(angle*toRadians);
			double y = centerY + radius * Math.sin(angle*toRadians);
			points[counter++] = x;
			points[counter++] = y;
		}
		
		poly = new Polygon(points);
		poly.setStroke(Color.BLACK);
		poly.setFill(Color.TRANSPARENT);
		
		//Algoritam za bojenje ivica u crnu boju
		
		paintEdges();
		
	}
	
	public void paintEdges() {
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < collumns; j++) {
				if(checkIntersection(poly, cells[i][j])) {
					if(!checkIntersection(poly, cells[i][j+1]))
						cells[i][j].setColor(Color.BLACK);
					else if(!checkIntersection(poly, cells[i][j-1]))
						cells[i][j].setColor(Color.BLACK);
					else if(!checkIntersection(poly, cells[i-1][j]))
						cells[i][j].setColor(Color.BLACK);
					else if(!checkIntersection(poly, cells[i+1][j]))
						cells[i][j].setColor(Color.BLACK);
				}
			}
		}
		
	}
	
	public void onCanvasSetup(ActionEvent e) {
		
		if(running)
			return;
		
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
				cells[i][j] = cell;
				cells[i][j].setX(i*sizeX);
				cells[i][j].setY(j*sizeY);
				gridPane.getChildren().add(cell.getRectangle());
			}
		}
		
	}
	
	public void onRedButton(ActionEvent e) {
		if(!running) {
			color = Color.RED;
			selectedColor.setStyle("-fx-background-color: red");
		}
	}
	
	public void onBlueButton(ActionEvent e) {
		if(!running) {
			color = Color.BLUE;
			selectedColor.setStyle("-fx-background-color: blue");
		}
	}
	
	public void onGreenButton(ActionEvent e) {
		if(!running) {
			color = Color.GREEN;
			selectedColor.setStyle("-fx-background-color: green");
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
