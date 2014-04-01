package ch.fhnw.oop.switzerland.base;

public interface Command
{
    void execute();
    
    void undo();
    
    boolean isUndoable();
    
    String getName();
}
