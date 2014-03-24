package ch.fhnw.oop.switzerland.base;

import java.util.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.concurrent.*;

public abstract class AbstractMasterDetailController
{
    private final CommandExecutor executor;
    private final AbstractModel masterDetailModel;
    private final String selectedModelProperty;
    private final String allModelsProperty;
    private final PresentationState presentationState;
    
    public AbstractMasterDetailController(final AbstractModel masterDetailModel, final String allModelsProperty, final String selectedModelProperty) {
        super();
        this.executor = new CommandExecutor();
        this.masterDetailModel = masterDetailModel;
        this.allModelsProperty = allModelsProperty;
        this.selectedModelProperty = selectedModelProperty;
        this.presentationState = new PresentationState();
    }
    
    protected AbstractModel getMasterDetailModel() {
        return this.masterDetailModel;
    }
    
    public CommandExecutor getCommandExecutor() {
        return this.executor;
    }
    
    public synchronized void setValueOnSelectedModel(final String propertyName, final Object newValue) {
        final AbstractModel selectedModel = (AbstractModel)this.masterDetailModel.getProperty(this.selectedModelProperty);
        if (selectedModel != null) {
            final Object oldValue = selectedModel.getProperty(propertyName);
            if ((oldValue == null && newValue != null) || (oldValue != null && !oldValue.equals(newValue))) {
                this.executor.execute(new SetValueCommand(selectedModel, propertyName, newValue));
            }
        }
    }
    
    public synchronized void setSelectedModel(final AbstractModel model) {
        final Object oldValue = this.masterDetailModel.getProperty(this.selectedModelProperty);
        if ((oldValue == null && model != null) || (oldValue != null && !oldValue.equals(model))) {
            this.executor.execute(new SetValueCommand(this.masterDetailModel, this.selectedModelProperty, model));
        }
    }
    
    public final void startup(final AbstractApplicationFrame view) {
        final SwingWorker<List<? extends AbstractModel>, Object> loadDataTask = new SwingWorker<List<? extends AbstractModel>, Object>() {
            @Override
            protected List<? extends AbstractModel> doInBackground() throws Exception {
                AbstractMasterDetailController.this.presentationState.setProperty("longRunningTaskMessage", "Daten werden geladen");
                return AbstractMasterDetailController.this.loadData();
            }
            
            @Override
            protected void done() {
                AbstractMasterDetailController.this.presentationState.setProperty("longRunningTaskMessage", null);
            }
        };
        loadDataTask.execute();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    view.createAndShow();
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
        	e.printStackTrace();
			throw new IllegalStateException("Can't create and show the ui");
        }
        try {
            final List<? extends AbstractModel> allModels = loadDataTask.get();
            this.masterDetailModel.setProperty(this.allModelsProperty, allModels);
            this.executor.clearStacks();
            if (!allModels.isEmpty()) {
                this.masterDetailModel.setProperty(this.selectedModelProperty, allModels.get(0));
            }
        }
        catch (ExecutionException | InterruptedException e) {
            throw new IllegalStateException("can't get result form loadDataTask");
        }
    }
    
    public void undo() {
        this.executor.undo();
    }
    
    public void redo() {
        this.executor.redo();
    }
    
    protected abstract List<? extends AbstractModel> loadData();
    
    public PresentationState getPresentationState() {
        return this.presentationState;
    }
}
