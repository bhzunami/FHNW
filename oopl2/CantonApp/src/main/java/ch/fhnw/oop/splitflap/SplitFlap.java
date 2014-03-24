package ch.fhnw.oop.splitflap;

import java.util.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SplitFlap extends JComponent implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8969025911812810505L;
	public static final String[] TIME_0_TO_5;
    public static final String[] TIME_0_TO_9;
    public static final String[] NUMERIC;
    public static final String[] ALPHANUMERIC;
    public static final String[] EXTENDED;
    private static final String PROPERTY_TEXT = "text";
    private final Rectangle INNER_BOUNDS;
    private final BasicStroke THIN_STROKE;
    private volatile int currentFlipSeqImage;
    private BufferedImage backgroundImage;
    private BufferedImage foregroundImage;
    private BufferedImage flipSequenceImage;
    private String[] selection;
    private ArrayList<String> selectedSet;
    private int currentSelectionIndex;
    private int nextSelectionIndex;
    private int previousSelectionIndex;
    private String text;
    private boolean flipping;
    private boolean flipComplete;
    private Font font;
    private Color fontColor;
    private final Rectangle2D CLIP;
    private final Rectangle2D TOP_CLIP;
    private final Rectangle2D BOTTOM_CLIP;
    private final transient ComponentListener COMPONENT_LISTENER;
    
    public SplitFlap() {
        super();
        this.addComponentListener(this.COMPONENT_LISTENER = new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent EVENT) {
                final boolean SQUARE = SplitFlap.this.getWidth() == SplitFlap.this.getHeight();
                final int SIZE = (SplitFlap.this.getWidth() <= SplitFlap.this.getHeight()) ? SplitFlap.this.getWidth() : SplitFlap.this.getHeight();
                final Container parent = SplitFlap.this.getParent();
                if (parent != null && parent.getLayout() == null) {
                    if (SIZE < SplitFlap.this.getMinimumSize().width || SIZE < SplitFlap.this.getMinimumSize().height) {
                        SplitFlap.this.setSize(SplitFlap.this.getMinimumSize().width, SplitFlap.this.getMinimumSize().height);
                    }
                    else if (SQUARE) {
                        SplitFlap.this.setSize(SIZE, SIZE);
                    }
                    else {
                        SplitFlap.this.setSize(SplitFlap.this.getWidth(), SplitFlap.this.getHeight());
                    }
                }
                else if (SIZE < SplitFlap.this.getMinimumSize().width || SIZE < SplitFlap.this.getMinimumSize().height) {
                    SplitFlap.this.setPreferredSize(SplitFlap.this.getMinimumSize());
                }
                else if (SQUARE) {
                    SplitFlap.this.setPreferredSize(new Dimension(SIZE, SIZE));
                }
                else {
                    SplitFlap.this.setPreferredSize(new Dimension(SplitFlap.this.getWidth(), SplitFlap.this.getHeight()));
                }
                SplitFlap.this.calcInnerBounds();
                if (SQUARE) {
                    SplitFlap.this.init(SplitFlap.this.INNER_BOUNDS.width, SplitFlap.this.INNER_BOUNDS.width);
                }
                else {
                    SplitFlap.this.init(SplitFlap.this.INNER_BOUNDS.width, SplitFlap.this.INNER_BOUNDS.height);
                }
            }
        });
        GlobalTimer.INSTANCE.addComponent(this);
        this.INNER_BOUNDS = new Rectangle(0, 0, 22, 31);
        this.THIN_STROKE = new BasicStroke(0.5f);
        this.currentFlipSeqImage = 0;
        this.selection = SplitFlap.EXTENDED;
        this.selectedSet = new ArrayList<String>(64);
        this.currentSelectionIndex = 0;
        this.nextSelectionIndex = 1;
        this.previousSelectionIndex = this.selection.length - 1;
        this.text = " ";
        this.flipping = false;
        this.flipComplete = false;
        this.fontColor = new Color(16770049);
        this.CLIP = new Rectangle2D.Double();
        this.TOP_CLIP = new Rectangle2D.Double();
        this.BOTTOM_CLIP = new Rectangle2D.Double();
        this.selectedSet.addAll(Arrays.asList(SplitFlap.EXTENDED));
        this.init(this.getWidth(), this.getHeight());
    }
    
    private void init(final int WIDTH, final int HEIGHT) {
        if (WIDTH <= 1 || HEIGHT <= 1) {
            return;
        }
        if (this.backgroundImage != null) {
            this.backgroundImage.flush();
        }
        this.backgroundImage = this.createBackgroundImage(WIDTH, HEIGHT);
        if (this.foregroundImage != null) {
            this.foregroundImage.flush();
        }
        this.foregroundImage = this.createForegroundImage(WIDTH, HEIGHT);
        this.font = FlipImages.INSTANCE.getFont().deriveFont(0.6451613f * HEIGHT).deriveFont(1);
        FlipImages.INSTANCE.recreateImages(WIDTH, HEIGHT);
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D G2 = (Graphics2D)g.create();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        G2.drawImage(this.backgroundImage, 0, 0, this);
        this.CLIP.setRect(G2.getClipBounds());
        G2.setClip(this.TOP_CLIP);
        G2.setPaint(this.fontColor);
        G2.setFont(this.font);
        FontMetrics metrics = G2.getFontMetrics();
        Rectangle2D charBounds = metrics.getStringBounds(this.selectedSet.get(this.currentSelectionIndex), G2);
        G2.translate(this.getWidth() * 0.1111111111111111, this.getHeight() * 0.078);
        G2.drawString(this.selectedSet.get(this.currentSelectionIndex), (float)((this.TOP_CLIP.getWidth() - charBounds.getWidth()) / 2.0), (float)(this.TOP_CLIP.getHeight() + metrics.getHeight() / 2.0f - metrics.getDescent()));
        G2.translate(-this.getWidth() * 0.1111111111111111, -this.getHeight() * 0.078);
        G2.setClip(this.BOTTOM_CLIP);
        G2.setPaint(this.fontColor);
        G2.setFont(this.font);
        metrics = G2.getFontMetrics();
        if (!this.flipComplete) {
            charBounds = metrics.getStringBounds(this.selectedSet.get(this.previousSelectionIndex), G2);
            G2.translate(this.getWidth() * 0.1111111111111111, this.getHeight() * 0.49);
            G2.drawString(this.selectedSet.get(this.previousSelectionIndex), (float)((this.BOTTOM_CLIP.getWidth() - charBounds.getWidth()) / 2.0), metrics.getHeight() / 2.0f - metrics.getDescent());
            G2.translate(-this.getWidth() * 0.1111111111111111, -this.getHeight() * 0.49);
        }
        else {
            charBounds = metrics.getStringBounds(this.selectedSet.get(this.currentSelectionIndex), G2);
            G2.translate(this.getWidth() * 0.1111111111111111, this.getHeight() * 0.49);
            G2.drawString(this.selectedSet.get(this.currentSelectionIndex), (float)((this.BOTTOM_CLIP.getWidth() - charBounds.getWidth()) / 2.0), metrics.getHeight() / 2.0f - metrics.getDescent());
            G2.translate(-this.getWidth() * 0.1111111111111111, -this.getHeight() * 0.49);
        }
        G2.setClip(this.CLIP);
        G2.drawImage(this.foregroundImage, 0, 0, this);
        if (this.flipSequenceImage != null) {
            G2.drawImage(this.flipSequenceImage, 0, 0, this);
        }
        G2.dispose();
    }
    
    public Color getFontColor() {
        return this.fontColor;
    }
    
    public void setFontColor(final Color FONT_COLOR) {
        this.fontColor = FONT_COLOR;
        this.repaint(this.INNER_BOUNDS);
    }
    
    public String[] getSelection() {
        return this.selection;
    }
    
    public void setSelection(final String[] SELECTION) {
        this.selectedSet.clear();
        if (SELECTION.length == 0) {
            this.selectedSet.addAll(Arrays.asList(SplitFlap.EXTENDED));
        }
        else {
            this.selectedSet.addAll(Arrays.asList(SELECTION));
        }
        this.currentSelectionIndex = 0;
        this.nextSelectionIndex = 1;
        this.previousSelectionIndex = this.selectedSet.size() - 1;
    }
    
    public ArrayList<String> getSelectedSet() {
        return this.selectedSet;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String TEXT) {
        if (TEXT.isEmpty()) {
            return;
        }
        final String oldText = this.text;
        if (!TEXT.isEmpty() || this.selectedSet.contains(TEXT.substring(0, 1))) {
            this.text = TEXT.substring(0, 1);
        }
        else {
            this.text = this.selectedSet.get(0);
        }
        this.firePropertyChange("text", oldText, this.text);
        if (!this.selectedSet.get(this.currentSelectionIndex).equals(this.text)) {
            this.flipping = true;
            this.flipComplete = true;
        }
    }
    
    public final String getNextText() {
        return this.selectedSet.get(this.nextSelectionIndex);
    }
    
    public final String getPreviousText() {
        return this.selectedSet.get(this.previousSelectionIndex);
    }
    
    public final void flipForward() {
        this.previousSelectionIndex = this.currentSelectionIndex;
        ++this.currentSelectionIndex;
        if (this.currentSelectionIndex >= this.selectedSet.size()) {
            this.currentSelectionIndex = 0;
        }
        this.nextSelectionIndex = this.currentSelectionIndex + 1;
        if (this.nextSelectionIndex >= this.selectedSet.size()) {
            this.nextSelectionIndex = 0;
        }
        this.setText(this.selectedSet.get(this.currentSelectionIndex));
    }
    
    private BufferedImage createBackgroundImage(final int WIDTH, final int HEIGHT) {
        final GraphicsConfiguration GFX_CONF = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, 1);
        final Graphics2D G2 = IMAGE.createGraphics();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        final Point2D START_BACKGROUND = new Point2D.Float(0.0f, 0.0f);
        final Point2D STOP_BACKGROUND = new Point2D.Float(0.0f, HEIGHT);
        final float[] FRACTIONS_BACKGROUND = { 0.0f, 1.0f };
        final Color[] COLORS_BACKGROUND = { new Color(5460557), new Color(3883319) };
        final LinearGradientPaint BACKGROUND_FILL = new LinearGradientPaint(START_BACKGROUND, STOP_BACKGROUND, FRACTIONS_BACKGROUND, COLORS_BACKGROUND);
        G2.setPaint(BACKGROUND_FILL);
        G2.fill(new Rectangle2D.Float(0.0f, 0.0f, WIDTH, HEIGHT));
        final Point2D START_HIGHLIGHT = new Point2D.Double(0.0, 0.0322580645 * HEIGHT);
        final Point2D STOP_HIGHLIGHT = new Point2D.Double(0.0, 0.9677419355 * HEIGHT);
        final float[] FRACTIONS_HIGHLIGHT = { 0.0f, 0.03f, 0.97f, 1.0f };
        final Color[] COLORS_HIGHLIGHT = { new Color(1841424), new Color(4078386), new Color(4078386), new Color(9669504) };
        final LinearGradientPaint HIGHLIGHT_FILL = new LinearGradientPaint(START_HIGHLIGHT, STOP_HIGHLIGHT, FRACTIONS_HIGHLIGHT, COLORS_HIGHLIGHT);
        G2.setPaint(HIGHLIGHT_FILL);
        G2.fill(new Rectangle2D.Double(0.0444444444 * WIDTH, 0.0322580645 * HEIGHT, 0.9111111111 * WIDTH, 0.935483871 * HEIGHT));
        final Point2D START_INNER_BACKGROUND = new Point2D.Double(0.0, 0.0483870968 * HEIGHT);
        final Point2D STOP_INNER_BACKGROUND = new Point2D.Double(0.0, 0.9516129032 * HEIGHT);
        final float[] FRACTIONS_INNER_BACKGROUND = { 0.0f, 0.02f, 0.96f, 0.98f, 1.0f };
        final Color[] COLORS_INNER_BACKGROUND = { new Color(39, 39, 39, 255), new Color(0, 0, 0, 255), new Color(0, 0, 0, 255), new Color(101, 101, 101, 255), new Color(0, 0, 0, 255) };
        final LinearGradientPaint INNER_BACKGROUND_FILL = new LinearGradientPaint(START_INNER_BACKGROUND, STOP_INNER_BACKGROUND, FRACTIONS_INNER_BACKGROUND, COLORS_INNER_BACKGROUND);
        G2.setPaint(INNER_BACKGROUND_FILL);
        G2.fill(new Rectangle2D.Double(0.0666666667 * WIDTH, 0.0483870968 * HEIGHT, 0.8666666667 * WIDTH, 0.9032258065 * HEIGHT));
        G2.translate(this.getWidth() * 0.1111111111111111, this.getHeight() * 0.08064516129032258);
        final GeneralPath TOP = new GeneralPath();
        TOP.moveTo(0.0, HEIGHT * 0.4032258065 * 0.12);
        TOP.quadTo(0.0, 0.0, HEIGHT * 0.4032258065 * 0.12, 0.0);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.9142857142857143, 0.0);
        TOP.quadTo(WIDTH * 0.77777777777, 0.0, WIDTH * 0.77777777777, HEIGHT * 0.4032258065 * 0.12);
        TOP.lineTo(WIDTH * 0.77777777777, HEIGHT * 0.4032258065 * 0.76);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.9714285714285714, HEIGHT * 0.4032258065 * 0.76);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.9714285714285714, HEIGHT * 0.4032258065);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.02857142857142857, HEIGHT * 0.4032258065);
        TOP.lineTo(WIDTH * 0.77777777777 * 0.02857142857142857, HEIGHT * 0.4032258065 * 0.76);
        TOP.lineTo(0.0, HEIGHT * 0.4032258065 * 0.76);
        TOP.closePath();
        final Point2D TOP_START = new Point2D.Double(0.0, TOP.getBounds2D().getMinY());
        final Point2D TOP_STOP = new Point2D.Double(0.0, TOP.getBounds2D().getMaxY());
        final float[] TOP_FRACTIONS = { 0.0f, 0.03f, 0.98f, 1.0f };
        final Color[] TOP_COLORS = { new Color(98, 98, 98, 255), new Color(8, 8, 8, 255), new Color(38, 38, 38, 255), new Color(64, 64, 64, 255) };
        final LinearGradientPaint TOP_FILL = new LinearGradientPaint(TOP_START, TOP_STOP, TOP_FRACTIONS, TOP_COLORS);
        G2.setPaint(TOP_FILL);
        G2.fill(TOP);
        G2.translate(-this.getWidth() * 0.1111111111111111, -this.getHeight() * 0.08064516129032258);
        G2.translate(this.getWidth() * 0.1111111111111111, this.getHeight() * 0.5161290322580645);
        final GeneralPath BOTTOM = new GeneralPath();
        BOTTOM.moveTo(WIDTH * 0.77777777777 * 0.02857142857142857, 0.0);
        BOTTOM.lineTo(WIDTH * 0.77777777777 * 0.9714285714285714, 0.0);
        BOTTOM.lineTo(WIDTH * 0.77777777777 * 0.9714285714285714, HEIGHT * 0.4032258065 * 0.24);
        BOTTOM.lineTo(WIDTH * 0.77777777777, HEIGHT * 0.4032258065 * 0.24);
        BOTTOM.lineTo(WIDTH * 0.77777777777, HEIGHT * 0.4032258065 * 0.88);
        BOTTOM.quadTo(WIDTH * 0.77777777777, HEIGHT * 0.4032258065, WIDTH * 0.77777777777 * 0.9142857142857143, HEIGHT * 0.4032258065);
        BOTTOM.lineTo(WIDTH * 0.77777777777 * 0.08571428571428572, HEIGHT * 0.4032258065);
        BOTTOM.quadTo(0.0, HEIGHT * 0.4032258065, 0.0, HEIGHT * 0.4032258065 * 0.88);
        BOTTOM.lineTo(0.0, HEIGHT * 0.4032258065 * 0.24);
        BOTTOM.lineTo(WIDTH * 0.77777777777 * 0.02857142857142857, HEIGHT * 0.4032258065 * 0.24);
        BOTTOM.closePath();
        final Point2D BOTTOM_START = new Point2D.Double(0.0, BOTTOM.getBounds2D().getMinY());
        final Point2D BOTTOM_STOP = new Point2D.Double(0.0, BOTTOM.getBounds2D().getMaxY());
        final float[] BOTTOM_FRACTIONS = { 0.0f, 0.03f, 0.06f, 0.92f, 0.96f, 1.0f };
        final Color[] BOTTOM_COLORS = { new Color(81, 81, 81, 255), new Color(62, 62, 62, 255), new Color(62, 62, 62, 255), new Color(89, 89, 89, 255), new Color(85, 85, 85, 255), new Color(112, 112, 112, 255) };
        final LinearGradientPaint BOTTOM_FILL = new LinearGradientPaint(BOTTOM_START, BOTTOM_STOP, BOTTOM_FRACTIONS, BOTTOM_COLORS);
        G2.setPaint(BOTTOM_FILL);
        G2.fill(BOTTOM);
        G2.translate(-this.getWidth() * 0.1111111111111111, -this.getHeight() * 0.5161290322580645);
        this.TOP_CLIP.setRect(WIDTH * 0.1111111111111111, HEIGHT * 0.08064516129032258, TOP.getBounds2D().getWidth(), TOP.getBounds2D().getHeight());
        this.BOTTOM_CLIP.setRect(WIDTH * 0.1111111111111111, HEIGHT * 0.5161290322580645, BOTTOM.getBounds2D().getWidth(), BOTTOM.getBounds2D().getHeight());
        G2.dispose();
        return IMAGE;
    }
    
    private BufferedImage createForegroundImage(final int WIDTH, final int HEIGHT) {
        final GraphicsConfiguration GFX_CONF = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, 3);
        final Graphics2D G2 = IMAGE.createGraphics();
        final int IMAGE_WIDTH = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        G2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        G2.setStroke(this.THIN_STROKE);
        G2.setColor(new Color(10066329));
        G2.draw(new Line2D.Double(0.1333333333 * WIDTH, 0.4838709677 * HEIGHT, 0.84444444 * WIDTH, 0.4838709677 * HEIGHT));
        G2.setColor(Color.BLACK);
        G2.draw(new Line2D.Double(0.1333333333 * WIDTH, 0.5 * HEIGHT, 0.84444444 * WIDTH, 0.5 * HEIGHT));
        final Rectangle2D SIDEBARBACK_RIGHT = new Rectangle2D.Double(IMAGE_WIDTH * 0.9278350515463918, IMAGE_HEIGHT * 0.34459459459459457, IMAGE_WIDTH * 0.061855670103092786, IMAGE_HEIGHT * 0.32432432432432434);
        final Point2D SIDEBARBACK_RIGHT_START = new Point2D.Double(0.0, SIDEBARBACK_RIGHT.getBounds2D().getMinY());
        final Point2D SIDEBARBACK_RIGHT_STOP = new Point2D.Double(0.0, SIDEBARBACK_RIGHT.getBounds2D().getMaxY());
        final float[] SIDEBARBACK_FRACTIONS = { 0.0f, 0.29f, 1.0f };
        final Color[] SIDEBARBACK_COLORS = { new Color(0, 0, 0, 255), new Color(73, 74, 77, 255), new Color(0, 0, 0, 255) };
        final LinearGradientPaint SIDEBARBACK_RIGHT_FILL = new LinearGradientPaint(SIDEBARBACK_RIGHT_START, SIDEBARBACK_RIGHT_STOP, SIDEBARBACK_FRACTIONS, SIDEBARBACK_COLORS);
        G2.setPaint(SIDEBARBACK_RIGHT_FILL);
        G2.fill(SIDEBARBACK_RIGHT);
        final Rectangle2D SIDEBARFRONT_RIGHT = new Rectangle2D.Double(IMAGE_WIDTH * 0.9381443298969072, IMAGE_HEIGHT * 0.34797297297297297, IMAGE_WIDTH * 0.041237113402061855, IMAGE_HEIGHT * 0.3108108108108108);
        final Point2D SIDEBARFRONT_RIGHT_START = new Point2D.Double(0.0, SIDEBARFRONT_RIGHT.getBounds2D().getMinY());
        final Point2D SIDEBARFRONT_RIGHT_STOP = new Point2D.Double(0.0, SIDEBARFRONT_RIGHT.getBounds2D().getMaxY());
        final float[] SIDEBARFRONT_FRACTIONS = { 0.0f, 0.28f, 0.57f, 0.93f, 0.96f, 1.0f };
        final Color[] SIDEBARFRONT_COLORS = { new Color(32, 30, 31, 255), new Color(116, 117, 121, 255), new Color(30, 31, 31, 255), new Color(30, 31, 31, 255), new Color(51, 45, 48, 255), new Color(15, 13, 14, 255) };
        final LinearGradientPaint SIDEBARFRONT_RIGHT_FILL = new LinearGradientPaint(SIDEBARFRONT_RIGHT_START, SIDEBARFRONT_RIGHT_STOP, SIDEBARFRONT_FRACTIONS, SIDEBARFRONT_COLORS);
        G2.setPaint(SIDEBARFRONT_RIGHT_FILL);
        G2.fill(SIDEBARFRONT_RIGHT);
        final Rectangle2D SIDEBARBACK_LEFT = new Rectangle2D.Double(IMAGE_WIDTH * 0.010309278350515464, IMAGE_HEIGHT * 0.34459459459459457, IMAGE_WIDTH * 0.061855670103092786, IMAGE_HEIGHT * 0.32432432432432434);
        final Point2D SIDEBARBACK_LEFT_START = new Point2D.Double(0.0, SIDEBARBACK_LEFT.getBounds2D().getMinY());
        final Point2D SIDEBARBACK_LEFT_STOP = new Point2D.Double(0.0, SIDEBARBACK_LEFT.getBounds2D().getMaxY());
        final LinearGradientPaint SIDEBARBACK_LEFT_FILL = new LinearGradientPaint(SIDEBARBACK_LEFT_START, SIDEBARBACK_LEFT_STOP, SIDEBARBACK_FRACTIONS, SIDEBARBACK_COLORS);
        G2.setPaint(SIDEBARBACK_LEFT_FILL);
        G2.fill(SIDEBARBACK_LEFT);
        final Rectangle2D SIDEBARFRONT_LEFT = new Rectangle2D.Double(IMAGE_WIDTH * 0.020618556701030927, IMAGE_HEIGHT * 0.34797297297297297, IMAGE_WIDTH * 0.041237113402061855, IMAGE_HEIGHT * 0.3108108108108108);
        final Point2D SIDEBARFRONT_LEFT_START = new Point2D.Double(0.0, SIDEBARFRONT_LEFT.getBounds2D().getMinY());
        final Point2D SIDEBARFRONT_LEFT_STOP = new Point2D.Double(0.0, SIDEBARFRONT_LEFT.getBounds2D().getMaxY());
        final LinearGradientPaint SIDEBARFRONT_LEFT_FILL = new LinearGradientPaint(SIDEBARFRONT_LEFT_START, SIDEBARFRONT_LEFT_STOP, SIDEBARFRONT_FRACTIONS, SIDEBARFRONT_COLORS);
        G2.setPaint(SIDEBARFRONT_LEFT_FILL);
        G2.fill(SIDEBARFRONT_LEFT);
        G2.dispose();
        return IMAGE;
    }
    
    private void calcInnerBounds() {
        final Insets INSETS = this.getInsets();
        this.INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, this.getWidth() - INSETS.left - INSETS.right, this.getHeight() - INSETS.top - INSETS.bottom);
    }
    
    private Rectangle getInnerBounds() {
        return this.INNER_BOUNDS;
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(22, 31);
    }
    
    @Override
    public void setPreferredSize(final Dimension DIM) {
        super.setPreferredSize(DIM);
        this.calcInnerBounds();
        this.init(DIM.width, DIM.height);
    }
    
    @Override
    public void setSize(final int WIDTH, final int HEIGHT) {
        super.setSize(WIDTH, HEIGHT);
        this.calcInnerBounds();
        this.init(WIDTH, HEIGHT);
    }
    
    @Override
    public void setSize(final Dimension DIM) {
        super.setSize(DIM);
        this.calcInnerBounds();
        this.init(DIM.width, DIM.height);
    }
    
    @Override
    public void setBounds(final Rectangle BOUNDS) {
        super.setBounds(BOUNDS);
        this.calcInnerBounds();
        this.init(BOUNDS.width, BOUNDS.height);
    }
    
    @Override
    public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT) {
        super.setBounds(X, Y, WIDTH, HEIGHT);
        this.calcInnerBounds();
        this.init(WIDTH, HEIGHT);
    }
    
    @Override
    public void actionPerformed(final ActionEvent EVENT) {
        if (EVENT.getActionCommand().equals("flip") && this.flipping) {
            this.previousSelectionIndex = this.currentSelectionIndex;
            ++this.currentSelectionIndex;
            if (this.currentSelectionIndex >= this.selectedSet.size()) {
                this.currentSelectionIndex = 0;
            }
            this.nextSelectionIndex = this.currentSelectionIndex + 1;
            if (this.nextSelectionIndex >= this.selectedSet.size()) {
                this.nextSelectionIndex = 0;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SplitFlap.this.repaint(SplitFlap.this.INNER_BOUNDS);
                }
            });
            this.flipComplete = false;
            if (this.selectedSet.get(this.currentSelectionIndex).equals(this.text)) {
                this.flipping = false;
            }
        }
        if (EVENT.getActionCommand().equals("flipSequence") && !this.flipComplete) {
            if (this.currentFlipSeqImage == 9) {
                this.currentFlipSeqImage = 0;
                this.flipSequenceImage = null;
                this.flipComplete = true;
                this.repaint(this.INNER_BOUNDS);
            }
            else {
                this.flipSequenceImage = FlipImages.INSTANCE.getFlipImageArray()[this.currentFlipSeqImage];
                ++this.currentFlipSeqImage;
                this.repaint(this.INNER_BOUNDS);
            }
        }
    }
    
    static {
        TIME_0_TO_5 = new String[] { "1", "2", "3", "4", "5", "0" };
        TIME_0_TO_9 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
        NUMERIC = new String[] { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "'", ".", "-", "+" };
        ALPHANUMERIC = new String[] { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "\u00c4", "\u00d6", "\u00dc", "(", ")" };
        EXTENDED = new String[] { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "\u00c4", "\u00d6", "\u00dc", "-", "/", ":", ",", ".", ";", "@", "#", "+", "?", "!", "%", "$", "=", "<", ">", "'" };
    }
}
