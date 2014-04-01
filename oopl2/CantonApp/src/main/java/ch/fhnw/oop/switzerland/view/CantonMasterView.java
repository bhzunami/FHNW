package ch.fhnw.oop.switzerland.view;

import ch.fhnw.oop.switzerland.model.*;
import ch.fhnw.oop.switzerland.base.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class CantonMasterView extends AbstractMasterTable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int NAME_COL = 0;
    private static final int SHORT_NAME_COL = 1;
    private static final int SINCE_COL = 2;
    private static final int POPULATION_COL = 3;
    private static final int AREA_COL = 4;
    
    public CantonMasterView(final Switzerland applicationModel, final AbstractMasterDetailController controller) {
        super(applicationModel, "selectedCanton", "cantons", controller);
    }
    
    @Override
    public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column) {
        final JLabel c = (JLabel)super.prepareRenderer(renderer, row, column);
        if (column == 1 || column == 2) {
            c.setHorizontalAlignment(0);
        }
        else if (column == 0) {
            c.setHorizontalAlignment(2);
        }
        return c;
    }
    
    @Override
    protected void adjustColumns() {
        TableColumn col = this.getColumnModel().getColumn(0);
        col.setPreferredWidth(170);
        col.setMaxWidth(2000);
        col.setMinWidth(70);
        col = this.getColumnModel().getColumn(1);
        col.setPreferredWidth(60);
        col.setMaxWidth(80);
        col.setMinWidth(40);
        col = this.getColumnModel().getColumn(2);
        col.setPreferredWidth(60);
        col.setMaxWidth(80);
        col.setMinWidth(40);
        col = this.getColumnModel().getColumn(3);
        col.setPreferredWidth(80);
        col.setMaxWidth(120);
        col.setMinWidth(40);
        col = this.getColumnModel().getColumn(4);
        col.setPreferredWidth(60);
        col.setMaxWidth(100);
        col.setMinWidth(40);
    }
    
    @Override
    protected void initializeBindings() {
        this.bind(0, "name", "Kanton", true, String.class);
        this.bind(1, "shortName", "K\u00fcrzel", true, String.class);
        this.bind(2, "since", "Beitritt", true, String.class);
        this.bind(3, "population", "Einwohner", true, Integer.class);
        this.bind(4, "area", "Fl\u00e4che", true, Float.class);
    }
}
