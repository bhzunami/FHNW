package ch.fhnw.oop.switzerland.model;

public class Commune
{
    private String cantonShortname;
    private int districtId;
    private int communeId;
    private String name;
    private String shortname;
    private String destrictName;
    private String cantonName;
    private String lastModified;
    
    public Commune(final String cantonShortname, final int districtId, final int communeId, final String name, final String shortname, final String destrictName, final String cantonName, final String lastModified) {
        super();
        this.cantonShortname = cantonShortname;
        this.districtId = districtId;
        this.communeId = communeId;
        this.name = name;
        this.shortname = shortname;
        this.destrictName = destrictName;
        this.cantonName = cantonName;
        this.lastModified = lastModified;
    }
    
    public String getCantonShortname() {
        return this.cantonShortname;
    }
    
    public int getDistrictId() {
        return this.districtId;
    }
    
    public int getCommuneId() {
        return this.communeId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getShortname() {
        return this.shortname;
    }
    
    public String getDestrictName() {
        return this.destrictName;
    }
    
    public String getCantonName() {
        return this.cantonName;
    }
    
    public String getLastModified() {
        return this.lastModified;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
