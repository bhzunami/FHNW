package ch.fhnw.colorpicker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ColorPickerJFrame extends JFrame implements Observer {

	/**
	 * The menu bar of this frame.
	 */
	private JMenuBar menuBar;

	/**
	 * The scroll bar for the color red.
	 */
	private JScrollBar redScroll;

	/**
	 * The scroll bar for the color green.
	 */
	private JScrollBar greenScroll;

	/**
	 * The scroll bar for the color blue.
	 */
	private JScrollBar blueScroll;

	/**
	 * The spinner for the red color.
	 */
	private JSpinner redSpinner;

	/**
	 * The spinner for the green color.
	 */
	private JSpinner greenSpinner;

	/**
	 * The spinner for the green color.
	 */
	private JSpinner blueSpinner;

	/**
	 * The hex field for the red color.
	 */
	private JTextField redHex;

	/**
	 * The hex field for the green color.
	 */
	private JTextField greenHex;

	/**
	 * The hex field for the green color.
	 */
	private JTextField blueHex;

	/**
	 * The color preview panel
	 */
	private JPanel colorPreview;

	/**
	 * The button group for the radio buttons
	 */
	private ButtonGroup radioButtonGroup;

	/**
	 * The radio button for the color red.
	 */
	private JRadioButton redRadioButton;

	/**
	 * The radio button for the color blue.
	 */
	private JRadioButton blueRadioButton;

	/**
	 * The radio button for the color green.
	 */
	private JRadioButton greenRadioButton;

	/**
	 * The radio button for the color yellow.
	 */
	private JRadioButton yellowRadioButton;

	/**
	 * The radio button for the color cyan.
	 */
	private JRadioButton cyanRadioButton;

	/**
	 * The radio button for the color orange.
	 */
	private JRadioButton orangeRadioButton;

	/**
	 * The radio button for the color black.
	 */
	private JRadioButton blackRadioButton;

	/**
	 * The button to make the color darker.
	 */
	private JButton darkerButton;

	/**
	 * the button to make the color brighter.
	 */
	private JButton brighterButton;

	/**
	 * The model that contains all data.
	 */
	private ColorPickerModel model;

	/**
	 * Default Constructor.
	 */
	public ColorPickerJFrame(ColorPickerModel inModel) {
		super();
		model = inModel;
		model.addObserver(this);
		setTitle("ColorPicker");
		setSize(new Dimension(300, 350));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		layoutComponents();
	}

	/**
	 * Initializes all the components.
	 */
	private void initComponents() {
		menuBar = new JMenuBar();
		menuBar.add(new JMenu("File"));
		menuBar.add(new JMenu("Attributes"));
		menuBar.getMenu(0).add(new JMenuItem("Exit"));
		menuBar.getMenu(0).getItem(0).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuBar.getMenu(1).add(new JCheckBox("Red"));
		menuBar.getMenu(1).add(new JCheckBox("Blue"));
		menuBar.getMenu(1).add(new JCheckBox("Green"));
		menuBar.getMenu(1).add(new JCheckBox("Yellow"));
		menuBar.getMenu(1).add(new JCheckBox("Cyan"));
		menuBar.getMenu(1).add(new JCheckBox("Orange"));
		menuBar.getMenu(1).add(new JCheckBox("Black"));

		SpinnerModel redSpinnerModel = new SpinnerNumberModel(0, 0, 255, 1);
		redSpinner = new JSpinner(redSpinnerModel);
		redSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				model.setRed((int) redSpinner.getValue());
			}
		});
		SpinnerModel greenSpinnerModel = new SpinnerNumberModel(0, 0, 255, 1);
		greenSpinner = new JSpinner(greenSpinnerModel);
		greenSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				model.setGreen((int) greenSpinner.getValue());				
			}
		});
		SpinnerModel blueSpinnerModel = new SpinnerNumberModel(0, 0, 255, 1);
		blueSpinner = new JSpinner(blueSpinnerModel);
		blueSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				model.setBlue((int) blueSpinner.getValue());
			}
		});

		redScroll = new JScrollBar(JScrollBar.HORIZONTAL, model.getRed(), 1, 0, 256);
		redScroll.setBackground(Color.RED);
		redScroll.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				model.setRed(redScroll.getValue());
			}
		});
		greenScroll = new JScrollBar(JScrollBar.HORIZONTAL, model.getGreen(), 1, 0, 256);
		greenScroll.setBackground(Color.GREEN);
		greenScroll.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				model.setGreen(greenScroll.getValue());
			}
		});
		blueScroll = new JScrollBar(JScrollBar.HORIZONTAL, model.getBlue(), 1, 0, 256);
		blueScroll.setBackground(Color.BLUE);
		blueScroll.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				model.setBlue(blueScroll.getValue());
			}
		});

		redHex = new JTextField(Integer.toHexString(model.getRed()));
		redHex.setEditable(false);
		greenHex = new JTextField(Integer.toHexString(model.getGreen()));
		greenHex.setEditable(false);
		blueHex = new JTextField(Integer.toHexString(model.getBlue()));
		blueHex.setEditable(false);

		colorPreview = new JPanel();
		colorPreview.setBackground(Color.BLACK);
		colorPreview.setBorder(new LineBorder(Color.BLACK));

		redRadioButton = new JRadioButton("Red");
		redRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(Color.RED);
			}
		});
		blueRadioButton = new JRadioButton("Blue");
		blueRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(Color.BLUE);
			}
		});
		greenRadioButton = new JRadioButton("Green");
		greenRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(Color.GREEN);
			}
		});
		yellowRadioButton = new JRadioButton("Yellow");
		yellowRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(Color.YELLOW);
			}
		});
		cyanRadioButton = new JRadioButton("Cyan");
		cyanRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(Color.CYAN);
			}
		});
		orangeRadioButton = new JRadioButton("Orange");
		orangeRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(Color.ORANGE);
			}
		});
		blackRadioButton = new JRadioButton("Black");
		blackRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(Color.BLACK);
			}
		});
		
		radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(redRadioButton);
		radioButtonGroup.add(blueRadioButton);
		radioButtonGroup.add(greenRadioButton);
		radioButtonGroup.add(yellowRadioButton);
		radioButtonGroup.add(cyanRadioButton);
		radioButtonGroup.add(orangeRadioButton);
		radioButtonGroup.add(blackRadioButton);

		darkerButton = new JButton("Darker");
		darkerButton.setEnabled(false);
		darkerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(model.getColor().darker());
			}
		});
		brighterButton = new JButton("Brighter");
		brighterButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setColor(model.getColor().brighter());
			}
		});
	}

	/**
	 * Layouts the components on the frame.
	 */
	private void layoutComponents() {
		setLayout(new MigLayout("fillx"));
		setJMenuBar(menuBar);
		add(redScroll, "grow");
		add(redSpinner, "grow");
		add(redHex, "wrap, grow");
		add(greenScroll, "grow");
		add(greenSpinner, "grow");
		add(greenHex, "wrap, grow");
		add(blueScroll, "grow");
		add(blueSpinner, "grow");
		add(blueHex, "wrap, grow");
		add(colorPreview, "span 1 7, grow");
		add(redRadioButton, "wrap");
		add(blueRadioButton, "wrap");
		add(greenRadioButton, "wrap");
		add(yellowRadioButton, "wrap");
		add(cyanRadioButton, "wrap");
		add(orangeRadioButton);
		add(darkerButton, "wrap, grow");
		add(blackRadioButton);
		add(brighterButton, "wrap, grow");
	}

	/**
	 * ${@inheritDoc}
	 */
	@Override
	public void update(Observable o, Object arg) {
		Color aColor = new Color(model.getRed(), model.getGreen(), model.getBlue());	
		
		// update spinners
		redSpinner.setValue(aColor.getRed());
		greenSpinner.setValue(aColor.getGreen());
		blueSpinner.setValue(aColor.getBlue());
		
		// set hex text field values
		redHex.setText(Integer.toHexString(model.getRed()));
		greenHex.setText(Integer.toHexString(model.getGreen()));
		blueHex.setText(Integer.toHexString(model.getBlue()));
		
		// set scrolls
		redScroll.setValue(model.getRed());
		greenScroll.setValue(model.getGreen());
		blueScroll.setValue(model.getBlue());
		
		// set preview
		colorPreview.setBackground(aColor);
		
		// set radio button
		if(aColor.equals(Color.RED)){
			redRadioButton.setSelected(true);
		} else if (aColor.equals(Color.BLUE)) {
			blueRadioButton.setSelected(true);
		} else if (aColor.equals(Color.GREEN)){
			greenRadioButton.setSelected(true);
		} else if (aColor.equals(Color.YELLOW)) {
			yellowRadioButton.setSelected(true);
		} else if (aColor.equals(Color.CYAN)) {
			cyanRadioButton.setSelected(true);
		} else if (aColor.equals(Color.ORANGE)){
			orangeRadioButton.setSelected(true);
		} else if (aColor.equals(Color.BLACK)){
			blackRadioButton.setSelected(true);
		} else {
			radioButtonGroup.clearSelection();
		}
		
		
		// enable disable darker / brighter buttons
		if(aColor.darker().equals(model.getColor())){
			darkerButton.setEnabled(false);
		} else {
			darkerButton.setEnabled(true);
		}
		if(aColor.brighter().equals(model.getColor())){
			brighterButton.setEnabled(false);
		} else {
			brighterButton.setEnabled(true);
		}
		
		// update view
		revalidate();
	}
}
