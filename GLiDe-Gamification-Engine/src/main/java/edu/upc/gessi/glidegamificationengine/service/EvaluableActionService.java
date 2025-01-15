package edu.upc.gessi.glidegamificationengine.service;

import edu.upc.gessi.glidegamificationengine.dto.EvaluableActionDTO;

import java.util.List;

public interface EvaluableActionService {

    void initiateEvaluableActions();

    List<EvaluableActionDTO> getEvaluableActions();

    EvaluableActionDTO getEvaluableAction(String evaluableActionId);

}
