package controller;

import model.*;

/**
 * <p>Title: ImageAbsNorm255Strategy</p>
 * <p>Description: Image-related strategy using absolute and normalized values to 255</p>
 * <p>Copyright: Copyright (c) 2016 Day, Pierre-Alexandre</p>
 * <p>Company: ETS - École de Technologie Supérieure</p>
 * @author Day, Pierre-Alexandre
 */
public class ImageAbsNorm255Strategy extends ImageConversionStrategy {
	/**
	 * Converts an ImageDouble to an ImageX using an absolute and normalize to 255 strategy.
	 */
	public ImageX convert(ImageDouble image) {
		int imageWidth = image.getImageWidth();
		int imageHeight = image.getImageHeight();
		ImageX newImage = new ImageX(0, 0, imageWidth, imageHeight);
		PixelDouble curPixelDouble = null;
		PixelDouble biggestPixelDouble = new PixelDouble();

		// Trouver le pixelDouble avec les valeurs RGB les plus élevées
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				curPixelDouble = image.getPixel(x,y);
				
				if (Math.abs(curPixelDouble.getRed()) > biggestPixelDouble.getRed() 
						&& Math.abs(curPixelDouble.getBlue()) > biggestPixelDouble.getBlue()
						&& Math.abs(curPixelDouble.getGreen()) > biggestPixelDouble.getGreen()) {
					biggestPixelDouble.setRed(Math.abs(curPixelDouble.getRed()));
					biggestPixelDouble.setBlue(Math.abs(curPixelDouble.getBlue()));
					biggestPixelDouble.setGreen(Math.abs(curPixelDouble.getGreen()));
					biggestPixelDouble.setAlpha(Math.abs(curPixelDouble.getAlpha()));
				}
			}
		}
		
		newImage.beginPixelUpdate();
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				curPixelDouble = image.getPixel(x,y);
				
				newImage.setPixel(x, y, new Pixel((normalizeTo255(Math.abs(curPixelDouble.getRed()), biggestPixelDouble.getRed())),
												  (normalizeTo255(Math.abs(curPixelDouble.getGreen()), biggestPixelDouble.getBlue())),
												  (normalizeTo255(Math.abs(curPixelDouble.getBlue()), biggestPixelDouble.getGreen())),
												  (normalizeTo255(Math.abs(curPixelDouble.getAlpha()), biggestPixelDouble.getAlpha()))));
			}
		}
		newImage.endPixelUpdate();
		return newImage;
	}
	
	/**
	 * Fonction pour normaliser les valeurs à 255
	 * Code inspiré de l'exemple sur http://stackoverflow.com/questions/695084/how-do-i-normalize-an-image
	 * @param value: La valeur à normaliser
	 * @return newValue: La nouvelle valeur
	 */
	private int normalizeTo255(double value, double maxValue) {
		int newValue = (int) (255.0 * value / maxValue);
			
		return newValue;
	}
}