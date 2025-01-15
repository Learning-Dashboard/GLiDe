package edu.upc.gessi.glidegamificationengine.type;

import edu.upc.gessi.glidegamificationengine.exception.TypeNotCorrectException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum AchievementCategoryType {
    Points      { @Override public String getDescription() { return "They represent the basic unit to measure the players' evolution and determine the players' level"; };
                  @Override public Boolean isNumerical() { return true; }; },
    Badges      { @Override public String getDescription() { return "They represent special milestones reached by the players"; };
                  @Override public Boolean isNumerical() { return false; }; },
    Resources   { @Override public String getDescription() { return "They represent real-world rewards for the players"; };
                  @Override public Boolean isNumerical() { return true; }; };

    public abstract String getDescription();

    public abstract Boolean isNumerical();

    public static AchievementCategoryType fromString(String string) {
        AchievementCategoryType achievementCategoryType;
        try{
            achievementCategoryType = AchievementCategoryType.valueOf(string);
        }
        catch (IllegalArgumentException e){
            throw new TypeNotCorrectException("Achievement category name '" + string + "' not a valid achievement category type (Only available: " +
                    Stream.of(AchievementCategoryType.values()).map(value -> "'" + value.toString() + "'").collect(Collectors.joining(", ")) + ").");
        }
        return achievementCategoryType;
    }
}
