package ch.fhnw.oop.switzerland.view;

import javax.swing.*;
import java.beans.*;
import ch.fhnw.oop.switzerland.base.*;
import ch.fhnw.oop.splitflap.*;
import org.jdesktop.layout.GroupLayout;
import java.awt.*;

public class NumberPanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7488160051698522748L;
	private SplitFlap splitFlap1;
    private SplitFlap splitFlap2;
    private SplitFlap splitFlap3;
    private SplitFlap splitFlap4;
    private SplitFlap splitFlap5;
    private SplitFlap splitFlap6;
    private SplitFlap splitFlap7;
    private SplitFlap splitFlap8;
    private SplitFlap splitFlap9;
    private SplitFlap splitFlap10;
    private String destination;
    private SplitFlap[] splitFlapArray;
    private float alpha;
    
    public NumberPanel(final PresentationState presentationState) {
        super();
        this.destination = "          ";
        this.splitFlapArray = new SplitFlap[10];
        this.alpha = 0.2f;
        this.initComponents();
        this.init();
        presentationState.addPropertyChangeListener("longRunningTaskMessage", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getNewValue() == null) {
                    AbstractApplicationFrame.fadeIn(NumberPanel.this);
                }
            }
        });
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
        this.repaint();
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.SrcOver.derive(this.alpha));
        super.paintComponent(g2);
    }
    
    private void init() {
        this.splitFlapArray[0] = this.splitFlap1;
        this.splitFlapArray[1] = this.splitFlap2;
        this.splitFlapArray[2] = this.splitFlap3;
        this.splitFlapArray[3] = this.splitFlap4;
        this.splitFlapArray[4] = this.splitFlap5;
        this.splitFlapArray[5] = this.splitFlap6;
        this.splitFlapArray[6] = this.splitFlap7;
        this.splitFlapArray[7] = this.splitFlap8;
        this.splitFlapArray[8] = this.splitFlap9;
        this.splitFlapArray[9] = this.splitFlap10;
        for (final SplitFlap splitFlap : this.splitFlapArray) {
            splitFlap.setSelection(SplitFlap.NUMERIC);
        }
        GlobalTimer.INSTANCE.startTimer();
    }
    
    public String getNumber() {
        return this.destination;
    }
    
    public void setNumber(final String DESTINATION) {
        this.destination = DESTINATION;
        if (DESTINATION.length() > 10) {
            this.destination = "----------";
        }
        this.flipIt();
    }
    
    private void flipIt() {
        for (int i = 0; i < 10; ++i) {
            if (i >= this.destination.length()) {
                this.splitFlapArray[i].setText(" ");
            }
            else {
                this.splitFlapArray[i].setText(this.destination.toUpperCase().substring(i, i + 1));
            }
        }
    }
    
    private void initComponents() {
        this.splitFlap1 = new SplitFlap();
        this.splitFlap2 = new SplitFlap();
        this.splitFlap3 = new SplitFlap();
        this.splitFlap4 = new SplitFlap();
        this.splitFlap5 = new SplitFlap();
        this.splitFlap6 = new SplitFlap();
        this.splitFlap7 = new SplitFlap();
        this.splitFlap8 = new SplitFlap();
        this.splitFlap9 = new SplitFlap();
        this.splitFlap10 = new SplitFlap();
        this.setBackground(new Color(51, 51, 51));
        this.setPreferredSize(new Dimension(360, 20));
        this.setSize(new Dimension(360, 20));
        this.splitFlap1.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap1Layout = new GroupLayout(this.splitFlap1);
        this.splitFlap1.setLayout(splitFlap1Layout);
        splitFlap1Layout.setHorizontalGroup(splitFlap1Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap1Layout.setVerticalGroup(splitFlap1Layout.createParallelGroup(1).add(0, 0, 32767));
        this.splitFlap2.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap2Layout = new GroupLayout(this.splitFlap2);
        this.splitFlap2.setLayout(splitFlap2Layout);
        splitFlap2Layout.setHorizontalGroup(splitFlap2Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap2Layout.setVerticalGroup(splitFlap2Layout.createParallelGroup(1).add(0, 0, 32767));
        this.splitFlap3.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap3Layout = new GroupLayout(this.splitFlap3);
        this.splitFlap3.setLayout(splitFlap3Layout);
        splitFlap3Layout.setHorizontalGroup(splitFlap3Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap3Layout.setVerticalGroup(splitFlap3Layout.createParallelGroup(1).add(0, 0, 32767));
        this.splitFlap4.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap4Layout = new GroupLayout(this.splitFlap4);
        this.splitFlap4.setLayout(splitFlap4Layout);
        splitFlap4Layout.setHorizontalGroup(splitFlap4Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap4Layout.setVerticalGroup(splitFlap4Layout.createParallelGroup(1).add(0, 0, 32767));
        this.splitFlap5.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap5Layout = new GroupLayout(this.splitFlap5);
        this.splitFlap5.setLayout(splitFlap5Layout);
        splitFlap5Layout.setHorizontalGroup(splitFlap5Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap5Layout.setVerticalGroup(splitFlap5Layout.createParallelGroup(1).add(0, 0, 32767));
        this.splitFlap6.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap6Layout = new GroupLayout(this.splitFlap6);
        this.splitFlap6.setLayout(splitFlap6Layout);
        splitFlap6Layout.setHorizontalGroup(splitFlap6Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap6Layout.setVerticalGroup(splitFlap6Layout.createParallelGroup(1).add(0, 0, 32767));
        this.splitFlap7.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap7Layout = new GroupLayout(this.splitFlap7);
        this.splitFlap7.setLayout(splitFlap7Layout);
        splitFlap7Layout.setHorizontalGroup(splitFlap7Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap7Layout.setVerticalGroup(splitFlap7Layout.createParallelGroup(1).add(0, 54, 32767));
        this.splitFlap8.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap8Layout = new GroupLayout(this.splitFlap8);
        this.splitFlap8.setLayout(splitFlap8Layout);
        splitFlap8Layout.setHorizontalGroup(splitFlap8Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap8Layout.setVerticalGroup(splitFlap8Layout.createParallelGroup(1).add(0, 0, 32767));
        this.splitFlap9.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap9Layout = new GroupLayout(this.splitFlap9);
        this.splitFlap9.setLayout(splitFlap9Layout);
        splitFlap9Layout.setHorizontalGroup(splitFlap9Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap9Layout.setVerticalGroup(splitFlap9Layout.createParallelGroup(1).add(0, 0, 32767));
        this.splitFlap10.setFontColor(new Color(255, 255, 255));
        final GroupLayout splitFlap10Layout = new GroupLayout(this.splitFlap10);
        this.splitFlap10.setLayout(splitFlap10Layout);
        splitFlap10Layout.setHorizontalGroup(splitFlap10Layout.createParallelGroup(1).add(0, 36, 32767));
        splitFlap10Layout.setVerticalGroup(splitFlap10Layout.createParallelGroup(1).add(0, 54, 32767));
        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().add(0, 0, 0).add(this.splitFlap1, -2, -1, -2).add(0, 0, 0).add(this.splitFlap2, -2, -1, -2).add(0, 0, 0).add(this.splitFlap3, -2, -1, -2).add(0, 0, 0).add(this.splitFlap4, -2, -1, -2).add(0, 0, 0).add(this.splitFlap5, -2, -1, -2).add(0, 0, 0).add(this.splitFlap6, -2, -1, -2).add(0, 0, 0).add(this.splitFlap7, -2, -1, -2).add(0, 0, 0).add(this.splitFlap8, -2, -1, -2).add(0, 0, 0).add(this.splitFlap9, -2, -1, -2).add(0, 0, 0).add(this.splitFlap10, -2, -1, -2).add(0, 0, 0)));
        layout.setVerticalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().add(0, 0, 0).add(layout.createParallelGroup(1).add(layout.createParallelGroup(1, false).add(this.splitFlap9, -1, -1, 32767).add(this.splitFlap8, -1, -1, 32767).add(this.splitFlap7, -1, -1, 32767).add(this.splitFlap6, -1, -1, 32767).add(this.splitFlap5, -1, -1, 32767).add(this.splitFlap4, -1, -1, 32767).add(this.splitFlap3, -1, -1, 32767).add(this.splitFlap2, -1, -1, 32767).add(this.splitFlap1, -1, -1, 32767)).add(this.splitFlap10, -2, -1, -2)).add(0, 0, 0)));
    }
}
