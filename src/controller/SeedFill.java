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
	private HSVConverter hsvConverter;
	
	/**
	 * Creates an ImageLineFiller with default parameters.
	 * Default pixel change color is black.
	 */
	public SeedFill() {}

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
		
		Pixel initialPtColor = currentImage.getPixel(ptClicked.x, ptClicked.y);

        Stack<Point> stack = new Stack<Point>();
        stack.push(ptClicked);
        while (!stack.empty()) {
            Point current = (Point)stack.pop();
            if (0 <= current.x && current.x < currentImage.getImageWidth()
                    && 0 <= current.y && current.y < currentImage.getImageHeight()
                    && !currentImage.getPixel(current.x, current.y).equals(fillColor)
                    && currentPointNeedsNewColor(current, initialPtColor)) {

                currentImage.setPixel(current.x, current.y, fillColor);

                // Next points to fill.
                Point west = new Point(current.x-1, current.y);
                Point east = new Point(current.x+1, current.y);
                Point north = new Point(current.x, current.y - 1);
                Point south = new Point(current.x, current.y + 1);

                // Add the points to the stack
                stack.push(west);
                stack.push(east);
                stack.push(north);
                stack.push(south);
            }
        }
		
		System.out.println("Flood fill finished");
	}
	
	private void boundaryFill(Point ptClicked) {
		Stack<Point> stack = new Stack<Point>();
		stack.push(ptClicked);
		while (!stack.empty()) {
			Point current = (Point)stack.pop();
			if (0 <= current.x && current.x < currentImage.getImageWidth() &&
				0 <= current.y && current.y < currentImage.getImageHeight() &&
				!currentImage.getPixel(current.x, current.y).equals(fillColor) &&
				currentPointNeedsNewColor(current, borderColor)) {
				
				currentImage.setPixel(current.x, current.y, fillColor);
				
				// Next points to fill.
				Point nextWest = new Point(current.x-1, current.y);
				Point nextEast = new Point(current.x+1, current.y);
				Point nextNorth = new Point(current.x, current.y+1);
				Point nextSouth = new Point(current.x, current.y-1);
				stack.push(nextWest);
				stack.push(nextEast);
				stack.push(nextNorth);
				stack.push(nextSouth);
			}
		}
	}

    private boolean currentPointNeedsNewColor(Point currentPt, Pixel regionColor){
        Pixel currentPx = this.currentImage.getPixel(currentPt.x, currentPt.y);

        hsvConverter = new HSVConverter(currentPx.getRed(), currentPx.getGreen(), currentPx.getBlue());

        double hueATester = hsvConverter.h;
        double saturationATester = hsvConverter.s;
        double valueATester = hsvConverter.v;
        
        hsvConverter = new HSVConverter(regionColor.getRed(), regionColor.getGreen(), regionColor.getBlue());

        double hueBoundary = hsvConverter.h;
        double saturationBoundary = hsvConverter.s;
        double valueBoundary = hsvConverter.v;

        if((hueBoundary - hueThreshold) <= hueATester && hueATester <= (hueBoundary + hueThreshold)&&
                (saturationBoundary - saturationThreshold) <= saturationATester && saturationATester <= (saturationBoundary + saturationThreshold)&&
                (valueBoundary - valueThreshold) <= valueATester && valueATester <= (valueBoundary + valueThreshold)){
            return this.isFloodFill();
        }
        return !this.isFloodFill();
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
