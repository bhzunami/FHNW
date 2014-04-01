package ch.fhnw.oop.switzerland.base;

import java.util.*;
import java.text.*;

public class DoubleConverter implements Converter<Double>
{
    private final NumberFormat format;
    
    public DoubleConverter() {
        this(2, 4);
    }
    
    public DoubleConverter(final int minFractionDigits, final int maxFractionDigits) {
        super();
        (this.format = NumberFormat.getNumberInstance(new Locale("de", "CH"))).setMinimumFractionDigits(minFractionDigits);
        this.format.setMaximumFractionDigits(maxFractionDigits);
    }
    
    @Override
    public String toString(final Double data) {
        return this.format.format(data);
    }
    
    @Override
    public Double toData(final String userInput) throws ParseException {
        final ParsePosition parsePosition = new ParsePosition(0);
        final Number number = this.format.parse(userInput, parsePosition);
        if (parsePosition.getIndex() != userInput.length() || number.doubleValue() > Double.MAX_VALUE) {
            throw new ParseException("Unparseable number: \"" + userInput + "\"", parsePosition.getErrorIndex());
        }
        return number.doubleValue();
    }
}
