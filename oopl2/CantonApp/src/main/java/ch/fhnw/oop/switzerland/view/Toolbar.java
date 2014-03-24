package ch.fhnw.oop.switzerland.view;

import ch.fhnw.oop.switzerland.model.*;
import ch.fhnw.oop.switzerland.controller.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.beans.*;
import ch.fhnw.oop.switzerland.base.*;

public class Toolbar extends JToolBar
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3996322304067942847L;
	private final Switzerland applicationModel;
    private final Controller controller;
    private JButton saveButton;
    private JButton undoButton;
    private JButton redoButton;
    private JTextField searchField;
    
    public Toolbar(final Switzerland applicationModel, final Controller controller) {
        super();
        this.applicationModel = applicationModel;
        this.controller = controller;
        this.setFloatable(false);
        this.setRollover(true);
        this.initializeComponents();
        this.layoutComponents();
        this.addEventListeners();
        this.addObservers();
    }
    
    private void initializeComponents() {
        this.saveButton = new JButton(new ImageIcon(Toolbar.class.getResource("save-icon.png")));
        this.undoButton = new JButton(new ImageIcon(Toolbar.class.getResource("undo-icon.png")));
        this.redoButton = new JButton(new ImageIcon(Toolbar.class.getResource("redo-icon.png")));
        (this.searchField = new JTextField()).setPreferredSize(new Dimension(200, 25));
        this.searchField.setMaximumSize(new Dimension(200, 25));
    }
    
    private void layoutComponents() {
        this.add(this.saveButton);
        this.addSeparator(new Dimension(20, 1));
        this.add(this.undoButton);
        this.add(this.redoButton);
        this.add(Box.createHorizontalGlue());
        this.add(this.searchField);
        this.add(Box.createHorizontalStrut(10));
    }
    
    private void addEventListeners() {
        this.saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Toolbar.this.controller.save();
            }
        });
        this.searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Toolbar.this.controller.searchNext(Toolbar.this.searchField.getText());
            }
        });
        this.undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Toolbar.this.controller.undo();
            }
        });
        this.redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Toolbar.this.controller.redo();
            }
        });
    }
    
    private void addObservers() {
        this.applicationModel.addPropertyChangeListener("searchString", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                Toolbar.this.searchField.setText((String)evt.getNewValue());
            }
        });
        this.controller.getCommandExecutor().addPropertyChangeListener("nextRedoCommand", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final Command cmd = (Command)evt.getNewValue();
                Toolbar.this.redoButton.setEnabled(cmd != null);
                Toolbar.this.redoButton.setToolTipText((cmd != null) ? ("redo " + cmd.getName()) : "nothing to redo");
            }
        });
        this.controller.getCommandExecutor().addPropertyChangeListener("nextUndoCommand", new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final Command cmd = (Command)evt.getNewValue();
                Toolbar.this.undoButton.setEnabled(cmd != null);
                Toolbar.this.undoButton.setToolTipText((cmd != null) ? ("undo " + cmd.getName()) : "nothing to undo");
            }
        });
    }
}
