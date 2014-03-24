package ch.fhnw.oop.switzerland.view;

import ch.fhnw.oop.switzerland.model.*;
import ch.fhnw.oop.switzerland.controller.*;
import ch.fhnw.oop.splitflap.*;
import net.miginfocom.swing.*;
import ch.fhnw.oop.switzerland.base.*;
import java.util.*;
import java.text.*;
import java.beans.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class CantonDetailView extends AbstractDetailEditor
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5873557583132574377L;
	private JLabel coatOfArmsLabel;
    private JLabel nameLabel;
    private JLabel nameReadOnly;
    private JTextField nameField;
    private JLabel shortNameLabel;
    private JLabel shortNameReadOnly;
    private JTextField shortNameField;
    private JLabel cantonIdLabel;
    private JLabel cantonIdField;
    private JLabel standesstimmeLabel;
    private JTextField standesstimmeField;
    private JLabel sinceLabel;
    private JTextField sinceField;
    private JLabel capitalLabel;
    private JTextField capitalField;
    private JLabel populationLabel;
    private JSpinner populationField;
    private JLabel foreignersLabel;
    private JTextField foreignersField;
    private JLabel areaLabel;
    private JSpinner areaField;
    private JLabel languageLabel;
    private JTextField languageField;
    private JLabel communesLabel;
    private JTextArea communesField;
    
    public CantonDetailView(final Switzerland applicationModel, final Controller controller) {
        super(applicationModel, "selectedCanton", controller);
    }
    
    @Override
    protected void initializeComponents() {
        this.coatOfArmsLabel = new JLabel();
        this.nameLabel = new JLabel("Kanton");
        (this.nameReadOnly = new JLabel()).setFont(FlipImages.INSTANCE.getFont().deriveFont(1, 28.0f));
        (this.shortNameReadOnly = new JLabel()).setFont(FlipImages.INSTANCE.getFont().deriveFont(1, 18.0f));
        this.nameField = new JTextField();
        this.shortNameLabel = new JLabel("K\u00fcrzel");
        this.shortNameField = new JTextField();
        this.cantonIdLabel = new JLabel("Kantonsnummer");
        this.cantonIdField = new JLabel();
        this.standesstimmeLabel = new JLabel("Standesstimme");
        this.standesstimmeField = new JTextField();
        this.sinceLabel = new JLabel("Beitritt");
        this.sinceField = new JTextField();
        this.capitalLabel = new JLabel("Hauptort");
        this.capitalField = new JTextField();
        this.populationLabel = new JLabel("Einwohner");
        this.populationField = new JSpinner(new SpinnerNumberModel(0, null, 2147483646, 1));
        this.foreignersLabel = new JLabel("Ausl\u00e4nderanteil");
        this.foreignersField = new JTextField();
        this.areaLabel = new JLabel("<html>Fl\u00e4che&nbsp;in&nbsp;<font size = 2>km<sup>2</sup></font></html>");
        this.areaField = new JSpinner(new SpinnerNumberModel(0.0, null, null, 1.0));
        this.languageLabel = new JLabel("Amtssprache");
        this.languageField = new JTextField();
        this.communesLabel = new JLabel("Gemeinden");
        (this.communesField = new JTextArea()).setLineWrap(true);
    }
    
    @Override
    protected void layoutComponents() {
        final JPanel header = new JPanel();
        header.setLayout(new MigLayout("wrap 2, insets 0 0 0 0", "[]20[shrink, grow, fill]", "[top]20[top]"));
        header.add(this.coatOfArmsLabel, "w 125:125:125, h 151:151:151, span 1 2");
        header.add(this.nameReadOnly, "w 30::");
        header.add(this.shortNameReadOnly, "");
        this.add(header, "span 4, pushy 0, shrink");
        this.add(this.nameLabel, "w 30::, gapy 3");
        this.add(this.nameField);
        this.add(this.cantonIdLabel, "w 30::,gapy 3");
        this.add(this.cantonIdField, "w :60:60");
        this.add(this.shortNameLabel, "w 30::,gapy 3");
        this.add(this.shortNameField, "w :60:60");
        this.add(this.standesstimmeLabel, "w 30::,gapy 3");
        this.add(this.standesstimmeField, "w :60:60");
        this.add(this.capitalLabel, "w 30::,gapy 3");
        this.add(this.capitalField);
        this.add(this.sinceLabel, "w 30::,gapy 3");
        this.add(this.sinceField, "w :60:60");
        this.add(this.populationLabel, "w 30::,gapy 3");
        this.add(this.populationField, "w 30::");
        this.add(this.foreignersLabel, "w 30::,gapy 3");
        this.add(this.foreignersField, "w :60:60");
        this.add(this.areaLabel, "w 30::");
        this.add(this.areaField);
        this.add(this.languageLabel, "w 30::, gapy 3");
        this.add(this.languageField, "wrap");
        this.add(this.communesLabel, "gapy 3, w 30::, growy 0, span 4");
        this.add(new JScrollPane(this.communesField), "h 30:80:max, pad -15 0, span 4, push, grow");
    }
    
    @Override
    protected void initializeBindings() {
        this.bind(this.coatOfArmsLabel, "coatOfArms");
        this.bind(this.nameReadOnly, "name");
        this.bind(this.shortNameReadOnly, "shortName");
        this.bind(this.nameField, "name", new NullConverter());
        this.bind(this.shortNameField, "shortName", new NullConverter());
        this.bind(this.cantonIdField, "cantonId", new IntegerConverter());
        this.bind(this.standesstimmeField, "standesstimme", new FloatConverter());
        this.bind(this.sinceField, "since", new NullConverter());
        this.bind(this.capitalField, "capital", new NullConverter());
        this.bind(this.populationField, "population");
        this.bind(this.foreignersField, "foreigners", new PercentageConverter());
        this.bind(this.areaField, "area");
        this.bind(this.languageField, "languages", new NullConverter());
        this.bind(this.communesLabel, "communes", new Converter() {
            @Override
            public String toString(final Object data) {
                final String communes = (String)data;
                final StringTokenizer tokenizer = new StringTokenizer(communes, ",", false);
                int counter = 0;
                while (tokenizer.hasMoreTokens()) {
                    final String next = tokenizer.nextToken();
                    if (!next.trim().isEmpty()) {
                        ++counter;
                    }
                }
                return "Gemeinden (" + counter + ")";
            }
            
            @Override
            public Object toData(final String userInput) throws ParseException {
                return userInput;
            }
        });
        this.bind(this.communesField, "communes", new NullConverter());
        this.addBinding(new Binding(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                try {
                    final Highlighter hilite = CantonDetailView.this.communesField.getHighlighter();
                    final Document doc = CantonDetailView.this.communesField.getDocument();
                    final String text = doc.getText(0, doc.getLength());
                    int pos = 0;
                    hilite.removeAllHighlights();
                    final String pattern = (String)((Switzerland)CantonDetailView.this.getApplicationModel()).getProperty("searchString");
                    if (pattern != null && pattern.length() > 0) {
                        final Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.yellow);
                        while ((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos)) >= 0) {
                            hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                            pos += pattern.length();
                        }
                    }
                }
                catch (BadLocationException ex) {}
            }
        }, this.communesField, "communes"));
    }
    
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
    {
        public MyHighlightPainter(final Color color) {
            super(color);
        }
    }
}
