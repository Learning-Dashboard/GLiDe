package edu.upc.gessi.glidegamificationengine.type;

import java.sql.Date;
import java.time.LocalDate;

public enum StateType {
    Preparation, Playing, Finished;

    public static StateType getState(Date startDate, Date endDate) {
        LocalDate now = new Date(System.currentTimeMillis()).toLocalDate();
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        if (now.isBefore(start)) {
            return StateType.Preparation;
        } else if (now.isAfter(end)) {
            return StateType.Finished;
        } else {
            return StateType.Playing;
        }
    }
}
