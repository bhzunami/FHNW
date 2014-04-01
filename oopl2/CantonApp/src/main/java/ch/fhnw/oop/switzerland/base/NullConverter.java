package ch.fhnw.oop.switzerland.base;

import java.text.*;

public class NullConverter implements Converter<Object>
{
    @Override
    public String toString(final Object data) {
        return data.toString();
    }
    
    @Override
    public Object toData(final String userInput) throws ParseException {
        return userInput;
    }
}
