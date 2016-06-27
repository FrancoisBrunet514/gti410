package controller;

import model.*;

/**
 * <p>Title: PaddingCirularStrategy</p>
 * <p>Description: Padding strategy where the opposite values are returned if Pixel values are out of range.</p>
 * <p>Copyright: Copyright (c) 2016 Day, Pierre-Alexandre</p>
 * <p>Company: ETS - École de Technologie Supérieure</p>
 * @author Day, Pierre-Alexandre
 */
public class PaddingCircularStrategy extends PaddingStrategy {
	/**
	 * Returns and validates the Pixel at the specified coordinate.
	 * If the Pixel is out of range, the opposite Pixel is returned.
	 * It's just like if the image was a sphere, linked from each side.
	 * @param image source Image
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the validated Pixel value at the specified coordinates 
	 */
	public Pixel pixelAt(ImageX image, int x, int y) {
		if ((x > 0) && (x < image.getImageWidth()) &&
			(y > 0) && (y < image.getImageHeight())) {
			return image.getPixel(x, y);
		} else {
			if (x == 0)
				x = image.getImageWidth() - 1;
			else if (x < 0)
				x = image.getImageWidth() - Math.abs(x);
			else if (x >= image.getImageWidth())
				x = x - image.getImageWidth();
			
			if (y == 0)
				y = image.getImageHeight() - 1;
			else if (y < 0)
				y = image.getImageHeight() - Math.abs(y);
			else if (y >= image.getImageHeight())
				y = y - image.getImageHeight();
				
			return image.getPixel(x, y);
		}
	}

	/**
	 * Returns and validates the PixelDouble at the specified coordinate.
	 * Original Pixel is converted to PixelDouble.
	 * If the PixelDouble is out of range, the opposite PixelDouble is returned.
	 * It's just like if the image was a sphere, linked from each side.
	 * @param image source ImageDouble
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the validated PixelDouble value at the specified coordinates
	 */	
	public PixelDouble pixelAt(ImageDouble image, int x, int y) {
		if ((x > 0) && (x < image.getImageWidth()) &&
			(y > 0) && (y < image.getImageHeight())) {
			return image.getPixel(x, y);
		} else {
			if (x == 0)
				x = image.getImageWidth() - 1;
			else if (x < 0)
				x = image.getImageWidth() - Math.abs(x);
			else if (x >= image.getImageWidth())
				x = x - image.getImageWidth();
			
			if (y == 0)
				y = image.getImageHeight() - 1;
			else if (y < 0)
				y = image.getImageHeight() - Math.abs(y);
			else if (y >= image.getImageHeight())
				y = y - image.getImageHeight();
				
			return image.getPixel(x, y);
		}
	}
}