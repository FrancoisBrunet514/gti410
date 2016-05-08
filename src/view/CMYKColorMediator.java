package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;
import model.PixelDouble;
import utils.ColorConverter;

public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {

    // Sliders
	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider blackCS;

    // Composantes de couleur
	int cyan;
	int magenta;
	int yellow;
	int black;

    // Images pour chacune des composantes
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage blackImage;

    // Dimension des images
	int imagesWidth;
	int imagesHeight;

    // Le resultat
	ColorDialogResult result;

    /**
     * Constructeur
     * @param result
     * @param imagesWidth
     * @param imagesHeight
     */
	CMYKColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;

		int red = result.getPixel().getRed();
		int green = result.getPixel().getGreen();
		int blue = result.getPixel().getBlue();

		int[] cmyk = ColorConverter.rgbToCMYK(red, green, blue);

		this.cyan = cmyk[0];
		this.magenta = cmyk[1];
		this.yellow = cmyk[2];
		this.black = cmyk[3];

		this.result = result;
		result.addObserver(this);

		cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		blackImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		computeCyanImage(cyan, magenta, yellow, black);
		computeMagentaImage(cyan, magenta, yellow, black);
		computeYellowImage(cyan, magenta, yellow, black);
		computeBlackImage(cyan, magenta, yellow, black);
	}

	@Override
	public void update() {
		int[] rgb = ColorConverter.cmykToRGB(this.cyan, this.magenta, this.yellow, this.black);

        int red = rgb[0];
        int green = rgb[1];
        int blue = rgb[2];

		Pixel currentColor = new Pixel(red, green, blue, 255);
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;

		red = result.getPixel().getRed();
		green = result.getPixel().getGreen();
		blue = result.getPixel().getBlue();

		int[] cmyk = ColorConverter.rgbToCMYK(red, green, blue);

		cyanCS.setValue(cmyk[0]);
		magentaCS.setValue(cmyk[1]);
		yellowCS.setValue(cmyk[2]);
		blackCS.setValue(cmyk[3]);

		cyan = cmyk[0];
		magenta = cmyk[1];
		yellow = cmyk[2];
		black = cmyk[3];

		computeCyanImage(cyan, magenta, yellow, black);
		computeMagentaImage(cyan, magenta, yellow, black);
		computeYellowImage(cyan, magenta, yellow, black);
		computeBlackImage(cyan, magenta, yellow, black);
	}

	@Override
	public void update(ColorSlider cs, int v) {
		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		boolean updateBlack = false;

		if (cs == cyanCS && v != cyan) {
			cyan = v;
			updateMagenta = true;
			updateYellow = true;
			updateBlack = true;
		}
		if (cs == magentaCS && v != magenta) {
			magenta = v;
			updateCyan = true;
			updateYellow = true;
			updateBlack = true;
		}
		if (cs == yellowCS && v != yellow) {
			yellow = v;
			updateCyan = true;
			updateMagenta = true;
			updateBlack = true;
		}
		if (cs == blackCS && v != black) {
			black = v;
			updateCyan = true;
			updateMagenta = true;
			updateYellow = true;
			updateBlack = true;
		}

		if (updateCyan) {
			computeCyanImage(cyan, magenta, yellow, black);
		}
		if (updateMagenta) {
			computeMagentaImage(cyan, magenta, yellow, black);
		}
		if (updateYellow) {
			computeYellowImage(cyan, magenta, yellow, black);
		}
		if (updateBlack) {
			computeBlackImage(cyan, magenta, yellow, black);
		}

		int[] rgb = ColorConverter.cmykToRGB(cyan, magenta, yellow, black);

		Pixel pixel = new Pixel(rgb[0], rgb[1], rgb[2], 255);
		result.setPixel(pixel);
	}

    /**
     * Permet d'afficher le spectre de couleur pour la composante Cyan
     * @param cyan
     * @param magenta
     * @param yellow
     * @param black
     */
	private void computeCyanImage(int cyan, int magenta, int yellow, int black) {
		int[] rgbArray = ColorConverter.cmykToRGB(cyan, magenta, yellow, black);
		Pixel p = new Pixel(rgbArray[0], rgbArray[1], rgbArray[2], 255);
		for (int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(255 - black - ((double) i / (double) imagesWidth * (255 - black))));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}

    /**
     * Permet d'afficher le spectre de couleur pour la composante Magenta
     * @param cyan
     * @param magenta
     * @param yellow
     * @param black
     */
	private void computeMagentaImage(int cyan, int magenta, int yellow, int black) {
		int[] rgbArray = ColorConverter.cmykToRGB(cyan, magenta, yellow, black);
		Pixel p = new Pixel(rgbArray[0], rgbArray[1], rgbArray[2], 255);
		for (int i = 0; i<imagesWidth; ++i) {
			p.setGreen((int)(255 - black - ((double) i / (double) imagesWidth * (255 - black))));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				magentaImage.setRGB(i, j, rgb);
			}
		}
		if (magentaCS != null) {
			magentaCS.update(magentaImage);
		}
	}

    /**
     * Permet d'afficher le spectre de couleur pour la composante Yellow
     * @param cyan
     * @param magenta
     * @param yellow
     * @param black
     */
	private void computeYellowImage(int cyan, int magenta, int yellow, int black) {
		int[] rgbArray = ColorConverter.cmykToRGB(cyan, magenta, yellow, black);
		Pixel p = new Pixel(rgbArray[0], rgbArray[1], rgbArray[2], 255);
		for (int i = 0; i<imagesWidth; ++i) {
			p.setBlue((int)(255 - black - ((double) i / (double) imagesWidth * (255 - black))));
			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				yellowImage.setRGB(i, j, rgb);
			}
		}
		if (yellowCS != null) {
			yellowCS.update(yellowImage);
		}
	}

    /**
     * Permet d'afficher le spectre de couleur pour la composante Black/Key
     * @param cyan
     * @param magenta
     * @param yellow
     * @param black
     */
	private void computeBlackImage(int cyan, int magenta, int yellow, int black) {
		int _blackReference;
		int[] rgbArray = ColorConverter.cmykToRGB(cyan, magenta, yellow, black);
		Pixel p = new Pixel(rgbArray[0], rgbArray[1], rgbArray[2], 255);
		for (int i = 0; i<imagesWidth; ++i) {

			_blackReference = (int)Math.round((((double)i / (double)imagesWidth)* 255.0));

			int[] rgbArrayLoop = ColorConverter.cmykToRGB(cyan, magenta, yellow, _blackReference);

			p.setRed(rgbArrayLoop[0]);
			p.setGreen(rgbArrayLoop[1]);
			p.setBlue(rgbArrayLoop[2]);

			int rgb = p.getARGB();
			for (int j = 0; j<imagesHeight; ++j) {
				blackImage.setRGB(i, j, rgb);
			}
		}
		if (blackCS != null) {
			blackCS.update(blackImage);
		}
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
	public BufferedImage getBlackImage() {
		return blackImage;
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
	public void setBlackCS(ColorSlider slider) {
		blackCS = slider;
		slider.addObserver(this);
	}

}
