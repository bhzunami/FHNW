package ch.fhnw.oop.switzerland.model;

import ch.fhnw.oop.switzerland.base.*;
import java.util.*;
import java.io.*;

public class Switzerland extends AbstractModel
{
    public static final String SELECTED_CANTON_PROPERTY = "selectedCanton";
    public static final String All_CANTONS_PROPERTY = "cantons";
    public static final String SEARCH_STRING_PROPERTY = "searchString";
    private Canton selectedCanton;
    private List<Canton> cantons;
    private String searchString;
    
    public Switzerland() {
        super();
        this.cantons = new ArrayList<Canton>();
    }
    
    public List<Canton> loadCantons() {
        try {
            Thread.sleep(300L);
        }
        catch (InterruptedException ex) {}
        return this.readCantonsFromFile();
    }
    
    public Canton searchNext(final String text) {
        this.searchString = text;
        if (text.length() == 2) {
            for (final Canton canton : this.cantons) {
                final String shortName = ((String)canton.getProperty("shortName")).toLowerCase();
                if (shortName.equals(text.toLowerCase())) {
                    return canton;
                }
            }
        }
        int i;
        int start;
        for (start = (i = this.cantons.indexOf(this.selectedCanton) + 1); i < this.cantons.size(); ++i) {
            final Canton canton2 = this.cantons.get(i);
            if (canton2.containsText(text)) {
                return canton2;
            }
        }
        for (i = 0; i < start; ++i) {
            final Canton canton2 = this.cantons.get(i);
            if (canton2.containsText(text)) {
                return canton2;
            }
        }
        return null;
    }
    
    private List<Canton> readCantonsFromFile() {
        BufferedReader bufferedFileReader = null;
        final List<Canton> result = new ArrayList<Canton>();
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(Switzerland.class.getResourceAsStream("CantonsWithCommunes.csv"), "UTF-8");
            bufferedFileReader = new BufferedReader(inputStreamReader);
            bufferedFileReader.readLine();
            for (String line = bufferedFileReader.readLine(); line != null; line = bufferedFileReader.readLine()) {
                final Scanner scanner = new Scanner(line);
                scanner.useDelimiter(";");
                scanner.useLocale(new Locale("de", "CH"));
                final String name = scanner.next();
                final String shortName = scanner.next();
                final int cantonId = scanner.nextInt();
                final float standesstimme = scanner.nextFloat();
                final String since = scanner.next();
                final String capital = scanner.next();
                final int population = scanner.nextInt();
                final float foreigners = scanner.nextFloat() / 100.0f;
                final float area = scanner.nextFloat();
                final float density = scanner.nextFloat();
                final String languages = scanner.next();
                final String communes = scanner.next();
                result.add(new Canton(name, shortName, cantonId, standesstimme, since, capital, population, foreigners, area, density, languages, communes));
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        finally {
            try {
                if (bufferedFileReader != null) {
                    bufferedFileReader.close();
                }
            }
            catch (IOException ex) {}
        }
        return result;
    }
    
    public Canton getSelectedCanton() {
        return this.selectedCanton;
    }
    
    public long getTotalPopulation() {
        long population = 0L;
        for (final Canton c : this.cantons) {
            population += c.getPopulation();
        }
        return population;
    }
    
    public double getTotalArea() {
        double area = 0.0;
        for (final Canton c : this.cantons) {
            area += c.getArea();
        }
        return area;
    }
    
    public List<Canton> getCantons() {
        return this.cantons;
    }
    
    public void save() {
        for (final Canton c : this.cantons) {
            c.setProperty("isDirty", false);
        }
    }
}
