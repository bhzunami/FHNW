package ch.fhnw.oop.led;

import javax.swing.*;
import java.awt.image.*;
import java.beans.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;

public class Led extends JComponent implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1690905801002949208L;
	public static final String COLOR_PROPERTY = "COLOR";
	public static final String BLINKING_PROPERTY = "BLINKING";
	public static final String ON_PROPERTY = "ON";
	public static final String FRAMEVISIBLE_PROPERTY = "FRAMEVISIBLE";
	private final Timer blinkTimer;
	private Color color;
	private boolean blinking;
	private boolean on;
	private boolean frameVisible;
	private PropertyChangeSupport propertySupport;
	private final Rectangle INNER_BOUNDS;
	private final Point2D CENTER;
	private int horizontalAlignment;
	private int verticalAlignment;
	private BufferedImage frameImage;
	private BufferedImage ledOffImage;
	private BufferedImage ledOnImage;
	private BufferedImage highlightImage;
	private boolean square;
	private final transient ComponentListener COMPONENT_LISTENER;

	public Led() {
		super();
		this.INNER_BOUNDS = new Rectangle(0, 0, 100, 100);
		this.COMPONENT_LISTENER = new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				final int SIZE = (Led.this.getWidth() <= Led.this.getHeight()) ? Led.this
						.getWidth() : Led.this.getHeight();
				final Container parent = Led.this.getParent();
				if (parent != null && parent.getLayout() == null) {
					if (SIZE < Led.this.getMinimumSize().width
							|| SIZE < Led.this.getMinimumSize().height) {
						Led.this.setSize(Led.this.getMinimumSize());
					} else if (Led.this.square) {
						Led.this.setSize(SIZE, SIZE);
					} else {
						Led.this.setSize(Led.this.getWidth(),
								Led.this.getHeight());
					}
				} else if (SIZE < Led.this.getMinimumSize().width
						|| SIZE < Led.this.getMinimumSize().height) {
					Led.this.setPreferredSize(Led.this.getMinimumSize());
				} else if (Led.this.square) {
					Led.this.setPreferredSize(new Dimension(SIZE, SIZE));
				} else {
					Led.this.setPreferredSize(new Dimension(
							Led.this.getWidth(), Led.this.getHeight()));
				}
				Led.this.calcInnerBounds();
				Led.this.init(Led.this.INNER_BOUNDS.width,
						Led.this.INNER_BOUNDS.height);
			}
		};
		this.propertySupport = new PropertyChangeSupport(this);
		this.blinkTimer = new Timer(500, this);
		this.color = new Color(16711680);
		this.blinking = false;
		this.on = false;
		this.frameVisible = true;
		this.CENTER = new Point2D.Double();
		this.frameImage = this.createImage(this.INNER_BOUNDS.width,
				this.INNER_BOUNDS.height, 3);
		this.ledOffImage = this.createImage(this.INNER_BOUNDS.width,
				this.INNER_BOUNDS.height, 3);
		this.ledOnImage = this.createImage(this.INNER_BOUNDS.width,
				this.INNER_BOUNDS.height, 3);
		this.highlightImage = this.createImage(this.INNER_BOUNDS.width,
				this.INNER_BOUNDS.height, 3);
		this.horizontalAlignment = 0;
		this.verticalAlignment = 0;
		this.square = true;
		this.addComponentListener(this.COMPONENT_LISTENER);
	}

	public final void init(final int WIDTH, final int HEIGHT) {
		if (WIDTH <= 1 || HEIGHT <= 1) {
			return;
		}
		if (this.frameImage != null) {
			this.frameImage.flush();
		}
		this.frameImage = this.createFrameImage(WIDTH, HEIGHT);
		if (this.ledOffImage != null) {
			this.ledOffImage.flush();
		}
		this.ledOffImage = this.createLedOffImage(WIDTH, HEIGHT);
		if (this.ledOnImage != null) {
			this.ledOnImage.flush();
		}
		this.ledOnImage = this.createLedOnImage(WIDTH, HEIGHT);
		if (this.highlightImage != null) {
			this.highlightImage.flush();
		}
		this.highlightImage = this.createHighlightImage(WIDTH, HEIGHT);
		this.CENTER.setLocation(WIDTH / 2.0, HEIGHT / 2.0);
	}

	@Override
	protected void paintComponent(final Graphics G) {
		final Graphics2D G2 = (Graphics2D) G.create();
		G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		G2.translate(this.INNER_BOUNDS.x, this.INNER_BOUNDS.y);
		G2.drawImage(this.frameImage, 0, 0, null);
		G2.drawImage(this.ledOffImage, 0, 0, null);
		if (this.isOn()) {
			G2.drawImage(this.ledOnImage, 0, 0, null);
		} else {
			G2.drawImage(this.highlightImage, 0, 0, null);
		}
		G2.dispose();
	}

	public final Color getColor() {
		return this.color;
	}

	public final void setColor(final Color COLOR) {
		final Color oldColor = this.color;
		this.color = COLOR;
		this.propertySupport.firePropertyChange("COLOR", oldColor, this.color);
		this.init(this.INNER_BOUNDS.width, this.INNER_BOUNDS.height);
		this.repaint(this.INNER_BOUNDS);
	}

	public final boolean isBlinking() {
		return this.blinking;
	}

	public final void setBlinking(final boolean BLINKING) {
		final boolean oldBlinking = this.blinking;
		this.blinking = BLINKING;
		this.propertySupport.firePropertyChange("BLINKING", oldBlinking,
				this.blinking);
		if (BLINKING && !this.blinkTimer.isRunning()) {
			this.blinkTimer.start();
		} else {
			this.blinkTimer.stop();
			this.on = false;
			this.repaint(this.INNER_BOUNDS);
		}
	}

	public final boolean isOn() {
		return this.on;
	}

	public final void setOn(final boolean ON) {
		final boolean wasOn = this.on;
		this.on = ON;
		this.propertySupport.firePropertyChange("ON", wasOn, this.on);
		this.repaint(this.INNER_BOUNDS);
	}

	public final boolean isFrameVisible() {
		return this.frameVisible;
	}

	public final void setFrameVisible(final boolean FRAMEVISIBLE) {
		final boolean oldFrameVisible = this.frameVisible;
		this.frameVisible = FRAMEVISIBLE;
		this.propertySupport.firePropertyChange("FRAMEVISIBLE",
				oldFrameVisible, this.frameVisible);
		this.repaint(this.INNER_BOUNDS);
	}

	public int getHorizontalAlignment() {
		return this.horizontalAlignment;
	}

	public void setHorizontalAlignment(final int HORIZONTAL_ALIGNMENT) {
		this.horizontalAlignment = HORIZONTAL_ALIGNMENT;
	}

	public int getVerticalAlignment() {
		return this.verticalAlignment;
	}

	public void setVerticalAlignment(final int VERTICAL_ALIGNMENT) {
		this.verticalAlignment = VERTICAL_ALIGNMENT;
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener LISTENER) {
		if (this.isShowing()) {
			this.propertySupport.addPropertyChangeListener(LISTENER);
		}
	}

	@Override
	public void removePropertyChangeListener(
			final PropertyChangeListener LISTENER) {
		this.propertySupport.removePropertyChangeListener(LISTENER);
	}

	private void calcInnerBounds() {
		final Insets INSETS = this.getInsets();
		this.INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, this.getWidth()
				- INSETS.left - INSETS.right, this.getHeight() - INSETS.top
				- INSETS.bottom);
	}

	public Rectangle getInnerBounds() {
		return this.INNER_BOUNDS;
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(100, 100);
	}

	@Override
	public void setPreferredSize(final Dimension DIM) {
		final int SIZE = (DIM.width <= DIM.height) ? DIM.width : DIM.height;
		if (this.square) {
			super.setPreferredSize(new Dimension(SIZE, SIZE));
		} else {
			super.setPreferredSize(DIM);
		}
		this.calcInnerBounds();
		this.init(this.INNER_BOUNDS.width, this.INNER_BOUNDS.height);
	}

	@Override
	public void setSize(final int WIDTH, final int HEIGHT) {
		final int SIZE = (WIDTH <= HEIGHT) ? WIDTH : HEIGHT;
		if (this.square) {
			super.setSize(SIZE, SIZE);
		} else {
			super.setSize(WIDTH, HEIGHT);
		}
		this.calcInnerBounds();
		this.init(this.INNER_BOUNDS.width, this.INNER_BOUNDS.height);
	}

	@Override
	public void setSize(final Dimension DIM) {
		final int SIZE = (DIM.width <= DIM.height) ? DIM.width : DIM.height;
		if (this.square) {
			super.setSize(new Dimension(SIZE, SIZE));
		} else {
			super.setSize(DIM);
		}
		this.calcInnerBounds();
		this.init(this.INNER_BOUNDS.width, this.INNER_BOUNDS.height);
	}

	@Override
	public void setBounds(final Rectangle BOUNDS) {
		if (this.square) {
			if (BOUNDS.width <= BOUNDS.height) {
				int yNew = 0;
				switch (this.verticalAlignment) {
				case 1: {
					yNew = BOUNDS.y;
					break;
				}
				case 3: {
					yNew = BOUNDS.y + (BOUNDS.height - BOUNDS.width);
					break;
				}
				default: {
					yNew = BOUNDS.y + (BOUNDS.height - BOUNDS.width) / 2;
					break;
				}
				}
				super.setBounds(BOUNDS.x, yNew, BOUNDS.width, BOUNDS.width);
			} else {
				int xNew = 0;
				switch (this.horizontalAlignment) {
				case 2: {
					xNew = BOUNDS.x;
					break;
				}
				case 4: {
					xNew = BOUNDS.x + (BOUNDS.width - BOUNDS.height);
					break;
				}
				default: {
					xNew = BOUNDS.x + (BOUNDS.width - BOUNDS.height) / 2;
					break;
				}
				}
				super.setBounds(xNew, BOUNDS.y, BOUNDS.height, BOUNDS.height);
			}
		} else {
			super.setBounds(BOUNDS);
		}
		this.calcInnerBounds();
		this.init(this.INNER_BOUNDS.width, this.INNER_BOUNDS.height);
	}

	@Override
	public void setBounds(final int X, final int Y, final int WIDTH,
			final int HEIGHT) {
		if (this.square) {
			if (WIDTH <= HEIGHT) {
				int yNew = 0;
				switch (this.verticalAlignment) {
				case 1: {
					yNew = Y;
					break;
				}
				case 3: {
					yNew = Y + (HEIGHT - WIDTH);
					break;
				}
				default: {
					yNew = Y + (HEIGHT - WIDTH) / 2;
					break;
				}
				}
				super.setBounds(X, yNew, WIDTH, WIDTH);
			} else {
				int xNew = 0;
				switch (this.horizontalAlignment) {
				case 2: {
					xNew = X;
					break;
				}
				case 4: {
					xNew = X + (WIDTH - HEIGHT);
					break;
				}
				default: {
					xNew = X + (WIDTH - HEIGHT) / 2;
					break;
				}
				}
				super.setBounds(xNew, Y, HEIGHT, HEIGHT);
			}
		} else {
			super.setBounds(X, Y, WIDTH, HEIGHT);
		}
		this.calcInnerBounds();
		this.init(this.INNER_BOUNDS.width, this.INNER_BOUNDS.height);
	}

	private BufferedImage createImage(final int WIDTH, final int HEIGHT,
			final int TRANSPARENCY) {
		final GraphicsConfiguration GFX_CONF = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		if (WIDTH <= 0 || HEIGHT <= 0) {
			return GFX_CONF.createCompatibleImage(1, 1, TRANSPARENCY);
		}
		final BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH,
				HEIGHT, TRANSPARENCY);
		return IMAGE;
	}

	private BufferedImage createFrameImage(final int WIDTH, final int HEIGHT) {
		if (WIDTH <= 0 || HEIGHT <= 0) {
			return this.createImage(1, 1, 3);
		}
		final BufferedImage IMAGE = this.createImage(WIDTH, HEIGHT, 3);
		final Graphics2D G2 = IMAGE.createGraphics();
		G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		final int IMAGE_WIDTH = IMAGE.getWidth();
		final int IMAGE_HEIGHT = IMAGE.getHeight();
		final Ellipse2D SHAPE = new Ellipse2D.Double(0.15 * IMAGE_WIDTH,
				0.15 * IMAGE_HEIGHT, 0.7 * IMAGE_WIDTH, 0.7 * IMAGE_HEIGHT);
		final Paint SHAPE_FILL = new LinearGradientPaint(new Point2D.Double(
				0.25 * IMAGE_WIDTH, 0.25 * IMAGE_HEIGHT), new Point2D.Double(
				0.7379036790187178 * IMAGE_WIDTH,
				0.7379036790187178 * IMAGE_HEIGHT), new float[] { 0.0f, 0.15f,
				0.26f, 0.261f, 0.85f, 1.0f },
				new Color[] {
						new Color(0.078431375f, 0.078431375f, 0.078431375f,
								0.64705884f),
						new Color(0.078431375f, 0.078431375f, 0.078431375f,
								0.64705884f),
						new Color(0.16078432f, 0.16078432f, 0.16078432f,
								0.64705884f),
						new Color(0.16078432f, 0.16078432f, 0.16078432f,
								0.6431373f),
						new Color(0.78431374f, 0.78431374f, 0.78431374f,
								0.40392157f),
						new Color(0.78431374f, 0.78431374f, 0.78431374f,
								0.34509805f) });
		G2.setPaint(SHAPE_FILL);
		G2.fill(SHAPE);
		G2.dispose();
		return IMAGE;
	}

	private BufferedImage createLedOffImage(final int WIDTH, final int HEIGHT) {
		if (WIDTH <= 0 || HEIGHT <= 0) {
			return this.createImage(1, 1, 3);
		}
		final BufferedImage IMAGE = this.createImage(WIDTH, HEIGHT, 3);
		final Graphics2D G2 = IMAGE.createGraphics();
		G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		final int IMAGE_WIDTH = IMAGE.getWidth();
		final int IMAGE_HEIGHT = IMAGE.getHeight();
		final Ellipse2D GLOWOFF = new Ellipse2D.Double(0.25 * IMAGE_WIDTH,
				0.25 * IMAGE_HEIGHT, 0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
		final Paint GLOWOFF_FILL = new LinearGradientPaint(new Point2D.Double(
				0.33 * IMAGE_WIDTH, 0.33 * IMAGE_HEIGHT), new Point2D.Double(
				0.6694112549695429 * IMAGE_WIDTH,
				0.6694112549695427 * IMAGE_HEIGHT), new float[] { 0.0f, 0.49f,
				1.0f }, new Color[] { this.color.darker().darker(),
				this.color.darker().darker().darker(),
				this.color.darker().darker() });
		G2.setPaint(GLOWOFF_FILL);
		G2.fill(GLOWOFF);
		G2.dispose();
		return IMAGE;
	}

	private BufferedImage createLedOnImage(final int WIDTH, final int HEIGHT) {
		if (WIDTH <= 0 || HEIGHT <= 0) {
			return this.createImage(1, 1, 3);
		}
		final BufferedImage IMAGE = this.createImage(WIDTH, HEIGHT, 3);
		final Graphics2D G2 = IMAGE.createGraphics();
		G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		final int IMAGE_WIDTH = IMAGE.getWidth();
		final int IMAGE_HEIGHT = IMAGE.getHeight();
		final Ellipse2D GLOW = new Ellipse2D.Double(0.15 * IMAGE_WIDTH,
				0.15 * IMAGE_HEIGHT, 0.7 * IMAGE_WIDTH, 0.7 * IMAGE_HEIGHT);
		final Paint GLOW_FILL = new RadialGradientPaint(new Point2D.Double(
				0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT), 0.35f * IMAGE_WIDTH,
				new float[] { 0.0f, 0.5f, 0.73f, 1.0f }, new Color[] {
						new Color(1.0f, 1.0f, 1.0f, 0.0f),
						new Color(1.0f, 1.0f, 1.0f, 0.0f),
						new Color(this.color.getRed(), this.color.getGreen(),
								this.color.getBlue(), 153),
						new Color(this.color.getRed(), this.color.getGreen(),
								this.color.getBlue(), 25) });
		G2.setPaint(GLOW_FILL);
		G2.fill(GLOW);
		final Ellipse2D ON = new Ellipse2D.Double(0.25 * IMAGE_WIDTH,
				0.25 * IMAGE_HEIGHT, 0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
		final Paint ON_FILL = new LinearGradientPaint(new Point2D.Double(
				0.33 * IMAGE_WIDTH, 0.33 * IMAGE_HEIGHT), new Point2D.Double(
				0.6694112549695429 * IMAGE_WIDTH,
				0.6694112549695427 * IMAGE_HEIGHT), new float[] { 0.0f, 0.49f,
				1.0f }, new Color[] { this.color, this.color.darker(),
				this.color.brighter() });
		G2.setPaint(ON_FILL);
		G2.fill(ON);
		G2.dispose();
		return IMAGE;
	}

	private BufferedImage createHighlightImage(final int WIDTH, final int HEIGHT) {
		if (WIDTH <= 0 || HEIGHT <= 0) {
			return this.createImage(1, 1, 3);
		}
		final BufferedImage IMAGE = this.createImage(WIDTH, HEIGHT, 3);
		final Graphics2D G2 = IMAGE.createGraphics();
		G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		final int IMAGE_WIDTH = IMAGE.getWidth();
		final int IMAGE_HEIGHT = IMAGE.getHeight();
		final Ellipse2D SHAPE = new Ellipse2D.Double(0.3 * IMAGE_WIDTH,
				0.3 * IMAGE_HEIGHT, 0.4 * IMAGE_WIDTH, 0.4 * IMAGE_HEIGHT);
		final Paint SHAPE_FILL = new RadialGradientPaint(new Point2D.Double(
				0.35 * IMAGE_WIDTH, 0.35 * IMAGE_HEIGHT), 0.205f * IMAGE_WIDTH,
				new float[] { 0.0f, 1.0f }, new Color[] {
						new Color(0.78431374f, 0.7607843f, 0.8156863f, 1.0f),
						new Color(0.78431374f, 0.7607843f, 0.8156863f, 0.0f) });
		G2.setPaint(SHAPE_FILL);
		G2.fill(SHAPE);
		G2.dispose();
		return IMAGE;
	}

	@Override
	public void actionPerformed(final ActionEvent EVENT) {
		if (EVENT.getSource().equals(this.blinkTimer)) {
			this.on ^= true;
			this.repaint(this.INNER_BOUNDS);
		}
	}

	@Override
	public String toString() {
		return "Led";
	}
}
