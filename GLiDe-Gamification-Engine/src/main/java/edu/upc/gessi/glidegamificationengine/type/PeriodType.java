package edu.upc.gessi.glidegamificationengine.type;

import edu.upc.gessi.glidegamificationengine.exception.TypeNotCorrectException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PeriodType {
    Quadrimester1, Quadrimester2;

    public static PeriodType fromString(String string) {
        PeriodType periodType;
        try{
            periodType = PeriodType.valueOf(string);
        }
        catch (IllegalArgumentException e){
            throw new TypeNotCorrectException("Period name '" + string + "' not a valid period type (Only available: " +
                    Stream.of(PeriodType.values()).map(value -> "'" + value.toString() + "'").collect(Collectors.joining(", ")) + ").");
        }
        return periodType;
    }
}
