package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.dto.EvaluableActionDTO;
import edu.upc.gessi.glidegamificationengine.entity.EvaluableActionEntity;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.mapper.EvaluableActionMapper;
import edu.upc.gessi.glidegamificationengine.repository.EvaluableActionRepository;
import edu.upc.gessi.glidegamificationengine.service.EvaluableActionService;
import edu.upc.gessi.glidegamificationengine.type.ActionCategoryType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import edu.upc.gessi.glidegamificationengine.type.SourceDataToolType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluableActionServiceImpl implements EvaluableActionService {

    @Autowired
    private EvaluableActionRepository evaluableActionRepository;

    /* Methods callable from Service Layer */

    protected EvaluableActionEntity getEvaluableActionEntityById(String evaluableActionId) {
        return evaluableActionRepository.findById(evaluableActionId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluable action with id '" + evaluableActionId + "' not found."));
    }


    /* Methods callable from Controller Layer */

    @Override
    public void initiateEvaluableActions() {

        /* Learning Dashboard's metrics */
        EvaluableActionEntity assignedtasks = new EvaluableActionEntity("LDIM-Student_tasks", "assignedtasks", "Percentage of tasks assigned to a student with respect to the total number of tasks in the project", ActionCategoryType.PlayerPerformance, PlayerType.Individual, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity unassignedtasksMetric = new EvaluableActionEntity("LDTM-Unassigned_tasks", "unassignedtasks", "Percentage of tasks without assignee with respect to the total number of tasks in the sprint", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity closedtasks = new EvaluableActionEntity("LDIM-Student_closed_tasks", "closedtasks", "Percentage of closed tasks made by a student with respect to the total number of tasks assigned to the student", ActionCategoryType.PlayerPerformance, PlayerType.Individual, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity tasks_with_EE = new EvaluableActionEntity("LDTM-Tasks_with_estimated_effort_information", "tasks_with_EE", "Percentage of tasks with estimated effort information specified with respect to the total number of tasks in the sprint", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity closed_tasks_with_AE = new EvaluableActionEntity("LDTM-Closed_tasks_with_actual_effort_information", "closed_tasks_with_AE", "Percentage of closed tasks with actual effort information specified with respect to the total number of closed tasks in the sprint", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity acceptance_criteria_check = new EvaluableActionEntity("LDTM-Acceptance_criteria_application", "acceptance_criteria_check", "Percentage of user stories with acceptance criteria specified with respect to the total number of user stories in the sprint", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity pattern_check = new EvaluableActionEntity("LDTM-Use_of_user_story_pattern", "pattern_check", "Percentage of user stories with the pattern 'AS - I WANT - SO THAT - ' specified with respect to the total number of user stories in the sprint", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity commits_taskreference = new EvaluableActionEntity("LDTM-Commits_tasks_relation", "commits_taskreference", "Percentage of commits with task references (i.e. commits that include in its message the reference 'TASK #No.task' to Taiga's task) with respect to the total number of commits in the project without considering anonymous commits", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity commits_anonymous = new EvaluableActionEntity("LDTM-Anonymous_commits", "commits_anonymous", "Percentage of commits made by an unknown contributor with respect to the total number of commits in the project considering also anonymous commits", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity commits = new EvaluableActionEntity("LDIM-Student_commits", "commits", "Percentage of commits made by a student with respect to the total number of commits in the project without considering anonymous commits", ActionCategoryType.PlayerPerformance, PlayerType.Individual, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity modifiedlines = new EvaluableActionEntity("LDIM-Student_modified_lines", "modifiedlines", "Percentage of modified lines of code made by a student with respect to the total number of modified lines of code in the project without considering anonymous commits", ActionCategoryType.PlayerPerformance, PlayerType.Individual, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity commits_sd = new EvaluableActionEntity("LDTM-Commits_standard_deviation", "commits_sd", "Percentage of variation in the number of commits between team members", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity tasks_sd = new EvaluableActionEntity("LDTM-Tasks_standard_deviation", "tasks_sd", "Percentage of variation in the number of tasks between team members", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");
        EvaluableActionEntity deviation_effort_estimation_simple = new EvaluableActionEntity("LDTM-Deviation_in_estimation_of_task_effort", "deviation_effort_estimation_simple", "Percentage of closed tasks with a deviation in the task effort estimation greater than 25% in the sprint", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/metrics/*/current?prj=*");

        evaluableActionRepository.save(assignedtasks);
        evaluableActionRepository.save(unassignedtasksMetric);
        evaluableActionRepository.save(closedtasks);
        evaluableActionRepository.save(tasks_with_EE);
        evaluableActionRepository.save(closed_tasks_with_AE);
        evaluableActionRepository.save(acceptance_criteria_check);
        evaluableActionRepository.save(pattern_check);
        evaluableActionRepository.save(commits_taskreference);
        evaluableActionRepository.save(commits_anonymous);
        evaluableActionRepository.save(commits);
        evaluableActionRepository.save(modifiedlines);
        evaluableActionRepository.save(commits_sd);
        evaluableActionRepository.save(tasks_sd);
        evaluableActionRepository.save(deviation_effort_estimation_simple);

        /* Learning Dashboard's quality factors */
        EvaluableActionEntity taskscontribution = new EvaluableActionEntity("LDTF-Tasks_contribution", "taskscontribution", "Groups the metrics that measure the percentage of tasks assigned to a student with respect to the total number of tasks in the project", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity unassignedtasksFactor = new EvaluableActionEntity("LDTF-Unassigned_tasks", "unassignedtasks", "Measures the percentage of tasks without assignee with respect to the total number of tasks defined in the sprint", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity fulfillmentoftasks = new EvaluableActionEntity("LDTF-Fulfillment_of_tasks", "fulfillmentoftasks", "Groups the metrics that measure the percentage of closed tasks made by a student with respect to the total number of tasks assigned to this student", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity taskseffortinformation = new EvaluableActionEntity("LDTF-Tasks_effort_information", "taskseffortinformation", "Groups the metrics that measure tasks effort information, both estimated and actual effort to raise awareness of the importance of accuracy in estimating the time spent on different sprint tasks", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity userstoriesdefinitionquality = new EvaluableActionEntity("LDTF-User_stories_definition_quality", "userstoriesdefinitionquality", "Groups the metrics that measure the quality of user stories through the specification of acceptance criteria and the 'AS - I WANT - SO THAT - ' pattern", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity commitstasksrelation = new EvaluableActionEntity("LDTF-Commits_tasks_relation", "commitstasksrelation", "Measures the percentage of commits with references to tasks (i.e. commits that include in its message the reference 'TASK #No.task' to Taiga's task) with respect to the total number of commits in the project", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity commitsmanagement = new EvaluableActionEntity("LDTF-Commits_management", "commitsmanagement", "Measures the percentage of anonymous commits (i.e. commits from unknown users which do not match with any known contributor", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity commitscontribution = new EvaluableActionEntity("LDTF-Commits_contribution", "commitscontribution", "Groups the metrics that measure the percentage of commits made by a student with respect to the total number of commits of the project without considering merge commits", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity modifiedlinescontribution = new EvaluableActionEntity("LDTF-Modified_lines_contribution", "modifiedlinescontribution", "Groups the metrics that measure the percentage of modified lines of code made by a student with respect to the total number of modified lines of code in the project considering also additions and deletions lines of code", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");
        EvaluableActionEntity deviationmetrics = new EvaluableActionEntity("LDTF-Deviation_metrics", "deviationmetrics", "Groups the metrics that measure the standard deviation of commits, tasks, and task effort estimation", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/qualityFactors/*/current?prj=*");

        evaluableActionRepository.save(taskscontribution);
        evaluableActionRepository.save(unassignedtasksFactor);
        evaluableActionRepository.save(fulfillmentoftasks);
        evaluableActionRepository.save(taskseffortinformation);
        evaluableActionRepository.save(userstoriesdefinitionquality);
        evaluableActionRepository.save(commitstasksrelation);
        evaluableActionRepository.save(commitsmanagement);
        evaluableActionRepository.save(commitscontribution);
        evaluableActionRepository.save(modifiedlinescontribution);
        evaluableActionRepository.save(deviationmetrics);

        /* Learning Dashboard's strategic indicators */
        EvaluableActionEntity backlogmanagement = new EvaluableActionEntity("LDTI-Backlog_management", "backlogmanagement", "Groups the factors related to the management of the sprint backlog tasks", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/strategicIndicators/*/current?prj=*");
        EvaluableActionEntity informationcompleteness = new EvaluableActionEntity("LDTI-Information_completeness", "informationcompleteness", "Groups the factors related to the quality of definition of user stories and traceable commits and to the specification of effort information", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/strategicIndicators/*/current?prj=*");
        EvaluableActionEntity repositorycontribution = new EvaluableActionEntity("LDTI-Repository_contribution", "repositorycontribution", "Groups the factors related to the contribution to the project's code repository", ActionCategoryType.PlayerPerformance, PlayerType.Team, SourceDataToolType.LearningDashboard, "/strategicIndicators/*/current?prj=*");

        evaluableActionRepository.save(backlogmanagement);
        evaluableActionRepository.save(informationcompleteness);
        evaluableActionRepository.save(repositorycontribution);
    }

    @Override
    public List<EvaluableActionDTO> getEvaluableActions() {
        List<EvaluableActionEntity> evaluableActionEntities = evaluableActionRepository.findAll();
        return evaluableActionEntities.stream().map((evaluableActionEntity -> EvaluableActionMapper.mapToEvaluableActionDto(evaluableActionEntity)))
                .collect(Collectors.toList());
    }

    @Override
    public EvaluableActionDTO getEvaluableAction(String evaluableActionId) {
        EvaluableActionEntity evaluableActionEntity = getEvaluableActionEntityById(evaluableActionId);
        return EvaluableActionMapper.mapToEvaluableActionDto(evaluableActionEntity);
    }

}
