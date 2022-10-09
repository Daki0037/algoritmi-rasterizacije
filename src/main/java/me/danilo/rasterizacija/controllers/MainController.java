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
	
	private double xOffset = 0, yOffset = 0;
	
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
	}
	
	public void onSeedFillBtn(ActionEvent e) {
		try {
			Node node = (Node) e.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/SeedFill.fxml"));
			Scene scene = new Scene(root);
			
			stage.hide();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void onScanlineBtn(ActionEvent e) {
		try {
			Node node = (Node) e.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scanline.fxml"));
			Scene scene = new Scene(root);
			
			stage.hide();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void onScanConversionBtn(ActionEvent e) {
		try {
			Node node = (Node) e.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/ScanConversion.fxml"));
			Scene scene = new Scene(root);
			
			stage.hide();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void onTriangulation(ActionEvent e) {
		try {
			Node node = (Node) e.getSource();
			Stage stage = (Stage) node.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/Triangulation.fxml"));
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
