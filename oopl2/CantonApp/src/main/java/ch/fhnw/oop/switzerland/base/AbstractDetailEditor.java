package ch.fhnw.oop.switzerland.base;

import net.miginfocom.swing.*;

import java.beans.*;
import java.util.*;
import java.util.List;

import javax.swing.text.*;

import java.awt.event.*;
import java.awt.*;
import java.text.*;

import javax.swing.event.*;

import java.net.*;

import javax.swing.*;

public abstract class AbstractDetailEditor extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2550590337474446442L;
	private final Observable masterDetailModel;
    private final String selectedModelProperty;
    private final AbstractMasterDetailController controller;
    private final List<Binding> bindings;
    private float alpha;
    
    public AbstractDetailEditor(final Observable masterDetailModel, final String selectedModelProperty, final AbstractMasterDetailController controller) {
        super();
        this.bindings = new ArrayList<Binding>();
        this.alpha = 0.2f;
        this.masterDetailModel = masterDetailModel;
        this.selectedModelProperty = selectedModelProperty;
        this.controller = controller;
        this.setLayout(new MigLayout("wrap 4", "30[30::100, shrink, fill, shrinkprio 0, sg label]15[80::300, grow, fill, shrinkprio 1, sg field]70[30::100, shrink, shrinkprio 0, sg label]15[80::300, grow, fill, shrinkprio 1, sg field]30", "20[]"));
        this.initializeComponents();
        this.layoutComponents();
        this.initializeBindings();
        this.addSelectedModelListener();
        controller.getPresentationState().addPropertyChangeListener("longRunningTaskMessage", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getNewValue() == null) {
                    AbstractApplicationFrame.fadeIn(AbstractDetailEditor.this);
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
    
    protected Observable getApplicationModel() {
        return this.masterDetailModel;
    }
    
    protected AbstractMasterDetailController getController() {
        return this.controller;
    }
    
    protected abstract void initializeComponents();
    
    protected abstract void layoutComponents();
    
    protected abstract void initializeBindings();
    
    private void addSelectedModelListener() {
        this.masterDetailModel.addPropertyChangeListener(this.selectedModelProperty, new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final Observable oldSelection = (Observable)evt.getOldValue();
                final Observable newSelection = (Observable)evt.getNewValue();
                if (oldSelection != null) {
                    for (final Binding binding : AbstractDetailEditor.this.bindings) {
                        oldSelection.removePropertyChangeListener(binding.getPropertyName(), binding.getListener());
                    }
                }
                if (newSelection != null) {
                    for (final Binding binding : AbstractDetailEditor.this.bindings) {
                        newSelection.addPropertyChangeListener(binding.getPropertyName(), binding.getListener());
                        binding.getComponent().setEnabled(true);
                    }
                }
                else {
                    for (final Binding binding : AbstractDetailEditor.this.bindings) {
                        AbstractDetailEditor.this.handleEmptySelection(binding.getComponent());
                    }
                }
            }
        });
    }
    
    protected void handleEmptySelection(final JComponent component) {
        component.setEnabled(false);
        if (component instanceof JTextComponent) {
            ((JTextComponent)component).setText("-");
        }
        else if (component instanceof JLabel) {
            ((JLabel)component).setIcon(null);
            ((JLabel)component).setText("-");
        }
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(AlphaComposite.SrcOver.derive(this.alpha));
        super.paintComponent(g2);
    }
    
    protected final void bind(final JTextComponent field, final String propertyName, final Converter converter) {
        field.addKeyListener(new MyKeyListener(propertyName, converter));
        this.bindings.add(new Binding(new FieldUpdater(field, converter), field, propertyName));
    }
    
    protected final void bind(final JLabel label, final String propertyName) {
        this.bind(label, propertyName, new NullConverter());
    }
    
    protected final void bind(final JLabel label, final String propertyName, final Converter converter) {
        this.bindings.add(new Binding(new LabelUpdater(label, converter), label, propertyName));
    }
    
    protected void bind(final JSpinner field, final String propertyName) {
        final JFormattedTextField textField = ((JSpinner.DefaultEditor)field.getEditor()).getTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                final String newUserInput = textField.getText();
                try {
                    final DecimalFormat format = ((JSpinner.NumberEditor)field.getEditor()).getFormat();
                    final ParsePosition parsePosition = new ParsePosition(0);
                    final Number number = format.parse(newUserInput, parsePosition);
                    if (number == null || parsePosition.getIndex() != newUserInput.length() || number.longValue() > 2147483647L) {
                        throw new ParseException("Unparseable number: \"" + newUserInput + "\"", parsePosition.getErrorIndex());
                    }
                    final Integer newValue = number.intValue();
                    final int caretPosition = textField.getCaretPosition();
                    int numberCount = 0;
                    for (int i = 0; i < caretPosition; ++i) {
                        if (newUserInput.charAt(i) != '\'') {
                            ++numberCount;
                        }
                    }
                    textField.setForeground(Color.black);
                    if (!field.getValue().equals(newValue)) {
                        field.setValue(newValue);
                        int newCaretPosition = numberCount;
                        final String newText = textField.getText();
                        for (int j = 0; j < numberCount; ++j) {
                            if (newText.charAt(j) == '\'') {
                                ++newCaretPosition;
                            }
                        }
                        newCaretPosition = Math.min(newCaretPosition, newText.length());
                        textField.setCaretPosition(newCaretPosition);
                    }
                }
                catch (ParseException e2) {
                    textField.setForeground(Color.red);
                }
            }
        });
        field.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                AbstractDetailEditor.this.controller.setValueOnSelectedModel(propertyName, field.getValue());
            }
        });
        this.bindings.add(new Binding(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final Object newValue = evt.getNewValue();
                if (field.getValue() != newValue) {
                    field.setValue(newValue);
                }
            }
        }, field, propertyName));
    }
    
    protected final void addBinding(final Binding binding) {
        this.bindings.add(binding);
    }
    
    private class MyKeyListener extends KeyAdapter
    {
        private final String propertyName;
        private Converter<Object> converter;
        
        public MyKeyListener(final String propertyName, final Converter<Object> converter) {
            super();
            this.propertyName = propertyName;
            this.converter = converter;
        }
        
        @Override
        public void keyReleased(final KeyEvent event) {
            final JTextComponent textField = (JTextComponent)event.getSource();
            try {
                final Object newValue = this.converter.toData(textField.getText());
                textField.setForeground(Color.black);
                AbstractDetailEditor.this.controller.setValueOnSelectedModel(this.propertyName, newValue);
            }
            catch (ParseException e) {
                textField.setForeground(Color.red);
            }
        }
    }
    
    private class FieldUpdater implements PropertyChangeListener
    {
        private final JTextComponent field;
        private Converter<Object> converter;
        
        public FieldUpdater(final JTextComponent field, final Converter<Object> converter) {
            super();
            this.field = field;
            this.converter = converter;
        }
        
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            final String newUserInput = this.field.getText();
            final int caretPosition = this.field.getCaretPosition();
            int numberCount = 0;
            for (int i = 0; i < caretPosition; ++i) {
                if (newUserInput.charAt(i) != '\'') {
                    ++numberCount;
                }
            }
            final String newText = this.converter.toString(evt.getNewValue());
            this.field.setText(newText);
            int newCaretPosition = numberCount;
            for (int j = 0; j < numberCount; ++j) {
                if (j < newText.length() && newText.charAt(j) == '\'') {
                    ++newCaretPosition;
                }
            }
            newCaretPosition = Math.min(newCaretPosition, newText.length());
            this.field.setCaretPosition(newCaretPosition);
        }
    }
    
    private class LabelUpdater implements PropertyChangeListener
    {
        private final JLabel label;
        private Converter<Object> converter;
        
        public LabelUpdater(final JLabel label, final Converter<Object> converter) {
            super();
            this.label = label;
            this.converter = converter;
        }
        
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            final Object newValue = evt.getNewValue();
            if (newValue instanceof URL) {
                this.label.setText(null);
                this.label.setIcon(new ImageIcon((URL)newValue));
            }
            else {
                this.label.setText(this.converter.toString(newValue));
            }
        }
    }
    
    protected class Binding
    {
        private final PropertyChangeListener listener;
        private final JComponent component;
        private final String propertyName;
        
        public Binding(final PropertyChangeListener listener, final JComponent component, final String propertyName) {
            super();
            this.listener = listener;
            this.component = component;
            this.propertyName = propertyName;
        }
        
        private PropertyChangeListener getListener() {
            return this.listener;
        }
        
        private JComponent getComponent() {
            return this.component;
        }
        
        private String getPropertyName() {
            return this.propertyName;
        }
    }
}
