package ch.fhnw.oop.splitflap;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;

import java.util.*;

public enum GlobalTimer implements ActionListener
{
    INSTANCE;
    
    private static final ActionEvent FLIP_EVENT;
    private static final ActionEvent FLIP_SEQUENCE_EVENT;
    private Timer flipTimer;
    private Timer flipSequenceTimer;
    private int currentSequence;
    private Set<SplitFlap> componentSet;
    
    private GlobalTimer() {
        this.flipTimer = new Timer(120, this);
        this.flipSequenceTimer = new Timer(10, this);
        this.currentSequence = 0;
        this.componentSet = new HashSet<SplitFlap>(32);
    }
    
    public void startTimer() {
        this.flipTimer.start();
    }
    
    public void stopTimer() {
        this.flipTimer.stop();
    }
    
    public void addComponent(final SplitFlap component) {
        this.componentSet.add(component);
    }
    
    public void removeComponent(final SplitFlap component) {
        this.componentSet.remove(component);
    }
    
    @Override
    public void actionPerformed(final ActionEvent EVENT) {
        if (EVENT.getSource().equals(this.flipTimer)) {
            for (final SplitFlap component : this.componentSet) {
                component.actionPerformed(GlobalTimer.FLIP_EVENT);
            }
            this.flipSequenceTimer.start();
        }
        if (EVENT.getSource().equals(this.flipSequenceTimer)) {
            if (this.currentSequence == 10) {
                this.currentSequence = 0;
                this.flipSequenceTimer.stop();
            }
            for (final SplitFlap component : this.componentSet) {
                component.actionPerformed(GlobalTimer.FLIP_SEQUENCE_EVENT);
            }
            ++this.currentSequence;
        }
    }
    
    static {
        FLIP_EVENT = new ActionEvent("flip", 1001, "flip");
        FLIP_SEQUENCE_EVENT = new ActionEvent("flipSequence", 1001, "flipSequence");
    }
}
