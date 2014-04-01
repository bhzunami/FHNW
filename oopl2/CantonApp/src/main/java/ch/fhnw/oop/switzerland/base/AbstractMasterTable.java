package ch.fhnw.oop.switzerland.base;

import java.beans.*;

import javax.swing.event.*;
import javax.swing.*;

import java.util.*;
import java.util.List;
import java.awt.*;

import javax.swing.table.*;

public abstract class AbstractMasterTable extends JTable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8483531052137517667L;
	private final AbstractModel masterDetailModel;
    private final String selectedModelProperty;
    private String allModelsProperty;
    private final AbstractMasterDetailController controller;
    private DataAdapter tableModel;
    private final List<Binding> bindings;
    private List<? extends AbstractModel> allModels;
    private float alpha;
    private final PropertyChangeListener tableUpdater;
    
    public AbstractMasterTable(final AbstractModel masterDetailModel, final String selectedModelProperty, final String allModelsProperty, final AbstractMasterDetailController controller) {
        super();
        this.bindings = new ArrayList<Binding>();
        this.alpha = 0.2f;
        this.tableUpdater = new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final int rowToUpdate = AbstractMasterTable.this.allModels.indexOf(evt.getSource());
                final String changedProperty = evt.getPropertyName();
                if (changedProperty != null) {
                    final int colToUpdate = AbstractMasterTable.this.getColumnIndex(changedProperty);
                    if (colToUpdate >= 0) {
                        AbstractMasterTable.this.tableModel.fireTableCellUpdated(rowToUpdate, colToUpdate);
                    }
                }
                else {
                    AbstractMasterTable.this.tableModel.fireTableRowsUpdated(rowToUpdate, rowToUpdate);
                }
            }
        };
        this.masterDetailModel = masterDetailModel;
        this.selectedModelProperty = selectedModelProperty;
        this.allModelsProperty = allModelsProperty;
        this.controller = controller;
        this.setSelectionMode(0);
        this.setCellSelectionEnabled(false);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(true);
        this.initializeBindings();
        this.allModels = new ArrayList<AbstractModel>();
        this.setModel(this.tableModel = new DataAdapter(this.allModels));
        this.adjustColumns();
        this.addActions();
        this.addPropertyChangeListeners();
    }
    
    protected AbstractModel getApplicationModel() {
        return this.masterDetailModel;
    }
    
    protected void adjustColumns() {
        for (int cols = this.getColumnCount(), i = 0; i < cols; ++i) {
            final TableColumn col = this.getColumnModel().getColumn(i);
            final Class colType = this.type(i);
            if (Integer.class.equals(colType)) {
                col.setPreferredWidth(60);
                col.setMaxWidth(120);
                col.setMinWidth(20);
            }
        }
    }
    
    protected abstract void initializeBindings();
    
    protected final void bind(final int columnIndex, final String propertyName, final String columnHeader) {
        this.bindings.add(new Binding(columnIndex, propertyName, columnHeader, true, (Class)String.class));
    }
    
    protected final void bind(final int columnIndex, final String propertyName, final String columnHeader, final boolean isEditable, final Class type) {
        this.bindings.add(new Binding(columnIndex, propertyName, columnHeader, isEditable, type));
    }
    
    private void addPropertyChangeListeners() {
        this.masterDetailModel.addPropertyChangeListener(this.selectedModelProperty, new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final AbstractModel oldSelection = (AbstractModel)evt.getOldValue();
                final AbstractModel newSelection = (AbstractModel)evt.getNewValue();
                if (oldSelection != null) {
                    oldSelection.removePropertyChangeListener(AbstractMasterTable.this.tableUpdater);
                }
                if (newSelection != null) {
                    newSelection.addPropertyChangeListener(AbstractMasterTable.this.tableUpdater);
                    final int index = AbstractMasterTable.this.allModels.indexOf(newSelection);
                    AbstractMasterTable.this.getSelectionModel().setSelectionInterval(index, index);
                }
                else {
                    AbstractMasterTable.this.clearSelection();
                    AbstractMasterTable.this.tableModel.fireTableDataChanged();
                }
            }
        });
        this.masterDetailModel.addPropertyChangeListener(this.allModelsProperty, new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                AbstractMasterTable.this.allModels = (List<? extends AbstractModel>)evt.getNewValue();
                AbstractMasterTable.this.tableModel = new DataAdapter(AbstractMasterTable.this.allModels);
                AbstractMasterTable.this.setModel(AbstractMasterTable.this.tableModel);
                AbstractMasterTable.this.adjustColumns();
                AbstractMasterTable.this.requestFocusInWindow();
            }
        });
        this.controller.getPresentationState().addPropertyChangeListener("longRunningTaskMessage", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getNewValue() == null) {
                    AbstractApplicationFrame.fadeIn(AbstractMasterTable.this);
                }
            }
        });
    }
    
    private void addActions() {
        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(final ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                final int selectionIndex = ((ListSelectionModel)e.getSource()).getMinSelectionIndex();
                if (selectionIndex >= 0) {
                    AbstractMasterTable.this.controller.setSelectedModel(AbstractMasterTable.this.allModels.get(selectionIndex));
                }
                else {
                    AbstractMasterTable.this.controller.setSelectedModel(null);
                }
            }
        });
    }
    
    private boolean isEditable(final int columnIndex) {
        for (final Binding binding : this.bindings) {
            if (binding.getColumnIndex() == columnIndex) {
                return binding.isEditable();
            }
        }
        throw new IllegalStateException("unknown column " + columnIndex);
    }
    
    private String getColumnHeader(final int columnIndex) {
        for (final Binding binding : this.bindings) {
            if (binding.getColumnIndex() == columnIndex) {
                return binding.getColumnHeader();
            }
        }
        throw new IllegalStateException("unknown column " + columnIndex);
    }
    
    private String getPropertyName(final int columnIndex) {
        for (final Binding binding : this.bindings) {
            if (binding.getColumnIndex() == columnIndex) {
                return binding.getPropertyName();
            }
        }
        throw new IllegalStateException("unknown column " + columnIndex);
    }
    
    private int getColumnIndex(final String propertyName) {
        for (final Binding binding : this.bindings) {
            if (binding.getPropertyName().equals(propertyName)) {
                return binding.getColumnIndex();
            }
        }
        return -1;
    }
    
    private Class type(final int columnIndex) {
        for (final Binding binding : this.bindings) {
            if (binding.getColumnIndex() == columnIndex) {
                return binding.getType();
            }
        }
        throw new IllegalStateException("unknown column " + columnIndex);
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
        g2.setComposite(AlphaComposite.SrcOver.derive(this.getAlpha()));
        super.paintComponent(g2);
    }
    
    private class DataAdapter extends AbstractTableModel
    {
        private final List<? extends AbstractModel> allModels;
        
        public DataAdapter(final List<? extends AbstractModel> allModels) {
            super();
            this.allModels = allModels;
        }
        
        @Override
        public int getRowCount() {
            return this.allModels.size();
        }
        
        @Override
        public int getColumnCount() {
            return AbstractMasterTable.this.bindings.size();
        }
        
        @Override
        public String getColumnName(final int column) {
            return AbstractMasterTable.this.getColumnHeader(column);
        }
        
        @Override
        public Object getValueAt(final int row, final int column) {
            final AbstractModel model = (AbstractModel)this.allModels.get(row);
            return model.getProperty(AbstractMasterTable.this.getPropertyName(column));
        }
        
        @Override
        public void setValueAt(final Object newValue, final int rowIndex, final int columnIndex) {
            AbstractMasterTable.this.controller.setValueOnSelectedModel(AbstractMasterTable.this.getPropertyName(columnIndex), newValue);
        }
        
        @Override
        public boolean isCellEditable(final int rowIndex, final int columnIndex) {
            return AbstractMasterTable.this.isEditable(columnIndex);
        }
        
        @Override
        public Class<?> getColumnClass(final int columnIndex) {
            return (Class<?>)AbstractMasterTable.this.type(columnIndex);
        }
    }
    
    private class Binding
    {
        private final int columnIndex;
        private final String propertyName;
        private final String columnHeader;
        private final boolean editable;
        private final Class type;
        
        private Binding(final int columnIndex, final String propertyName, final String columnHeader, final boolean editable, final Class type) {
            super();
            this.columnIndex = columnIndex;
            this.propertyName = propertyName;
            this.columnHeader = columnHeader;
            this.editable = editable;
            this.type = type;
        }
        
        private boolean isEditable() {
            return this.editable;
        }
        
        private int getColumnIndex() {
            return this.columnIndex;
        }
        
        private String getPropertyName() {
            return this.propertyName;
        }
        
        private String getColumnHeader() {
            return this.columnHeader;
        }
        
        private Class getType() {
            return this.type;
        }
    }
}
