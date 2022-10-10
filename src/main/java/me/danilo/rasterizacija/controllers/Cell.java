package me.danilo.rasterizacija.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {

	private SeedFillController controller;

	public Rectangle rectangle;
	private Color color = Color.BLACK;

	boolean clicked = false;
	private boolean active = false;

	private static int globalX, globalY;
	public int X, Y;

	private int x, y;
	private int width, height;
	
	public Cell(int x, int y, int width, int height) {
		setParametersOfCell(x, y, width, height);
		initializeRectangle();
		setCoordinates(x, y);
	}

	public void setParametersOfCell(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void initializeRectangle() {
		rectangle = new Rectangle(x, y, width, height);
		setColorsOfRectangleAndCell();
	}

	public void setColorsOfRectangleAndCell() {
		rectangle.setFill(Color.web("#DADCE0"));
		rectangle.setStroke(Color.web("#35363A"));
		color = Color.WHITE;
	}

	public void setCoordinates(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	
	public void listenerSetup() {
		
		rectangle.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				startOnMouseClicked();
			}
			
		});
		
		rectangle.setOnMouseDragEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event e) {
				startOnMouseDragged();
			}
			
		});
		
	}

	public void startOnMouseClicked() {

		//Ukoliko je celija aktivna, poziva se SeedFill algoritam i ne izvrsava se klasicno bojenje celija
		if(active) {
			startSeedFillAfterClick();
			return;
		}

		if(!clicked) {
			setClickedCell();
			return;
		}
		resetCellToOriginalState();
	}

	public void startOnMouseDragged() {
		if(active) {
			startSeedFillAfterClick();
			return;
		}
		setClickedCell();
	}

	public void setClickedCell() {
		clicked = true;
		color = Color.BLACK;
		rectangle.setFill(color);
	}

	public void resetCellToOriginalState() {
		clicked = false;
		resetCellColor();
	}

	public void startSeedFillAfterClick() {
		setGlobalCoordinates();
		controller.afterClick();
	}


	public void setGlobalCoordinates() {
		globalX = X;
		globalY = Y;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public void setColor(Color color) {
		this.color = color;
		if(color == Color.WHITE) {
			rectangle.setFill(Color.web("#DADCE0"));
			return;
		}
		rectangle.setFill(color);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean getActive() {
		return active;
	}

	public static int getGlobalX() {
		return globalX;
	}
	
	public static int getGlobalY() {
		return globalY;
	}
	
	public void setController(SeedFillController controller) {
		this.controller = controller;
	}
	
	public int getX() {
		return X;
	}

	public void setX(int x) {
		this.X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		this.Y = y;
	}

	public void clearCell() {
		resetCellState();
		resetCellColor();
	}

	public void resetCellState() {
		active = false;
		clicked = false;
	}

	public void resetCellColor() {
		color = Color.WHITE;
		rectangle.setFill(Color.web("#DADCE0"));
	}


}
