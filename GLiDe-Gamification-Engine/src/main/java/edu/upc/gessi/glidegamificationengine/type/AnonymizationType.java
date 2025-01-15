package edu.upc.gessi.glidegamificationengine.type;

import edu.upc.gessi.glidegamificationengine.exception.TypeNotCorrectException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AnonymizationType {
    Full, Partial, None;

    public static AnonymizationType fromString(String string) {
        AnonymizationType anonymizationType;
        try{
            anonymizationType = AnonymizationType.valueOf(string);
        }
        catch (IllegalArgumentException e){
            throw new TypeNotCorrectException("Anonymization name '" + string + "' not a valid anonymization type (Only available: " +
                    Stream.of(AnonymizationType.values()).map(value -> "'" + value.toString() + "'").collect(Collectors.joining(", ")) + ").");
        }
        return anonymizationType;
    }
}
