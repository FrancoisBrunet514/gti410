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

import utils.HSVConverter;
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

	/**
	 * Constructeur du médiateur de couleur HSV.
	 * 
	 * @param result
	 *            : résultat de l'image
	 * @param imagesWidth
	 *            : largeur de l'image
	 * @param imagesHeight
	 *            : hauteur de l'image
	 */
	HSVColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;

		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();
		this.result = result;
		result.addObserver(this);

		HSVConverter colors = new HSVConverter(red, green, blue);
		hue = colors.h;
		saturation = colors.s;
		value = colors.v;

		hueImage = new BufferedImage(imagesWidth, imagesHeight,
				BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight,
				BufferedImage.TYPE_INT_ARGB);
		valueImage = new BufferedImage(imagesWidth, imagesHeight,
				BufferedImage.TYPE_INT_ARGB);

		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);
	}

	/**
	 * Méthode pour recalculer les images résultantes des sliders HSV en
	 * fonction de la sélection par l'utilisateur.
	 * 
	 */
	public void update() {
		HSVConverter colors = new HSVConverter(hue, saturation, value);
		red = colors.r;
		green = colors.g;
		blue = colors.b;

		// When updated with the new "result" color, if the "currentColor"
		// is already properly set, there is no need to recompute the images.
		Pixel currentColor = new Pixel(red, green, blue, 255);

		if (currentColor.getARGB() == result.getPixel().getARGB())
			return;

		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();

		colors = new HSVConverter(red, green, blue);
		hue = colors.h;
		saturation = colors.s;
		value = colors.v;

		hueCS.setValue((int) hue);
		saturationCS.setValue((int) saturation);
		valueCS.setValue((int) value);

		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);

		// Efficiency issue: When the color is adjusted on a tab in the
		// user interface, the sliders color of the other tabs are recomputed,
		// even though they are invisible. For an increased efficiency, the
		// other tabs (mediators) should be notified when there is a tab
		// change in the user interface. This solution was not implemented
		// here since it would increase the complexity of the code, making it
		// harder to understand.
	}

	/**
	 * Méthode pour mettre à jour les images des sliders HSV suite à la
	 * sélection par utilisateur.
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
		if (updateHue) {
			computeHueImage(hue, saturation, value);
		}
		if (updateSaturation) {
			computeSaturationImage(hue, saturation, value);
		}
		if (updateValue) {
			computeValueImage(hue, saturation, value);
		}

		HSVConverter colors = new HSVConverter(hue, saturation, value);
		red = colors.r;
		green = colors.g;
		blue = colors.b;

		Pixel pixel = new Pixel(red, green, blue, 255);
		result.setPixel(pixel);
	}

	/**
	 * Méthode pour recalculer l'image pour la teinte.
	 * 
	 * @param hue: valeur de la teinte
	 * @param saturation: valeur de la saturation
	 * @param value: valeur de la luminosité
	 */
	public void computeHueImage(double hue, double saturation, double value) {
		HSVConverter colors = new HSVConverter(hue, saturation, value);
		red = colors.r;
		green = colors.g;
		blue = colors.b;

		Pixel p = new Pixel(red, green, blue, 255);

		for (int i = 0; i < imagesWidth; ++i) {
			colors = new HSVConverter(
					(int) (((double) i / (double) imagesWidth) * 255.0),
					colors.s, colors.v);
			p.setRed(colors.r);
			p.setGreen(colors.g);
			p.setBlue(colors.b);

			int rgb = p.getARGB();
			for (int j = 0; j < imagesHeight; ++j) {
				hueImage.setRGB(i, j, rgb);
			}
		}
		if (hueCS != null) {
			hueCS.update(hueImage);
		}
	}

	/**
	 * Méthode pour recalculer l'image pour la saturation.
	 * 
	 * @param hue: valeur de la teinte
	 * @param saturation: valeur de la saturation
	 * @param value: valeur de la luminosité
	 */
	public void computeSaturationImage(double hue, double saturation,
			double value) {
		HSVConverter colors = new HSVConverter(hue, saturation, value);
		red = colors.r;
		green = colors.g;
		blue = colors.b;

		Pixel p = new Pixel(red, green, blue, 255);

		for (int i = 0; i < imagesWidth; ++i) {
			colors = new HSVConverter(colors.h,
					(int) (((double) i / (double) imagesWidth) * 255.0),
					colors.v);
			p.setRed(colors.r);
			p.setGreen(colors.g);
			p.setBlue(colors.b);

			int rgb = p.getARGB();
			for (int j = 0; j < imagesHeight; ++j) {
				saturationImage.setRGB(i, j, rgb);
			}
		}
		if (saturationCS != null) {
			saturationCS.update(saturationImage);
		}
	}

	/**
	 * Méthode pour recalculer l'image pour la luminosité.
	 * 
	 * @param hue: valeur de la teinte
	 * @param saturation: valeur de la saturation
	 * @param value: valeur de la luminosité
	 */
	public void computeValueImage(double hue, double saturation, double value) {
		HSVConverter colors = new HSVConverter(hue, saturation, value);
		red = colors.r;
		green = colors.g;
		blue = colors.b;

		Pixel p = new Pixel(red, green, blue, 255);

		for (int i = 0; i < imagesWidth; ++i) {
			colors = new HSVConverter(colors.h, colors.s,
					(int) (((double) i / (double) imagesWidth) * 255.0));
			p.setRed(colors.r);
			p.setGreen(colors.g);
			p.setBlue(colors.b);

			int rgb = p.getARGB();
			for (int j = 0; j < imagesHeight; ++j) {
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

}
