package model;

/**
 * Cette classe s'occupe du traitement à faire aux différentes couleurs.
 * 
 * @author Pierre-Alexandre Day
 *
 */
public class Colors {
	
	public int r;
	public int g;
	public int b;
	public double h;
	public double s;
	public double v;
	
	/**
	 * Ceci est le constructeur de couleur RGB.
	 * 
	 * @param r: la valeur rouge
	 * @param g: la valeur vert
	 * @param b: la valeur bleu
	 */
	public Colors(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
		
		RGBtoHSV(); //appel pour la conversion de RGB vers HSV
	}
	
	/**
	 * Ceci est le constructeur de couleur HSV.
	 * 
	 * @param h: la valeur pour la teinte
	 * @param s: la valeur pour la saturation
	 * @param v: la valeur pour l'intensité
	 */
	public Colors(double h, double s, double v) {
		this.h = h;
		this.s = s;
		this.v = v;
		
		HSVtoRGB(); //appel pour la conversion de HSV vers RGB
	}
	
	/**
	 * Méthode pour la conversion des couleurs RGB vers HSV.
	 * 
	 * À partir du code des notes du Cours 1 pwp 32
	 */
	private void RGBtoHSV() {	
//		double r = this.r / 255.0;
//		double g = this.g / 255.0;
//		double b = this.b / 255.0;
//		
//		double min = Math.min(r, Math.min(g, b));
//		double max = Math.max(r, Math.max(g, b));
//	    double diff = max - min;
//		
//	    double h = 0.0;
//	    double s = 0.0;
//	    double v = max;
//	    
//	    if (diff == 0.0)
//	    	h = 0.0;
//	    else {
//	    	if (r == max) {
//	    		if (g < b)
//	    			h = (g - b) / diff + 6;
//	    		else
//	    			h = (g - b) / diff;
//	    	} else if (g == max)
//		    	h = (b - r) / diff + 2;
//		    else if (b == max)
//		    	h = (r - g) / diff + 4;
//		    h = h * 60;
//	    }
//	    
//	    if (h < 0.0)
//	    	h += 360;
//	    	    
//	    if (max != 0.0)
//	    	s = diff / max;
//	    
//	    this.h = Math.round(h / 360 * 255);
//		this.s = Math.round(s * 255);
//		this.v = Math.round(v * 255);
		
//		------------------------------------

		double r = this.r / 255.0;
		double g = this.g / 255.0;
		double b = this.b / 255.0;
		
		double min = Math.min(r, Math.min(g, b));
		double max = Math.max(r, Math.max(g, b));
		
	    double h = 0.0;
	    double s = 0.0;
	    double v = max;
		
		if (r == max && g == min)
			h = 5 + (r - b) / (r - g);
		else if (r == max && b == min)
			h = 1 - (r - g) / (r - b);
		else if (g == max && b == min)
			h = 1 + (g - r) / (g - b);
		else if (g == max && r == min)
			h = 3 - (g - b) / (g - r);
		else if (b == max && r == min)
			h = 3 + (b - g) / (b - r);
		else if (b == max && g == min)
			h = 5 - (b - r) / (b - g);
		
		h = h * 60;
		
		if (h < 0)
			h += 360;
		
		s = (v - min) / v;
		
		this.h = Math.round(h / 360 * 255);
		this.s = Math.round(s * 255);
		this.v = Math.round(v * 255);
	}
	
	/**
	 * Méthode pour la conversion des couleurs HSV vers RGB.
	 * 
	 * Inspiré du code sur http://axonflux.com/handy-rgb-to-hsl-and-rgb-to-hsv-color-model-c
	 */
	private void HSVtoRGB() {
		double r = 0.0;
		double g = 0.0;
		double b = 0.0;
		double h = this.h / 255.0 * 360;
		double s = this.s / 255.0;
		double v = this.v / 255.0;

		int i = (int) Math.floor(h / 60);
	    double f = h / 60 - i;
	    double p = v * (1 - s);
	    double q = v * (1 - f * s);
	    double t = v * (1 - (1 - f) * s);
	    
	    if (i == 0 || i == 6) {
	    	r = v; g = t; b = p;
	    } else if (i == 1) {
	    	r = q; g = v; b = p;
	    } else if (i == 2) {
	    	r = p; g = v; b = t;
	    } else if (i == 3) {
	    	r = p; g = q; b = v;
	    } else if (i == 4) {
	    	r = t; g = p; b = v;
	    } else if (i == 5) {
	    	r = v; g = p; b = q;
	    }
	    	
	    this.r = (int) Math.round(r * 255);
		this.g = (int) Math.round(g * 255);
		this.b = (int) Math.round(b * 255);
	}
}
