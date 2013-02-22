package com.baselet.client;

import java.util.ArrayList;
import java.util.List;

import com.baselet.client.MouseDragUtils.MouseDragHandler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;

public class DrawPanelCanvas {
	
	public final static int GRID_SIZE = 10;

	CssColor red = CssColor.make("rgba(" + 255 + ", " + 0 + "," + 0 + ", " + 1.0 + ")");
	CssColor gray = CssColor.make("rgba(" + 100 + ", " + 100 + "," + 100 + ", " + 0.2 + ")");
	
	List<GridElement> gridElements = new ArrayList<GridElement>();
	
	Canvas elementCanvas;
	
	Canvas backgroundCanvas;
	
	public DrawPanelCanvas() {
		gridElements.add(new GridElement(new Rectangle(10, 10, 30, 30)));
		gridElements.add(new GridElement(new Rectangle(50, 10, 30, 30)));
		gridElements.add(new GridElement(new Rectangle(50, 50, 30, 30)));
	}
	
	Canvas makeCanvas(int width, int height) {
		elementCanvas = initCanvas(width, height);
		backgroundCanvas = initCanvas(width, height);

		MouseDragUtils.addMouseDragHandler(this, new MouseDragHandler() {
			@Override
			public void onMouseDrag(int diffX, int diffY, GridElement gridElement) {
				if (gridElement == null) { // nothing selected -> move whole diagram
					for (GridElement ge : gridElements) {
						ge.getBounds().move(diffX, diffY);
					}
				} else {
					gridElement.getBounds().move(diffX, diffY);
				}
				draw();
			}
		});

//		canvas.addMouseWheelHandler(new MouseWheelHandler() {
//			@Override
//			public void onMouseWheel(MouseWheelEvent event) {
//				float zoom;
//				if(event.getDeltaY() < 0) { // scrolling up = negative values, scrolling down = positive values
//					zoom = 1.1f;
//				} else {
//					zoom = 0.9f;
//				}
//				context.scale(zoom,zoom);
//			}
//		});

		drawBackgroundGrid(width, height);
		draw();
		return elementCanvas;
	}

	private void drawBackgroundGrid(int width, int height) {
		Context2d backgroundContext = backgroundCanvas.getContext2d();
		backgroundContext.setStrokeStyle(gray);
		for (int i = 0; i < width; i += GRID_SIZE) {
			ContextUtils.drawLine(backgroundContext, i, 0, i, height);
		}
		for (int i = 0; i < height; i += GRID_SIZE) {
			ContextUtils.drawLine(backgroundContext, 0, i, width, i);
		}
	}

	private Canvas initCanvas(int width, int height) {
		Canvas canvas = Canvas.createIfSupported();
		canvas.setStyleName("canvas");
		//		canvas.setWidth(width + "px");
		canvas.setCoordinateSpaceWidth(width);
		//		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceHeight(height);
		return canvas;
	}

	private void draw() {
		Context2d context = elementCanvas.getContext2d();
		context.clearRect(-1000000, -1000000, 2000000, 2000000);
		for (GridElement ge : gridElements) {
			context.setFillStyle(red);
			context.fillRect(ge.getBounds().getX(), ge.getBounds().getY(), ge.getBounds().getWidth(), ge.getBounds().getHeight());
		}
		context.fill();
		context.drawImage(backgroundCanvas.getCanvasElement(), 0, 0);
	}
	
	public Canvas getCanvas() {
		return elementCanvas;
	}
	
	public GridElement getGridElementOnPosition(int x, int y) {
		for (GridElement ge : gridElements) {
			if (ge.contains(x, y)) return ge;
		}
		return null;
	}
}
