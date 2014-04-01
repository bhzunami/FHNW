package ch.fhnw.oop.switzerland;

import java.util.Locale;

import javax.swing.UIManager;

import ch.fhnw.oop.switzerland.controller.Controller;
import ch.fhnw.oop.switzerland.model.Switzerland;
import ch.fhnw.oop.switzerland.view.SwitzerlandFrame;

public class CantonApp
{
    public static void main(final String[] args) {
        setLookAndFeel();
        Locale.setDefault(new Locale("de", "ch"));
        final Switzerland model = new Switzerland();
        final Controller controller = new Controller(model);
        final SwitzerlandFrame frame = new SwitzerlandFrame(model, controller);
        controller.startup(frame);
    }
    
    private static void setLookAndFeel() {
        try {
            // put license information here!
        	final String[] li = {  };
            UIManager.put("Synthetica.license.info", li);
            UIManager.put("Synthetica.license.key", "");
            UIManager.setLookAndFeel("de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel");
            UIManager.put("Synthetica.textComponents.useSwingOpaqueness", Boolean.FALSE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
