package ch.fhnw.oop.switzerland.base;

import java.beans.*;

public interface Observable
{
    void addPropertyChangeListener(PropertyChangeListener p0);
    
    void addPropertyChangeListener(String p0, PropertyChangeListener p1);
    
    void removePropertyChangeListener(PropertyChangeListener p0);
    
    void removePropertyChangeListener(String p0, PropertyChangeListener p1);
}
