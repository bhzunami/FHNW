package ch.fhnw.oop.switzerland.base;

import javax.swing.*;
import java.lang.reflect.*;
import java.beans.*;

public class AbstractModel implements Observable
{
    private final PropertyChangeSupport pcs;
    
    public AbstractModel() {
        super();
        this.pcs = new PropertyChangeSupport(this);
    }
    
    public Object getProperty(final String propertyName) {
        return this.getValue(this.getField(propertyName));
    }
    
    public void setProperty(final String propertyName, final Object newValue) {
        final Field field = this.getField(propertyName);
        final Object oldValue = this.getValue(field);
        if (oldValue != newValue) {
            this.setValue(field, newValue);
            if (SwingUtilities.isEventDispatchThread()) {
                this.pcs.firePropertyChange(propertyName, oldValue, newValue);
            }
            else {
                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            AbstractModel.this.pcs.firePropertyChange(propertyName, oldValue, newValue);
                        }
                    });
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(propertyName, listener);
        final Object value = this.getProperty(propertyName);
        listener.propertyChange(new PropertyChangeEvent(this, propertyName, value, value));
    }
    
    @Override
    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(propertyName, listener);
    }
    
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
        listener.propertyChange(new PropertyChangeEvent(this, null, null, null));
    }
    
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    
    private Object getValue(final Field field) {
        field.setAccessible(true);
        try {
            return field.get(this);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("can't access value of field " + field + " in class " + this.getClass().getName());
        }
    }
    
    private void setValue(final Field field, final Object newValue) {
        field.setAccessible(true);
        try {
            field.set(this, newValue);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("can't access value of field " + field + " in class " + this.getClass().getName());
        }
    }
    
    private Field getField(final String propertyName) {
        try {
            return this.getClass().getDeclaredField(propertyName);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException("unknown field " + propertyName + " in class " + this.getClass().getName());
        }
    }
}
