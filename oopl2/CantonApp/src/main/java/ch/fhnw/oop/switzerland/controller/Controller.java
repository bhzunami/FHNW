package ch.fhnw.oop.switzerland.controller;

import ch.fhnw.oop.switzerland.base.*;
import ch.fhnw.oop.switzerland.model.*;
import java.util.*;

public class Controller extends AbstractMasterDetailController
{
    public Controller(final Switzerland model) {
        super(model, "cantons", "selectedCanton");
    }
    
    @Override
    public void setSelectedModel(final AbstractModel model) {
        super.setSelectedModel(model);
        if (model instanceof Canton && !((Canton)model).containsText((String)this.getModel().getProperty("searchString"))) {
            this.getModel().setProperty("searchString", "");
        }
    }
    
    public List<Canton> loadData() {
        return this.getModel().loadCantons();
    }
    
    public void searchNext(final String text) {
        final Canton canton = this.getModel().searchNext(text);
        this.setSelectedModel(canton);
    }
    
    public void save() {
        this.getModel().save();
    }
    
    private Switzerland getModel() {
        return (Switzerland)this.getMasterDetailModel();
    }
}
