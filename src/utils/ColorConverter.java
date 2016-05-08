package utils;

/**
 * Created by etienne on 16-05-05.
 */
public class ColorConverter {

    /**
     * Methode permettant de convertir une couleur RGB en CMYK.
     * L'algorithme a ete inspire du site suivant:
     * http://www.rapidtables.com/convert/color/rgb-to-cmyk.htm
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public static int[] rgbToCMYK(int red, int green, int blue) {
        float cyan = (float) red / 255;
        float magenta = (float) green / 255;
        float yellow = (float) blue / 255;
        float black = 1 - Math.max(cyan, Math.max(magenta, yellow));

        return new int[] { Math.round((255 * (1 - cyan - black) / (1 - black))),
                Math.round((255 * (1 - magenta - black) / (1 - black))),
                Math.round((255 * (1 - yellow - black) / (1 - black))),
                Math.round((255 * black))};
    }

    /**
     * Methode permettant de convertir une couleur CMYK en RGB.
     * L'algorithme a ete inspire du site suivant:
     * http://www.rapidtables.com/convert/color/cmyk-to-rgb.htm
     * @param cyan
     * @param magenta
     * @param yellow
     * @param black
     * @return
     */
    public static int[] cmykToRGB(int cyan, int magenta, int yellow, int black) {
        return new int[] { Math.round((255 - cyan) * (255 - black) / 255),
                Math.round((255 - magenta) * (255 - black) / 255),
                Math.round((255 - yellow) * (255 - black) / 255) };
    }
}
