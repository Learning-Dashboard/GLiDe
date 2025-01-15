package edu.upc.gessi.glidegamificationengine.service;

import edu.upc.gessi.glidegamificationengine.dto.GameGroupDTO;

public interface GameGroupService {

    GameGroupDTO createGroup(Integer gameCourse, String gamePeriod, String gameSubjectAcronym, Integer group);
}
