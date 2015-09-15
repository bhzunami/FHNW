package ch.fhnw.colorpicker;

/**
 * The ColorPicker Application
 * 
 * @author tobias
 *
 */
public class ColorPickerApp {

	/**
	 * The Entry Point of the application.
	 * 
	 * @param args
	 *            an array of arguments.
	 */
	public static void main(String[] args) {
		ColorPickerModel model = new ColorPickerModel();
		ColorPickerJFrame aFrame = new ColorPickerJFrame(model);
		aFrame.setVisible(true);
	}

}
