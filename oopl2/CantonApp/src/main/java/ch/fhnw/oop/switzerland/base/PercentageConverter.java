package ch.fhnw.oop.switzerland.base;

import java.text.*;
import java.util.*;

public class PercentageConverter implements Converter<Float>
{
    private static final NumberFormat FORMAT;
    
    @Override
    public String toString(final Float data) {
        return PercentageConverter.FORMAT.format(data);
    }
    
    @Override
    public Float toData(final String userInput) throws ParseException {
        return PercentageConverter.FORMAT.parse(userInput).floatValue();
    }
    
    static {
        (FORMAT = NumberFormat.getPercentInstance(new Locale("de", "CH"))).setMinimumFractionDigits(1);
    }
}
