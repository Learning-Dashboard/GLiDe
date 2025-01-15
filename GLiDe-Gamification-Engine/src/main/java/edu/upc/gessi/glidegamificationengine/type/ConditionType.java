package edu.upc.gessi.glidegamificationengine.type;

import edu.upc.gessi.glidegamificationengine.exception.TypeNotCorrectException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ConditionType {
    ValueGreaterThan            { @Override public Integer getNumberOfRequiredParameters() { return 1; };
                                  @Override public ActionCategoryType getApplicableActionCategory() { return ActionCategoryType.PlayerPerformance; }; },
    ValueLessThan               { @Override public Integer getNumberOfRequiredParameters() { return 1; };
                                  @Override public ActionCategoryType getApplicableActionCategory() { return ActionCategoryType.PlayerPerformance; }; },
    ValueEqualTo                { @Override public Integer getNumberOfRequiredParameters() { return 1; };
                                  @Override public ActionCategoryType getApplicableActionCategory() { return ActionCategoryType.PlayerPerformance; }; },
    ValueGreaterThanOrEqualTo   { @Override public Integer getNumberOfRequiredParameters() { return 1; };
                                  @Override public ActionCategoryType getApplicableActionCategory() { return ActionCategoryType.PlayerPerformance; }; },
    ValueLessThanOrEqualTo      { @Override public Integer getNumberOfRequiredParameters() { return 1; };
                                  @Override public ActionCategoryType getApplicableActionCategory() { return ActionCategoryType.PlayerPerformance; }; },
    ValueOutsideOfRange         { @Override public Integer getNumberOfRequiredParameters() { return 2; };
                                  @Override public ActionCategoryType getApplicableActionCategory() { return ActionCategoryType.PlayerPerformance; }; },
    ValueInsideOfRange          { @Override public Integer getNumberOfRequiredParameters() { return 2; };
                                  @Override public ActionCategoryType getApplicableActionCategory() { return ActionCategoryType.PlayerPerformance; }; };

    public abstract Integer getNumberOfRequiredParameters();

    public abstract ActionCategoryType getApplicableActionCategory();

    public Boolean checkCondition(Float value, List<Float> parameters) {
        return switch (this) {
            case ValueGreaterThan -> (value > parameters.getFirst());
            case ValueLessThan -> (value < parameters.getFirst());
            case ValueEqualTo -> (value.equals(parameters.getFirst()));
            case ValueGreaterThanOrEqualTo -> (value >= parameters.getFirst());
            case ValueLessThanOrEqualTo -> (value <= parameters.getFirst());
            case ValueOutsideOfRange -> (value < parameters.getFirst() || value > parameters.getLast());
            case ValueInsideOfRange -> (value >= parameters.getFirst() && value <= parameters.getLast());
            default -> false;
        };
    }

    public static ConditionType fromString(String string) {
        ConditionType conditionType;
        try{
            conditionType = ConditionType.valueOf(string);
        }
        catch (IllegalArgumentException e){
            throw new TypeNotCorrectException("Condition name '" + string + "' not a valid condition type (Only available: " +
                    Stream.of(ConditionType.values()).map(value -> "'" + value.toString() + "'").collect(Collectors.joining(", ")) + ").");
        }
        return conditionType;
    }
}
