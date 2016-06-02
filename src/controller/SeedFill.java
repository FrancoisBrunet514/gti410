package controller;

import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import model.ImageX;
import model.Pixel;
import model.Shape;
import utils.HSVConverter;

public class SeedFill extends AbstractTransformer {
	private ImageX currentImage;
	private Pixel fillColor = new Pixel(0xFF00FFFF);
	private Pixel borderColor = new Pixel(0xFFFFFF00);
	private boolean floodFill = true;
	private int hueThreshold = 1;
	private int saturationThreshold = 2;
	private int valueThreshold = 3;
	private int red;
	private int green;
	private int blue;
	
	/**
	 * Creates an ImageLineFiller with default parameters.
	 * Default pixel change color is black.
	 */
	public SeedFill() {
	}

	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_FLOODER; } 
	
	protected boolean mouseClicked(MouseEvent e) {
		List intersectedObjects = Selector.getDocumentObjectsAtLocation(e.getPoint());
		if (!intersectedObjects.isEmpty()) {
			Shape shape = (Shape)intersectedObjects.get(0);
			if (shape instanceof ImageX) {
				currentImage = (ImageX)shape;

				Point pt = e.getPoint();
				Point ptTransformed = new Point();
				try {
					shape.inverseTransformPoint(pt, ptTransformed);
				} catch (NoninvertibleTransformException e1) {
					e1.printStackTrace();
					return false;
				}
				ptTransformed.translate(-currentImage.getPosition().x, -currentImage.getPosition().y);
				if (0 <= ptTransformed.x && ptTransformed.x < currentImage.getImageWidth() &&
				    0 <= ptTransformed.y && ptTransformed.y < currentImage.getImageHeight()) {
					currentImage.beginPixelUpdate();
				
					if(isFloodFill()) {
						floodFill(ptTransformed);
					} else {
						boundaryFill(ptTransformed);
					}
					
					currentImage.endPixelUpdate();											 	
					return true;
				}
			}
			
			
		}
		
		return false;
	}
	
	private void floodFill(Point ptClicked) {
		System.out.println("Flood fill started");
		
		HSVConverter converter = new HSVConverter((double)this.hueThreshold, (double)this.saturationThreshold, (double)this.valueThreshold );
		Pixel pxClicked = new Pixel();
		pxClicked.setRed(converter.r);
		pxClicked.setGreen(converter.g);
		pxClicked.setBlue(converter.b);
		
		floodFill4(ptClicked.x, ptClicked.y, pxClicked, fillColor);
		
		System.out.println("Flood fill finished");
	}
	
	private void floodFill4(int x, int y, Pixel targetColor, Pixel replacementColor) {
		if (targetColor.equals(replacementColor)) return;
		if (!targetColor.equals(currentImage.getPixel(x, y))) return;
		currentImage.setPixel(x, y, replacementColor);
		floodFill4(x-1,y,targetColor,replacementColor);
		floodFill4(x+1,y,targetColor,replacementColor);
		floodFill4(x,y-1,targetColor,replacementColor);
		floodFill4(x,y+1,targetColor,replacementColor);
	}
	
	private void boundaryFill(Point ptClicked) {
		Stack<Point> stack = new Stack<Point>();
		stack.push(ptClicked);
		hsvConverter = new HSVConverter(this.getHueThreshold(), this.getSaturationThreshold(), this.getValueThreshold());
		Pixel thresholdColor = new Pixel(hsvConverter.r, hsvConverter.g, hsvConverter.b);
		while (!stack.empty()) {
			Point current = (Point)stack.pop();
			if (0 <= current.x && current.x < currentImage.getImageWidth() &&
				0 <= current.y && current.y < currentImage.getImageHeight() &&
				!currentImage.getPixel(current.x, current.y).equals(borderColor) &&
				!currentImage.getPixel(current.x, current.y).equals(thresholdColor)) {
				currentImage.setPixel(current.x, current.y, borderColor);
				
				// Next points to fill.
				Point nextLeft = new Point(current.x-1, current.y);
				Point nextRight = new Point(current.x+1, current.y);
				Point nextUp = new Point(current.x, current.y+1);
				Point nextDown = new Point(current.x, current.y-1);
				stack.push(nextLeft);
				stack.push(nextRight);
				stack.push(nextUp);
				stack.push(nextDown);
			}
		}
	}
	
	/**
	 * @return
	 */
	public Pixel getBorderColor() {
		return borderColor;
	}

	/**
	 * @return
	 */
	public Pixel getFillColor() {
		return fillColor;
	}
	
	/**
	 * @param pixel
	 */
	public void setBorderColor(Pixel pixel) {
		borderColor = pixel;
		System.out.println("new border color");
	}

	/**
	 * @param pixel
	 */
	public void setFillColor(Pixel pixel) {
		fillColor = pixel;
		System.out.println("new fill color");
	}
	
	/**
	 * @return true if the filling algorithm is set to Flood Fill, false if it is set to Boundary Fill.
	 */
	public boolean isFloodFill() {
		return floodFill;
	}

	/**
	 * @param b set to true to enable Flood Fill and to false to enable Boundary Fill.
	 */
	public void setFloodFill(boolean b) {
		floodFill = b;
	}
	
	/**
	 * @return
	 */
	public int getHueThreshold() {
		return hueThreshold;
	}

	/**
	 * @return
	 */
	public int getSaturationThreshold() {
		return saturationThreshold;
	}

	/**
	 * @return
	 */
	public int getValueThreshold() {
		return valueThreshold;
	}

	/**
	 * @param i
	 */
	public void setHueThreshold(int i) {
		hueThreshold = i;
		System.out.println("new Hue Threshold " + i);
	}

	/**
	 * @param i
	 */
	public void setSaturationThreshold(int i) {
		saturationThreshold = i;
		System.out.println("new Saturation Threshold " + i);
	}

	/**
	 * @param i
	 */
	public void setValueThreshold(int i) {
		valueThreshold = i;
		System.out.println("new Value Threshold " + i);
	}

}
