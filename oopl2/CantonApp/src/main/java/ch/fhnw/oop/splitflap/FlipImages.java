package ch.fhnw.oop.splitflap;

import java.awt.image.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.*;

public enum FlipImages {
	INSTANCE;

	private int formerWidth;
	private int formerHeight;
	private Font font;
	private BufferedImage[] flipImageArray;

	public BufferedImage[] getFlipImageArray() {
		return this.flipImageArray.clone();
	}

	public Font getFont() {
		return this.font;
	}

	public void recreateImages(final int WIDTH, final int HEIGHT) {
		if (WIDTH == this.formerWidth && HEIGHT == this.formerHeight) {
			return;
		}
		this.flipImageArray = this.createFlipImages(WIDTH,
				(int) (0.9838709677 * HEIGHT));
		this.formerWidth = WIDTH;
		this.formerHeight = HEIGHT;
	}

	private BufferedImage[] createFlipImages(final int WIDTH, final int HEIGHT) {
		final BufferedImage[] IMAGE_ARRAY = new BufferedImage[9];
		final GraphicsConfiguration GFX_CONF = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		final BufferedImage FLIP_IMG1 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		Graphics2D g2 = FLIP_IMG1.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP1 = new GeneralPath();
		FLIP1.moveTo(WIDTH * 0.08888888888888889, HEIGHT * 0.16923076923076924);
		FLIP1.quadTo(WIDTH * 0.08888888888888889, HEIGHT * 0.13846153846153847,
				WIDTH * 0.15555555555555556, HEIGHT * 0.13846153846153847);
		FLIP1.lineTo(WIDTH * 0.8444444444444444, HEIGHT * 0.13846153846153847);
		FLIP1.quadTo(WIDTH * 0.9111111111111111, HEIGHT * 0.13846153846153847,
				WIDTH * 0.9111111111111111, HEIGHT * 0.16923076923076924);
		FLIP1.lineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.43548387096774194);
		FLIP1.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.46774193548387094);
		FLIP1.lineTo(WIDTH * 0.13333333333333333, HEIGHT * 0.46774193548387094);
		FLIP1.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.43548387096774194);
		FLIP1.closePath();
		g2.setColor(new Color(5657937).darker());
		g2.fill(FLIP1);
		g2.dispose();
		IMAGE_ARRAY[0] = FLIP_IMG1;
		final BufferedImage FLIP_IMG2 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		g2 = FLIP_IMG2.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP2 = new GeneralPath();
		FLIP2.moveTo(WIDTH * 0.06666666666666667, HEIGHT * 0.25806451612903225);
		FLIP2.quadTo(WIDTH * 0.06666666666666667, HEIGHT * 0.22580645161290322,
				WIDTH * 0.13333333333333333, HEIGHT * 0.22580645161290322);
		FLIP2.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.22580645161290322);
		FLIP2.quadTo(WIDTH * 0.9333333333333333, HEIGHT * 0.22580645161290322,
				WIDTH * 0.9333333333333333, HEIGHT * 0.25806451612903225);
		FLIP2.lineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.43548387096774194);
		FLIP2.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.46774193548387094);
		FLIP2.lineTo(WIDTH * 0.13333333333333333, HEIGHT * 0.46774193548387094);
		FLIP2.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.43548387096774194);
		FLIP2.closePath();
		g2.setColor(new Color(5657937).darker().darker());
		g2.fill(FLIP2);
		g2.dispose();
		IMAGE_ARRAY[1] = FLIP_IMG2;
		final BufferedImage FLIP_IMG3 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		g2 = FLIP_IMG3.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP3 = new GeneralPath();
		FLIP3.moveTo(WIDTH * 0.044444444444444446, HEIGHT * 0.3225806451612903);
		FLIP3.quadTo(WIDTH * 0.044444444444444446, HEIGHT * 0.3064516129032258,
				WIDTH * 0.1111111111111111, HEIGHT * 0.3064516129032258);
		FLIP3.lineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.3064516129032258);
		FLIP3.quadTo(WIDTH * 0.9555555555555556, HEIGHT * 0.3064516129032258,
				WIDTH * 0.9555555555555556, HEIGHT * 0.3225806451612903);
		FLIP3.lineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.45161290322580644);
		FLIP3.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.46774193548387094);
		FLIP3.lineTo(WIDTH * 0.13333333333333333, HEIGHT * 0.46774193548387094);
		FLIP3.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.45161290322580644);
		FLIP3.closePath();
		g2.setColor(new Color(5657937).darker().darker());
		g2.fill(FLIP3);
		g2.dispose();
		IMAGE_ARRAY[2] = FLIP_IMG3;
		final BufferedImage FLIP_IMG4 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		g2 = FLIP_IMG4.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP4 = new GeneralPath();
		FLIP4.moveTo(WIDTH * 0.044444444444444446, HEIGHT * 0.4032258064516129);
		FLIP4.quadTo(WIDTH * 0.022222222222222223, HEIGHT * 0.3870967741935484,
				WIDTH * 0.08888888888888889, HEIGHT * 0.3870967741935484);
		FLIP4.lineTo(WIDTH * 0.9111111111111111, HEIGHT * 0.3870967741935484);
		FLIP4.quadTo(WIDTH * 0.9555555555555556, HEIGHT * 0.3870967741935484,
				WIDTH * 0.9777777777777777, HEIGHT * 0.4032258064516129);
		FLIP4.lineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.46774193548387094);
		FLIP4.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.46774193548387094);
		FLIP4.lineTo(WIDTH * 0.13333333333333333, HEIGHT * 0.46774193548387094);
		FLIP4.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.46774193548387094);
		FLIP4.closePath();
		g2.setColor(new Color(5657937).darker().darker());
		g2.fill(FLIP4);
		g2.dispose();
		IMAGE_ARRAY[3] = FLIP_IMG4;
		final BufferedImage FLIP_IMG5 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		g2 = FLIP_IMG5.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP5 = new GeneralPath();
		FLIP5.moveTo(WIDTH * 0.044444444444444446, HEIGHT * 0.46774193548387094);
		FLIP5.lineTo(WIDTH * 0.9555555555555556, HEIGHT * 0.46774193548387094);
		FLIP5.lineTo(WIDTH * 0.9555555555555556, HEIGHT * 0.4838709677419355);
		FLIP5.lineTo(WIDTH * 0.044444444444444446, HEIGHT * 0.4838709677419355);
		FLIP5.closePath();
		g2.setColor(new Color(5657937).darker().darker().darker());
		g2.fill(FLIP5);
		g2.dispose();
		IMAGE_ARRAY[4] = FLIP_IMG5;
		final BufferedImage FLIP_IMG6 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		g2 = FLIP_IMG6.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP6 = new GeneralPath();
		FLIP6.moveTo(WIDTH * 0.13333333333333333, HEIGHT * 0.5);
		FLIP6.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.5);
		FLIP6.lineTo(WIDTH * 0.9111111111111111, HEIGHT * 0.5161290322580645);
		FLIP6.lineTo(WIDTH * 0.9777777777777777, HEIGHT * 0.5645161290322581);
		FLIP6.quadTo(WIDTH * 0.9777777777777777, HEIGHT * 0.5806451612903226,
				WIDTH * 0.9111111111111111, HEIGHT * 0.5806451612903226);
		FLIP6.lineTo(WIDTH * 0.08888888888888889, HEIGHT * 0.5806451612903226);
		FLIP6.quadTo(WIDTH * 0.022222222222222223, HEIGHT * 0.5806451612903226,
				WIDTH * 0.044444444444444446, HEIGHT * 0.5645161290322581);
		FLIP6.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.5161290322580645);
		FLIP6.closePath();
		g2.setColor(new Color(5657937).brighter().brighter());
		g2.fill(FLIP6);
		g2.dispose();
		IMAGE_ARRAY[5] = FLIP_IMG6;
		final BufferedImage FLIP_IMG7 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		g2 = FLIP_IMG7.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP7 = new GeneralPath();
		FLIP7.moveTo(WIDTH * 0.13333333333333333, HEIGHT * 0.5);
		FLIP7.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.5);
		FLIP7.lineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.5161290322580645);
		FLIP7.lineTo(WIDTH * 0.9555555555555556, HEIGHT * 0.6451612903225806);
		FLIP7.quadTo(WIDTH * 0.9555555555555556, HEIGHT * 0.6612903225806451,
				WIDTH * 0.8888888888888888, HEIGHT * 0.6612903225806451);
		FLIP7.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.6612903225806451);
		FLIP7.quadTo(WIDTH * 0.044444444444444446, HEIGHT * 0.6612903225806451,
				WIDTH * 0.044444444444444446, HEIGHT * 0.6451612903225806);
		FLIP7.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.5161290322580645);
		FLIP7.closePath();
		g2.setColor(new Color(5657937).brighter());
		g2.fill(FLIP7);
		g2.dispose();
		IMAGE_ARRAY[6] = FLIP_IMG7;
		final BufferedImage FLIP_IMG8 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		g2 = FLIP_IMG8.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP8 = new GeneralPath();
		FLIP8.moveTo(WIDTH * 0.13333333333333333, HEIGHT * 0.5);
		FLIP8.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.5);
		FLIP8.lineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.532258064516129);
		FLIP8.lineTo(WIDTH * 0.9333333333333333, HEIGHT * 0.7096774193548387);
		FLIP8.quadTo(WIDTH * 0.9333333333333333, HEIGHT * 0.7419354838709677,
				WIDTH * 0.8666666666666667, HEIGHT * 0.7419354838709677);
		FLIP8.lineTo(WIDTH * 0.13333333333333333, HEIGHT * 0.7419354838709677);
		FLIP8.quadTo(WIDTH * 0.06666666666666667, HEIGHT * 0.7419354838709677,
				WIDTH * 0.06666666666666667, HEIGHT * 0.7096774193548387);
		FLIP8.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.532258064516129);
		FLIP8.closePath();
		g2.setColor(new Color(5657937).brighter());
		g2.fill(FLIP8);
		g2.dispose();
		IMAGE_ARRAY[7] = FLIP_IMG8;
		final BufferedImage FLIP_IMG9 = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, 3);
		g2 = FLIP_IMG9.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		final GeneralPath FLIP9 = new GeneralPath();
		FLIP9.moveTo(WIDTH * 0.13333333333333333, HEIGHT * 0.5);
		FLIP9.lineTo(WIDTH * 0.8666666666666667, HEIGHT * 0.5);
		FLIP9.lineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.532258064516129);
		FLIP9.lineTo(WIDTH * 0.9111111111111111, HEIGHT * 0.7903225806451613);
		FLIP9.quadTo(WIDTH * 0.9111111111111111, HEIGHT * 0.8225806451612904,
				WIDTH * 0.8444444444444444, HEIGHT * 0.8225806451612904);
		FLIP9.lineTo(WIDTH * 0.15555555555555556, HEIGHT * 0.8225806451612904);
		FLIP9.quadTo(WIDTH * 0.08888888888888889, HEIGHT * 0.8225806451612904,
				WIDTH * 0.08888888888888889, HEIGHT * 0.7903225806451613);
		FLIP9.lineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.532258064516129);
		FLIP9.closePath();
		g2.setColor(new Color(5657937));
		g2.fill(FLIP9);
		g2.dispose();
		IMAGE_ARRAY[8] = FLIP_IMG9;
		return IMAGE_ARRAY;
	}



	private FlipImages() {
		this.formerWidth = 0;
		this.formerHeight = 0;
		this.flipImageArray = this.createFlipImages(45, 61);
		try {
			this.font = Font.createFont(
					0,
					this.getClass().getResourceAsStream(
							"/swingdepartureboard/splitflap/ArialNarrow.ttf"));
		} catch (FontFormatException ex) {
			this.font = new Font("sans-serif", 0, 40);
		} catch (IOException ex2) {
			this.font = new Font("sans-serif", 0, 40);
		}
		GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
				this.font);
	}

}
