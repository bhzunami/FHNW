package ch.fhnw.oop.switzerland.view;

import ch.fhnw.oop.switzerland.model.*;
import ch.fhnw.oop.switzerland.controller.*;
import ch.fhnw.oop.switzerland.base.*;
import javax.swing.*;
import net.miginfocom.swing.*;
import java.awt.*;

public class SwitzerlandFrame extends AbstractApplicationFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -741336710052135358L;
	private JToolBar toolBar;
    private JComponent table;
    private JComponent editor;
    private JComponent footer;
    
    public SwitzerlandFrame(final Switzerland model, final Controller controller) {
        super(model, controller);
        this.setTitle("Schweizer Kantone");
    }
    
    @Override
    protected void initializeComponents() {
        final Switzerland model = (Switzerland)this.getApplicationModel();
        final Controller controller = (Controller)this.getController();
        this.toolBar = new Toolbar(model, controller);
        this.table = new CantonMasterView(model, controller);
        this.editor = new CantonDetailView(model, controller);
        this.footer = new Footer(model, controller);
    }
    
    @Override
    protected JComponent layoutComponents() {
        final JScrollPane scrollPane = new JScrollPane(this.table) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 5920536569809799182L;

			@Override
            protected void paintComponent(final Graphics g) {
                final Graphics2D g2 = (Graphics2D)g;
                g2.setComposite(AlphaComposite.SrcOver.derive(SwitzerlandFrame.this.getAlpha()));
                super.paintComponent(g2);
            }
        };
        scrollPane.setMinimumSize(new Dimension(250, 100));
        final JSplitPane splitPane = new JSplitPane(1, scrollPane, this.editor);
        splitPane.setContinuousLayout(true);
        final JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new MigLayout("", "", ""));
        rootPanel.add(this.toolBar, "dock north");
        rootPanel.add(this.footer, "dock south");
        rootPanel.add(splitPane, "dock center");
        return rootPanel;
    }
    
    @Override
    protected void addEventListeners() {
    }
    
    @Override
    protected void addObservers() {
    }
}
