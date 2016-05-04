package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {
	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider keyCS;
	int red;
	int green;
	int blue;
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage keyImage;
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
	CMYKColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();
		this.result = result;
		result.addObserver(this);
		
		cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		keyImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		computeCyanImage(red, green, blue);
		computeMagentaImage(red, green, blue);
		computeYellowImage(red, green, blue);
		computeKeyImage(red, green, blue);
	}

	@Override
	public void update() {
		// When updated with the new "result" color, if the "currentColor"
		// is aready properly set, there is no need to recompute the images.
		Pixel currentColor = new Pixel(red, green, blue, 255);
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;
				
		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();
		
		double[] cmyk = RGBToCMYK(red, green, blue);
		
		cyanCS.setValue(cmyk[0]);
		magentaCS.setValue(cmyk[1]);
		yellowCS.setValue(cmyk[2]);

		computeCyanImage(red, green, blue);
		computeMagentaImage(red, green, blue);
		computeYellowImage(red, green, blue);
				
		// Efficiency issue: When the color is adjusted on a tab in the 
		// user interface, the sliders color of the other tabs are recomputed,
		// even though they are invisible. For an increased efficiency, the 
		// other tabs (mediators) should be notified when there is a tab 
		// change in the user interface. This solution was not implemented
		// here since it would increase the complexity of the code, making it
		// harder to understand.
	}

	@Override
	public void update(ColorSlider cs, int v) {
		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		if (cs == cyanCS && v != red) {
			red = v;
			updateMagenta = true;
			updateYellow = true;
		}
		if (cs == magentaCS && v != green) {
			green = v;
			updateCyan = true;
			updateYellow = true;
		}
		if (cs == yellowCS && v != blue) {
			blue = v;
			updateCyan = true;
			updateMagenta = true;
		}
		if (updateCyan) {
			computeCyanImage(red, green, blue);
		}
		if (updateMagenta) {
			computeMagentaImage(red, green, blue);
		}
		if (updateYellow) {
			computeYellowImage(red, green, blue);
		}
		
		Pixel pixel = new Pixel(red, green, blue, 255);
		result.setPixel(pixel);
	}
	
	private void computeCyanImage(int red, int green, int blue) {
		Pixel p = new Pixel(red, green, blue, 255); 
		for (int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();
			
			
			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}
	
	private void computeMagentaImage(int red, int green, int blue) {
			
	}
	
	private void computeYellowImage(int red, int green, int blue) {
		
	}
	
	private void computeKeyImage(int red, int green, int blue) {
		
	}
	
	private double[] RGBToCMYK(int red, int green, int blue) {
		double cyan = 1 - red / 255;
		double magenta = 1 - green / 255;
		double yellow = 1 - blue / 255;
		
		double key = Math.min(Math.min(cyan, magenta), Math.min(magenta, yellow));
	
		if (key == 1) {
			cyan = magenta = yellow = 0;
		} else {
			double s = 1 - key;
			cyan = (cyan - key) / s;
			magenta = (magenta - key) / s;
			yellow = (yellow - key) / s;
		}

		return new double[] { cyan, magenta, yellow, key };
	}
	
	/**
	 * @return
	 */
	public BufferedImage getCyanImage() {
		return cyanImage;
	}

	/**
	 * @return
	 */
	public BufferedImage getMagentaImage() {
		return magentaImage;
	}

	/**
	 * @return
	 */
	public BufferedImage getYellowImage() {
		return yellowImage;
	}
	
	/**
	 * @return
	 */
	public BufferedImage getKeyImage() {
		return keyImage;
	}

	/**
	 * @param slider
	 */
	public void setCyanCS(ColorSlider slider) {
		cyanCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider
	 */
	public void setMagentaCS(ColorSlider slider) {
		magentaCS = slider;
		slider.addObserver(this);
	}
	
	/**
	 * @param slider
	 */
	public void setYellowCS(ColorSlider slider) {
		yellowCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider
	 */
	public void setKeyCS(ColorSlider slider) {
		keyCS = slider;
		slider.addObserver(this);
	}
	
	/**
	 * @return
	 */
	public double getBlue() {
		return blue;
	}

	/**
	 * @return
	 */
	public double getGreen() {
		return green;
	}

	/**
	 * @return
	 */
	public double getRed() {
		return red;
	}

}
