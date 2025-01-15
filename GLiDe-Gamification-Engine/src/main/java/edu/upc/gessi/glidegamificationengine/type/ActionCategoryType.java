package edu.upc.gessi.glidegamificationengine.type;

public enum ActionCategoryType {
    PlayerPerformance   { @Override public String getDescription() { return "Measurable characteristic of a player related to their performance in the game"; }; };

    public abstract String getDescription();
}
