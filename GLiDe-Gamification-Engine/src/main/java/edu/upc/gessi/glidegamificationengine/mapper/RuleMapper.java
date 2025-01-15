package edu.upc.gessi.glidegamificationengine.mapper;

import edu.upc.gessi.glidegamificationengine.dto.DateRuleDTO;
import edu.upc.gessi.glidegamificationengine.dto.SimpleRuleDTO;
import edu.upc.gessi.glidegamificationengine.entity.DateRuleEntity;
import edu.upc.gessi.glidegamificationengine.entity.SimpleRuleEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class RuleMapper {

    public static SimpleRuleDTO mapToSimpleRuleDto(SimpleRuleEntity simpleRuleEntity) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<SimpleRuleEntity, SimpleRuleDTO>() {
            @Override
            protected void configure() {
                using(ctx -> ((SimpleRuleEntity) ctx.getSource()).getGameEntity().getId().getSubjectAcronym())
                        .map(source, destination.getGameSubjectAcronym());
                using(ctx -> ((SimpleRuleEntity) ctx.getSource()).getGameEntity().getId().getCourse())
                        .map(source, destination.getGameCourse());
                using(ctx -> ((SimpleRuleEntity) ctx.getSource()).getGameEntity().getId().getPeriod())
                        .map(source, destination.getGamePeriod());
                using(ctx -> ((SimpleRuleEntity) ctx.getSource()).getEvaluableActionEntity().getId())
                        .map(source, destination.getEvaluableActionId());
                using(ctx -> ((SimpleRuleEntity) ctx.getSource()).getAchievementAssignmentEntity().getAchievementEntity().getId())
                        .map(source, destination.getAchievementId());
            }
        });
        return modelMapper.map(simpleRuleEntity, SimpleRuleDTO.class);
    }

    public static DateRuleDTO mapToDateRuleDto(DateRuleEntity dateRuleEntity) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<DateRuleEntity, DateRuleDTO>() {
            @Override
            protected void configure() {
                using(ctx -> ((DateRuleEntity) ctx.getSource()).getGameEntity().getId().getSubjectAcronym())
                        .map(source, destination.getGameSubjectAcronym());
                using(ctx -> ((DateRuleEntity) ctx.getSource()).getGameEntity().getId().getCourse())
                        .map(source, destination.getGameCourse());
                using(ctx -> ((DateRuleEntity) ctx.getSource()).getGameEntity().getId().getPeriod())
                        .map(source, destination.getGamePeriod());
                using(ctx -> ((DateRuleEntity) ctx.getSource()).getEvaluableActionEntity().getId())
                        .map(source, destination.getEvaluableActionId());
                using(ctx -> ((DateRuleEntity) ctx.getSource()).getAchievementAssignmentEntity().getAchievementEntity().getId())
                        .map(source, destination.getAchievementId());
            }
        });
        return modelMapper.map(dateRuleEntity, DateRuleDTO.class);
    }

}
