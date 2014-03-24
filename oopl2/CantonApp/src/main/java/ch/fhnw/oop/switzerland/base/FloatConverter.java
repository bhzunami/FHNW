package ch.fhnw.oop.switzerland.base;

import java.util.*;
import java.text.*;

public class FloatConverter implements Converter<Float>
{
    private final NumberFormat format;
    
    public FloatConverter() {
        this(1, 4);
    }
    
    public FloatConverter(final int minFractionDigits, final int maxFractionDigits) {
        super();
        (this.format = NumberFormat.getNumberInstance(new Locale("de", "CH"))).setMinimumFractionDigits(minFractionDigits);
        this.format.setMaximumFractionDigits(maxFractionDigits);
    }
    
    @Override
    public String toString(final Float data) {
        return this.format.format(data);
    }
    
    @Override
    public Float toData(final String userInput) throws ParseException {
        final ParsePosition parsePosition = new ParsePosition(0);
        final Number number = this.format.parse(userInput, parsePosition);
        if (number == null || parsePosition.getIndex() != userInput.length() || number.floatValue() > Float.MAX_VALUE) {
            throw new ParseException("Unparseable number: \"" + userInput + "\"", parsePosition.getErrorIndex());
        }
        return number.floatValue();
    }
}
