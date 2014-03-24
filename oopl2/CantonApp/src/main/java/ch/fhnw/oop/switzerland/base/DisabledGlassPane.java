package ch.fhnw.oop.switzerland.base;

import javax.swing.border.*;
import java.beans.*;
import org.jdesktop.animation.timing.interpolation.*;
import org.jdesktop.animation.timing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class DisabledGlassPane extends JComponent
{
    private static final Color BASE_COLOR;
    private static final Color TEXT_COLOR;
    private static final Border MESSAGE_BORDER;
    private JLabel message;
    private float alpha;
    
    public DisabledGlassPane(final PresentationState pm) {
        super();
        this.message = new JLabel();
        this.alpha = 1.0f;
        this.setVisible(false);
        this.setOpaque(false);
        this.setBackground(DisabledGlassPane.BASE_COLOR);
        this.setLayout(new GridBagLayout());
        this.message.setOpaque(true);
        this.message.setBorder(DisabledGlassPane.MESSAGE_BORDER);
        this.message.setBackground(DisabledGlassPane.BASE_COLOR);
        this.message.setForeground(DisabledGlassPane.TEXT_COLOR);
        this.add(this.message, new GridBagConstraints());
        this.disableAllEvents();
        pm.addPropertyChangeListener("longRunningTaskMessage", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final String message = (String)evt.getNewValue();
                if (message == null) {
                    DisabledGlassPane.this.deactivate();
                }
                else {
                    DisabledGlassPane.this.activate(message);
                }
            }
        });
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
        this.repaint();
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.SrcOver.derive(this.alpha));
        super.paintComponent(g2);
    }
    
    private void activate(final String text) {
        if (this.isVisible()) {
            return;
        }
        if (text != null && text.length() > 0) {
            this.message.setText(text);
            this.message.setVisible(true);
        }
        else {
            this.message.setVisible(false);
        }
        this.setVisible(true);
        this.setCursor(Cursor.getPredefinedCursor(3));
        this.requestFocusInWindow();
    }
    
    private void deactivate() {
        if (!this.isVisible()) {
            return;
        }
        this.setCursor(null);
        final Animator animator = PropertySetter.createAnimator(800, this, "alpha", 0.0f);
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.3f);
        animator.addTarget(new TimingTargetAdapter() {
            @Override
            public void end() {
                DisabledGlassPane.this.setVisible(false);
            }
        });
        animator.start();
    }
    
    private void disableAllEvents() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                DisabledGlassPane.this.beep();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {});
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(final KeyEvent e) {
                e.consume();
            }
            
            @Override
            public void keyPressed(final KeyEvent e) {
                e.consume();
            }
            
            @Override
            public void keyReleased(final KeyEvent e) {
                e.consume();
                DisabledGlassPane.this.beep();
            }
        });
        this.setFocusTraversalKeysEnabled(false);
    }
    
    private void beep() {
        Toolkit.getDefaultToolkit().beep();
    }
    
    static {
        BASE_COLOR = UIManager.getColor("textHighlight");
        TEXT_COLOR = UIManager.getColor("textHighlightText");
        MESSAGE_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(0, DisabledGlassPane.BASE_COLOR, DisabledGlassPane.BASE_COLOR.darker()), BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }
}
