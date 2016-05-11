/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package view;

import java.awt.image.BufferedImage;

import model.Colors;
import model.ObserverIF;
import model.Pixel;

class HSVColorMediator extends Object implements SliderObserver, ObserverIF {
	ColorSlider hueCS;
	ColorSlider saturationCS;
	ColorSlider valueCS;
	int red;
	int green;
	int blue;
	double hue;
	double saturation;
	double value;
	BufferedImage hueImage;
	BufferedImage saturationImage;
	BufferedImage valueImage;
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
	HSVColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		Colors colors = new Colors(red, green, blue);
		this.hue = colors.h;
		this.saturation = colors.s;
		this.value = colors.v;
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();
		this.result = result;
		result.addObserver(this);
		
		hueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		valueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		computeHueImage(red, green, blue);
		computeSaturationImage(red, green, blue);
		computeValueImage(red, green, blue); 	
	}
	
	
	/*
	 * @see View.SliderObserver#update(double)
	 */
	public void update(ColorSlider s, int v) {
		boolean updateHue = false;
		boolean updateSaturation = false;
		boolean updateValue = false;
		
		if (s == hueCS && v != hue) {
			hue = v;
			updateSaturation = true;
			updateValue = true;
		}
		if (s == saturationCS && v != saturation) {
			saturation = v;
			updateHue = true;
			updateValue = true;
		}
		if (s == valueCS && v != value) {
			value = v;
			updateHue = true;
			updateSaturation = true;
		}
		
		Colors colors = new Colors(hue, saturation, value);
		red = colors.r;
		green = colors.g;
		blue = colors.b;
		
		if (updateHue) {
			computeHueImage(red, green, blue);
		}
		if (updateSaturation) {
			computeSaturationImage(red, green, blue);
		}
		if (updateValue) {
			computeValueImage(red, green, blue);
		}
		
		Pixel pixel = new Pixel(red, green, blue, 255);
		result.setPixel(pixel);
	}
	
	public void computeHueImage(int red, int green, int blue) { 
		Pixel p = new Pixel(red, green, blue, 255);
		Colors oldColor = new Colors(red, green, blue);
		
		for (int i = 0; i<imagesWidth; ++i) {
			Colors newColor = new Colors((int)(((double)i / (double)imagesWidth)*255.0), oldColor.s, oldColor.v);
			p.setRed(newColor.r);
			p.setGreen(newColor.g);
			p.setBlue(newColor.b);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				hueImage.setRGB(i, j, rgb);
			}
		}
		if (hueCS != null) {
			hueCS.update(hueImage);
		}
	}
	
	public void computeSaturationImage(int red, int green, int blue) {
		Pixel p = new Pixel(red, green, blue, 255);
		Colors oldColor = new Colors(red, green, blue);
		
		for (int i = 0; i<imagesWidth; ++i) {
			Colors newColor = new Colors(oldColor.h, (int)(((double)i / (double)imagesWidth)*255.0), oldColor.v);
			p.setRed(newColor.r);
			p.setGreen(newColor.g);
			p.setBlue(newColor.b);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				saturationImage.setRGB(i, j, rgb);
			}
		}
		if (saturationCS != null) {
			saturationCS.update(saturationImage);
		}
	}
	
	public void computeValueImage(int red, int green, int blue) { 
		Pixel p = new Pixel(red, green, blue, 255);
		Colors oldColor = new Colors(red, green, blue);
		
		for (int i = 0; i<imagesWidth; ++i) {
			Colors newColor = new Colors(oldColor.h, oldColor.s, (int)(((double)i / (double)imagesWidth))*255.0);
			p.setRed(newColor.r);
			p.setGreen(newColor.g);
			p.setBlue(newColor.b);
			
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				valueImage.setRGB(i, j, rgb);
			}
		}
		if (valueCS != null) {
			valueCS.update(valueImage);
		}
	}
	
	/**
	 * @return
	 */
	public BufferedImage getHueImage() {
		return hueImage;
	}

	/**
	 * @return
	 */
	public BufferedImage getSaturationImage() {
		return saturationImage;
	}

	/**
	 * @return
	 */
	public BufferedImage getValueImage() {
		return valueImage;
	}

	/**
	 * @param slider
	 */
	public void setHueCS(ColorSlider slider) {
		hueCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider
	 */
	public void setSaturationCS(ColorSlider slider) {
		saturationCS = slider;
		slider.addObserver(this);
	}

	/**
	 * @param slider
	 */
	public void setValueCS(ColorSlider slider) {
		valueCS = slider;
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


	/* (non-Javadoc)
	 * @see model.ObserverIF#update()
	 */
	public void update() {
		// When updated with the new "result" color, if the "currentColor"
		// is already properly set, there is no need to recompute the images.
		Pixel currentColor = new Pixel(red, green, blue, 255);
		
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;
		
		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();
		
		Colors colors = new Colors(red, green, blue);
		this.hue = colors.h;
		this.saturation = colors.s;
		this.value = colors.v;
		
		hueCS.setValue((int) hue);
		saturationCS.setValue((int) saturation);
		valueCS.setValue((int) value);
		
		computeHueImage(red, green, blue);
		computeSaturationImage(red, green, blue);
		computeValueImage(red, green, blue);
		
		// Efficiency issue: When the color is adjusted on a tab in the 
		// user interface, the sliders color of the other tabs are recomputed,
		// even though they are invisible. For an increased efficiency, the 
		// other tabs (mediators) should be notified when there is a tab 
		// change in the user interface. This solution was not implemented
		// here since it would increase the complexity of the code, making it
		// harder to understand.
	}

}

