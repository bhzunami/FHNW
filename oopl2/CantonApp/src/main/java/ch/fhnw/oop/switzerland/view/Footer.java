package ch.fhnw.oop.switzerland.view;

import ch.fhnw.oop.led.*;
import ch.fhnw.oop.switzerland.controller.*;
import ch.fhnw.oop.switzerland.model.*;
import ch.fhnw.oop.splitflap.*;

import java.net.*;

import net.miginfocom.swing.*;

import java.beans.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import ch.fhnw.oop.switzerland.base.*;

public class Footer extends AbstractDetailEditor
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5347396469960906257L;
	private JLabel swissFlag;
    private JLabel populationLabel;
    private JLabel areaLabel;
    private NumberPanel populationPanel;
    private NumberPanel areaPanel;
    private List<Led> leds;
    private JPanel ledPanel;
    
    public Footer(final Switzerland applicationModel, final Controller controller) {
        super(applicationModel, "selectedCanton", controller);
    }
    
    @Override
    protected void initializeComponents() {
        final URL flagFile = Canton.class.getResource("wappen/switzerland.png");
        this.swissFlag = new JLabel(new ImageIcon(flagFile));
        this.populationLabel = new JLabel("Einwohner");
        final Font font = FlipImages.INSTANCE.getFont().deriveFont(1, 28.0f);
        this.populationLabel.setFont(font);
        this.populationLabel.setForeground(Color.white);
        (this.areaLabel = new JLabel("<html>km<font size = 6><sup>2</sup></font></html>")).setFont(font);
        this.areaLabel.setForeground(Color.white);
        final PresentationState presentationState = this.getController().getPresentationState();
        this.populationPanel = new NumberPanel(presentationState);
        this.areaPanel = new NumberPanel(presentationState);
        this.ledPanel = new JPanel();
        this.leds = new ArrayList<Led>();
    }
    
    @Override
    protected void layoutComponents() {
        this.ledPanel.setOpaque(false);
        this.ledPanel.setBackground(new Color(51, 51, 51));
        this.ledPanel.setLayout(new MigLayout("insets 0 0 5 0", "grow, fill, center"));
        this.setBackground(new Color(51, 51, 51));
        this.setLayout(new MigLayout("insets 10 10 10 10, wrap 3", "[grow, left]40[right]20[right]", "[center][center][top]"));
        this.add(this.ledPanel, "span 3, grow");
        this.add(this.swissFlag, "span 1 2, aligny bottom");
        this.add(this.populationPanel);
        this.add(this.populationLabel);
        this.add(this.areaPanel);
        this.add(this.areaLabel);
    }
    
    @Override
    protected void initializeBindings() {
        this.bindPopulation(this.populationPanel);
        this.bindArea(this.areaPanel);
        this.getApplicationModel().addPropertyChangeListener("selectedCanton", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                for (final Led l : Footer.this.leds) {
                    l.setOn(false);
                }
                final Canton newValue = (Canton)evt.getNewValue();
                if (newValue != null) {
                    final Switzerland model = (Switzerland)evt.getSource();
                    final int index = model.getCantons().indexOf(newValue);
                    Footer.this.leds.get(index).setOn(true);
                }
            }
        });
        this.getApplicationModel().addPropertyChangeListener("cantons", new PropertyChangeListener() {
            private void addEventListeners() {
                final List<Canton> cantons = Footer.this.getSwitzerland().getCantons();
                for (int i = 0; i < Footer.this.leds.size(); ++i) {
                    final Canton c = cantons.get(i);
                    Footer.this.leds.get(i).addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(final MouseEvent e) {
                            Footer.this.getController().setSelectedModel(c);
                        }
                    });
                }
            }
            
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final int numberOfCantons = Footer.this.getSwitzerland().getCantons().size();
                if (numberOfCantons > 0) {
                    for (final Led l : Footer.this.leds) {
                        Footer.this.ledPanel.remove(l);
                    }
                    final Dimension ledSize = new Dimension(20, 20);
                    for (int i = 0; i < numberOfCantons; ++i) {
                        final Led led = new Led();
                        led.setPreferredSize(ledSize);
                        led.setMaximumSize(ledSize);
                        led.setColor(Color.green);
                        Footer.this.leds.add(led);
                    }
                    for (final Led j : Footer.this.leds) {
                        Footer.this.ledPanel.add(j);
                    }
                    final List<Canton> cantons = Footer.this.getSwitzerland().getCantons();
                    for (int k = 0; k < cantons.size(); ++k) {
                        final Led led2 = Footer.this.leds.get(k);
                        cantons.get(k).addPropertyChangeListener("isDirty", new PropertyChangeListener() {
                            @Override
                            public void propertyChange(final PropertyChangeEvent evt) {
                                led2.setColor(((boolean)evt.getNewValue()) ? Color.red : Color.green);
                            }
                        });
                    }
                    this.addEventListeners();
                    Footer.this.populationPanel.setNumber(Footer.this.getTotalPopulation());
                    Footer.this.areaPanel.setNumber(Footer.this.getTotalArea());
                }
            }
        });
    }
    
    @Override
    protected void handleEmptySelection(final JComponent component) {
    }
    
    private void bindPopulation(final NumberPanel panel) {
        this.addBinding(new Binding(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final String totalPopulation = Footer.this.getTotalPopulation();
                if (totalPopulation.equals(panel.getNumber())) {
                    return;
                }
                panel.setNumber(totalPopulation);
            }
        }, panel, "population"));
    }
    
    private void bindArea(final NumberPanel panel) {
        this.addBinding(new Binding(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final String totalArea = Footer.this.getTotalArea();
                if (totalArea.equals(panel.getNumber())) {
                    return;
                }
                panel.setNumber(totalArea);
            }
        }, panel, "area"));
    }
    
    private String getTotalPopulation() {
        final long totalPopulation = this.getSwitzerland().getTotalPopulation();
        return this.addLeadingBlanks(new LongConverter().toString(Long.valueOf(totalPopulation)));
    }
    
    private String getTotalArea() {
        final double totalArea = this.getSwitzerland().getTotalArea();
        return this.addLeadingBlanks(new DoubleConverter(1, 1).toString(Double.valueOf(totalArea)));
    }
    
    private String addLeadingBlanks(String str) {
        for (int blanksNeeded = 10 - str.length(), i = 0; i < blanksNeeded; ++i) {
            str = " " + str;
        }
        return str;
    }
    
    private Switzerland getSwitzerland() {
        return (Switzerland)this.getApplicationModel();
    }
}
