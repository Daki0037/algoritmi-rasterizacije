package me.danilo.rasterizacija.controllers;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
	
	public Rectangle rectangle;
	boolean clicked = false;
	private Color color = Color.BLACK;
	private boolean active = false;
	private static int globalX, globalY;
	public int X, Y;
	private SeedFillController controller;
	
	public Cell(int x, int y, int width, int height) {
		rectangle = new Rectangle(x, y, width, height);
		rectangle.setFill(Color.web("#DADCE0"));
		rectangle.setStroke(Color.web("#35363A"));
		color = Color.WHITE;
		this.X = x;
		this.Y = y;
	}
	
	public void listenerSetup() {
		
		rectangle.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				//Ukoliko je celija aktivna, poziva se SeedFill algoritam i ne izvrsava se klasicno bojenje celija
				if(active) {
					globalX = X;
					globalY = Y;
					controller.afterClick();
					return;
				}
				
				//Ukoliko nije selektovana boja, poziva se klasicno na klik, gde se boji celija datom bojom
				if(!clicked) {
					clicked = true;
					color = Color.BLACK;
					rectangle.setFill(color);
					return;
				}
				clicked = false;
				rectangle.setFill(Color.web("#DADCE0"));
				color = Color.WHITE;
			}
			
		});
		
		rectangle.setOnMouseDragEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event e) {
				if(active) {
					globalX = X;
					globalY = Y;
					controller.afterClick();
					return;
				}
				clicked = true;
				color = Color.BLACK;
				rectangle.setFill(color);
			}
			
		});
		
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
	
	//Funkcije za globalX i globalY, koje se dobijaju klikom na celiju pri aktivnom stanju celije
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
	
	//Funckija brise boju celije i gasi aktivno stanje celije
	public void clearCell() {
		active = false;
		clicked = false;
		color = Color.WHITE;
		rectangle.setFill(Color.web("#DADCE0"));
	}
	
}
