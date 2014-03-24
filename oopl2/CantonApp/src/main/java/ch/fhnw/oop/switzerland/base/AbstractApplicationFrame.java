package ch.fhnw.oop.switzerland.base;

import java.awt.*;
import java.beans.*;
import javax.swing.*;
import org.jdesktop.animation.timing.interpolation.*;
import org.jdesktop.animation.timing.*;

public abstract class AbstractApplicationFrame extends JFrame
{
    public static final int FADE_IN_DURATION = 800;
    private final AbstractModel model;
    private final AbstractMasterDetailController controller;
    private float alpha;
    
    public AbstractApplicationFrame(final AbstractModel model, final AbstractMasterDetailController controller) {
        super();
        this.alpha = 0.2f;
        this.model = model;
        this.controller = controller;
    }
    
    public final void createAndShow() {
        this.setGlassPane(new DisabledGlassPane(this.controller.getPresentationState()));
        this.initializeComponents();
        final JComponent contents = this.layoutComponents();
        this.addEventListeners();
        this.controller.getPresentationState().addPropertyChangeListener("longRunningTaskMessage", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getNewValue() == null) {
                    AbstractApplicationFrame.fadeIn(AbstractApplicationFrame.this);
                }
            }
        });
        this.addObservers();
        this.add(contents);
        this.setDefaultCloseOperation(3);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
        this.repaint();
    }
    
    public static void fadeIn(final Object obj) {
        final Animator animator = PropertySetter.createAnimator(800, obj, "alpha", 1.0f);
        animator.setAcceleration(0.2f);
        animator.setDeceleration(0.3f);
        animator.start();
    }
    
    protected AbstractModel getApplicationModel() {
        return this.model;
    }
    
    protected AbstractMasterDetailController getController() {
        return this.controller;
    }
    
    protected abstract void initializeComponents();
    
    protected abstract JComponent layoutComponents();
    
    protected abstract void addEventListeners();
    
    protected abstract void addObservers();
}
