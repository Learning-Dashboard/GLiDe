package edu.upc.gessi.glidegamificationengine.type;

import edu.upc.gessi.glidegamificationengine.exception.TypeNotCorrectException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ExtentType {
    Subject, Group;

    public static ExtentType fromString(String string) {
        ExtentType extentType;
        try{
            extentType = ExtentType.valueOf(string);
        }
        catch (IllegalArgumentException e){
            throw new TypeNotCorrectException("Extent name '" + string + "' not a valid extent type (Only available: " +
                    Stream.of(ExtentType.values()).map(value -> "'" + value.toString() + "'").collect(Collectors.joining(", ")) + ").");
        }
        return extentType;
    }
}
