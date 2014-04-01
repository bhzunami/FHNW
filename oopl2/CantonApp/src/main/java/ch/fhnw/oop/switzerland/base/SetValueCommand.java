package ch.fhnw.oop.switzerland.base;

public class SetValueCommand implements Command
{
    private final AbstractModel model;
    private final String propertyName;
    private final Object newValue;
    private final Object oldValue;
    
    public SetValueCommand(final AbstractModel model, final String propertyName, final Object newValue) {
        super();
        this.propertyName = propertyName;
        this.oldValue = model.getProperty(propertyName);
        this.newValue = newValue;
        this.model = model;
    }
    
    @Override
    public String getName() {
        return "set to '" + this.newValue + "'";
    }
    
    @Override
    public void execute() {
        this.model.setProperty(this.propertyName, this.newValue);
    }
    
    @Override
    public void undo() {
        this.model.setProperty(this.propertyName, this.oldValue);
    }
    
    @Override
    public boolean isUndoable() {
        return true;
    }
}
