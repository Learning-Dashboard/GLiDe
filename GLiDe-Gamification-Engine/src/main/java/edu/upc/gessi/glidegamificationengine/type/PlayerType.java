package edu.upc.gessi.glidegamificationengine.type;

import edu.upc.gessi.glidegamificationengine.exception.TypeNotCorrectException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PlayerType {
    Team, Individual;

    public static PlayerType fromString(String string) {
        PlayerType playerType;
        try{
            playerType = PlayerType.valueOf(string);
        }
        catch (IllegalArgumentException e){
            throw new TypeNotCorrectException("Player name '" + string + "' not a valid player type (Only available: " +
                    Stream.of(PlayerType.values()).map(value -> "'" + value.toString() + "'").collect(Collectors.joining(", ")) + ").");
        }
        return playerType;
    }
}
