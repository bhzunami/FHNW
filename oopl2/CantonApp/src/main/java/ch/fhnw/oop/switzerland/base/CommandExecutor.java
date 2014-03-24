package ch.fhnw.oop.switzerland.base;

import java.util.*;

public class CommandExecutor extends AbstractModel
{
    public static final String NEXT_UNDO_COMMAND_PROPERTY = "nextUndoCommand";
    public static final String NEXT_REDO_COMMAND_PROPERTY = "nextRedoCommand";
    private final Deque<Command> undoStack;
    private final Deque<Command> redoStack;
    private Command nextUndoCommand;
    private Command nextRedoCommand;
    
    public CommandExecutor() {
        super();
        this.undoStack = new ArrayDeque<Command>();
        this.redoStack = new ArrayDeque<Command>();
    }
    
    public void execute(final Command cmd) {
        cmd.execute();
        if (cmd.isUndoable()) {
            this.undoStack.push(cmd);
        }
        else {
            this.undoStack.clear();
        }
        this.redoStack.clear();
        this.updateProperties();
    }
    
    public void undo() {
        if (this.undoStack.isEmpty()) {
            return;
        }
        final Command cmd = this.undoStack.pop();
        cmd.undo();
        this.redoStack.push(cmd);
        this.updateProperties();
    }
    
    public void redo() {
        if (this.redoStack.isEmpty()) {
            return;
        }
        final Command cmd = this.redoStack.pop();
        cmd.execute();
        this.undoStack.push(cmd);
        this.updateProperties();
    }
    
    public void clearStacks() {
        this.undoStack.clear();
        this.redoStack.clear();
        this.updateProperties();
    }
    
    private void updateProperties() {
        this.setProperty("nextUndoCommand", this.top(this.undoStack));
        this.setProperty("nextRedoCommand", this.top(this.redoStack));
    }
    
    private Command top(final Deque<Command> stack) {
        return stack.isEmpty() ? null : stack.peek();
    }
}
