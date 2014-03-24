package ch.fhnw.oop.switzerland.model;

import ch.fhnw.oop.switzerland.base.*;
import java.net.*;

public class Canton extends AbstractModel
{
    public static final String NAME_PROPERTY = "name";
    public static final String SHORT_NAME_PROPERTY = "shortName";
    public static final String CANTON_ID_PROPERTY = "cantonId";
    public static final String STANDESSTIMME_PROPERTY = "standesstimme";
    public static final String SINCE_PROPERTY = "since";
    public static final String CAPITAL_PROPERTY = "capital";
    public static final String POPULATION_PROPERTY = "population";
    public static final String FOREIGNERS_PROPERTY = "foreigners";
    public static final String AREA_PROPERTY = "area";
    public static final String DENSITY_PROPERTY = "density";
    public static final String LANGUAGES_PROPERTY = "languages";
    public static final String COMMUNES_PROPERTY = "communes";
    public static final String COAT_OF_ARMS_PROPERTY = "coatOfArms";
    public static final String IS_DIRTY_PROPERTY = "isDirty";
    private String name;
    private String shortName;
    private int cantonId;
    private float standesstimme;
    private String since;
    private String capital;
    private int population;
    private float foreigners;
    private double area;
    private float density;
    private String languages;
    private String communes;
    private URL coatOfArms;
    private boolean isDirty;
    
    @Override
    public void setProperty(final String propertyName, final Object newValue) {
        super.setProperty(propertyName, newValue);
        if (!"isDirty".equals(propertyName)) {
            this.setProperty("isDirty", true);
        }
    }
    
    public Canton(final String name, final String shortName, final int cantonId, final float standesstimme, final String since, final String capital, final int population, final float foreigners, final float area, final float density, final String languages, final String communes) {
        super();
        this.name = name;
        this.shortName = shortName;
        this.cantonId = cantonId;
        this.standesstimme = standesstimme;
        this.since = since;
        this.capital = capital;
        this.population = population;
        this.foreigners = foreigners;
        this.area = area;
        this.density = density;
        this.languages = languages;
        this.communes = communes;
        this.coatOfArms = Canton.class.getResource("wappen/" + shortName + ".png");
        this.isDirty = false;
    }
    
    public int getPopulation() {
        return this.population;
    }
    
    public double getArea() {
        return this.area;
    }
    
    @Override
    public String toString() {
        return this.name + " (" + this.shortName + ")";
    }
    
    public boolean containsText(final String searchString) {
        if (searchString == null || searchString.length() == 0) {
            return false;
        }
        boolean isContained = false;
        if (!isContained) {
            isContained = this.name.toLowerCase().contains(searchString.toLowerCase());
        }
        if (!isContained) {
            isContained = this.communes.toLowerCase().contains(searchString.toLowerCase());
        }
        return isContained;
    }
}
