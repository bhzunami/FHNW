package ch.fhnw.oop.switzerland.base;

import java.text.*;

public interface Converter<T>
{
    String toString(T p0);
    
    T toData(String p0) throws ParseException;
}
