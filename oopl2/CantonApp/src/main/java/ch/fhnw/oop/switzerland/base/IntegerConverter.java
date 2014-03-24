package ch.fhnw.oop.switzerland.base;

import java.text.*;
import java.util.*;

public class IntegerConverter implements Converter<Integer>
{
    private static final NumberFormat FORMAT;
    
    @Override
    public String toString(final Integer data) {
        return IntegerConverter.FORMAT.format(data);
    }
    
    @Override
    public Integer toData(final String userInput) throws ParseException {
        final ParsePosition parsePosition = new ParsePosition(0);
        final Number number = IntegerConverter.FORMAT.parse(userInput, parsePosition);
        if (parsePosition.getIndex() != userInput.length() || number.longValue() > 2147483647L) {
            throw new ParseException("Unparseable number: \"" + userInput + "\"", parsePosition.getErrorIndex());
        }
        return number.intValue();
    }
    
    static {
        FORMAT = NumberFormat.getIntegerInstance(new Locale("de", "CH"));
    }
}
