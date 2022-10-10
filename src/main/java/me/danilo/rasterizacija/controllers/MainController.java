package me.danilo.rasterizacija.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainController {
	
	@FXML
	private AnchorPane ap;
	@FXML
	private Pane titleBar;
	
	private double xCoordinateOfScene = 0, yCoordinateOfScene = 0;
	private double xCoordinateOfScreen = 0, yCoordinateOfScreen = 0;

	private Stage stage;
	private Node node;
	private Scene scene;

	private String fxmlLocation;
	
	@FXML
	public void initialize() {
		
		titleBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setSceneCoordinates(event);
            }
        });
		
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
	
	public void onSeedFillBtn(ActionEvent e) {
		try {
			loadScreen("/fxml/SeedFill.fxml", e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void onScanlineBtn(ActionEvent e) {
		try {
			loadScreen("/fxml/Scanline.fxml", e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void onScanConversionBtn(ActionEvent e) {
		try {
			loadScreen("/fxml/ScanConversion.fxml", e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void onTriangulation(ActionEvent e) {
		try {
			loadScreen("/fxml/Triangulation.fxml", e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void loadScreen(String fxmlLocation, ActionEvent e) throws IOException {
		this.fxmlLocation = fxmlLocation;
		initializateNodeAndStage(e);
		loadAndSetViewToScene();
		changeSceneOfStage(scene);
	}

	public void initializateNodeAndStage(ActionEvent e) {
		this.node = (Node) e.getSource();
		this.stage = (Stage) node.getScene().getWindow();
	}

	public void loadAndSetViewToScene() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(fxmlLocation));
		this.scene = new Scene(root);
	}

	public void changeSceneOfStage(Scene scene) {
		stage.hide();
		stage.setScene(scene);
		stage.show();
	}
	
	public void onExitButton(ActionEvent e) {
		System.exit(0);
	}
	
}
