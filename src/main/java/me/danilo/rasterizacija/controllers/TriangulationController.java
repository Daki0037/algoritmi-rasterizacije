package me.danilo.rasterizacija.controllers;

import java.io.IOException;
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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class TriangulationController {
	
	@FXML
	private AnchorPane ap;
	@FXML
	private Pane gridPane;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private ComboBox numPoints;
	@FXML
	private Pane titleBar;
	
	Random random = new Random();
	public double points[], toRadians = Math.PI/180,
			xOffset = 0, yOffset = 0;
	Polygon poly;
	LinkedList<Line> lines = new LinkedList<Line>();
	
	ObservableList<Integer> list = FXCollections.observableArrayList(3, 4, 5, 6, 7);
	
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
		
		numPoints.setItems(list);
	}
	
	public void onGenerate(ActionEvent e) {
		int centerX = 250, centerY = 250, angle = 0, currentAngle, radius;
		int radiusMax = 200, radiusMin = 50, pointNum;
		
		Color color = colorPicker.getValue();
		
		try {
			pointNum = (int) numPoints.getValue();
		} catch(Exception e1) {
			numPoints.setStyle("-fx-border-color: red;");
			numPoints.requestFocus();
			return;
		}
		
		int angleMax = 360/pointNum, angleMin = angleMax - (angleMax/5);
		
		if(gridPane.getChildren().contains(poly))
			gridPane.getChildren().clear();
		
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
		poly.setFill(color);
		poly.setStroke(Color.BLACK);
		if(color == Color.BLACK)
			poly.setStroke(Color.WHITE);
		gridPane.getChildren().add(poly);
		
	}
	
	public Point[] toPoints() {
		
		Point[] list = new Point[points.length/2];
		int counter = 0, listCounter = 0;
		
		for(int i = 0; i < points.length/2; i++) {
			double x = points[counter++];
			double y = points[counter++];
			Point point = new Point(x, y);
			list[listCounter++] = point;
		}
		
		return list;
	}
	
	public void onTriangulate(ActionEvent e) {
		
		if(!gridPane.getChildren().contains(poly))
			return;
		
		numPoints.setStyle("-fx-border: none;");
		
		for(Line l : lines) {
			if(gridPane.getChildren().contains(l))
				gridPane.getChildren().remove(l);
		}
		
		lines.clear();
		
		Point[] list = toPoints();
		Point center = new Point(250, 250);
		double distance = Double.MAX_VALUE;
		Point min = null;
		int greaterIndex = 0, lowerIndex = 0, index = 0;
		
		for(int i = 0; i < list.length; i++) {
			if(center.getDistanceTo(list[i]) < distance) {
				distance = center.getDistanceTo(list[i]);
				min = list[i];
				index = i;
			}
		}
		
		greaterIndex = index + 1;
		if(greaterIndex >= list.length)
			greaterIndex = 0;
		lowerIndex = index-1;
		if(lowerIndex < 0)
			lowerIndex = list.length-1;
		
		for(int i = 0; i < list.length; i++) {
			if(i == lowerIndex || i == greaterIndex)
				continue;
			
			Line line = new Line(min.x, min.y, list[i].x, list[i].y);
			lines.add(line);
			gridPane.getChildren().add(line);
		}
		
	}
	
	public void onReturnBtn(ActionEvent e) {
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
	
	class Point {
		double x, y;
		
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public double getDistanceTo(Point p) {
			return Math.sqrt(Math.pow((x - p.x), 2) + Math.pow((y - p.y), 2));
		}

		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + "]";
		}
		
		
	}
	
}
