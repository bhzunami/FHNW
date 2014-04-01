package ch.fhnw.oop.switzerland.base;

import java.text.*;
import java.util.*;

public class LongConverter implements Converter<Long>
{
    private static final NumberFormat FORMAT;
    
    @Override
    public String toString(final Long data) {
        return LongConverter.FORMAT.format(data);
    }
    
    @Override
    public Long toData(final String userInput) throws ParseException {
        final ParsePosition parsePosition = new ParsePosition(0);
        final Number number = LongConverter.FORMAT.parse(userInput, parsePosition);
        if (parsePosition.getIndex() != userInput.length() || number.longValue() > 2147483647L) {
            throw new ParseException("Unparseable number: \"" + userInput + "\"", parsePosition.getErrorIndex());
        }
        return number.longValue();
    }
    
    static {
        FORMAT = NumberFormat.getIntegerInstance(new Locale("de", "CH"));
    }
}
