package ch.fhnw.colorpicker;

import java.awt.Color;
import java.util.Observable;

/**
 * The model for the ColorPickerJFrame.
 * 
 * @author tobias
 *
 */
public class ColorPickerModel extends Observable {

	/**
	 * The int value for the color red.
	 */
	private int red;

	/**
	 * The int value for the color green.
	 */
	private int green;

	/**
	 * The int value for the color blue.
	 */
	private int blue;

	/**
	 * Default constructor.
	 */
	public ColorPickerModel() {
		super();
	}

	/**
	 * Gets the red
	 * 
	 * @return the red
	 */
	public int getRed() {
		return red;
	}
	
	/**
	 * Sets red green blue by color object.
	 * @param inColor the color object.
	 */
	public void setColor(Color inColor){
		red = inColor.getRed();
		green = inColor.getGreen();
		blue = inColor.getBlue();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Gets the color as a color object.
	 * @return the color object.
	 */
	public Color getColor(){
		return new Color(red, green, blue);
	}

	/**
	 * Sets the red
	 * 
	 * @param red
	 *            the red to set
	 */
	public void setRed(int red) {
		this.red = red;
		setChanged();
		notifyObservers();
	}

	/**
	 * Gets the green
	 * 
	 * @return the green
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * Sets the green
	 * 
	 * @param green
	 *            the green to set
	 */
	public void setGreen(int green) {
		this.green = green;
		setChanged();
		notifyObservers();
	}

	/**
	 * Gets the blue
	 * 
	 * @return the blue
	 */
	public int getBlue() {
		return blue;
	}

	/**
	 * Sets the blue
	 * 
	 * @param blue
	 *            the blue to set
	 */
	public void setBlue(int blue) {
		this.blue = blue;
		setChanged();
		notifyObservers();
	}

}
