package edu.upc.gessi.glidegamificationengine.service;

import edu.upc.gessi.glidegamificationengine.dto.SimpleRuleDTO;

import java.util.List;

public interface OpenAPIService {

    SimpleRuleDTO getSuggestedRuleTitleMessage(String evaluableActionDescription, String condition, List<Float> conditionParameters);
}
