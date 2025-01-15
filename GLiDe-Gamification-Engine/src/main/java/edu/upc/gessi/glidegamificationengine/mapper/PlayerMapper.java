package edu.upc.gessi.glidegamificationengine.mapper;

import edu.upc.gessi.glidegamificationengine.dto.IndividualPlayerDTO;
import edu.upc.gessi.glidegamificationengine.dto.PlayerLoggedAchievementDTO;
import edu.upc.gessi.glidegamificationengine.dto.TeamPlayerDTO;
import edu.upc.gessi.glidegamificationengine.entity.IndividualPlayerEntity;
import edu.upc.gessi.glidegamificationengine.entity.LoggedAchievementEntity;
import edu.upc.gessi.glidegamificationengine.entity.TeamPlayerEntity;
import org.modelmapper.ModelMapper;

public class PlayerMapper {

    public static TeamPlayerDTO mapToTeamPlayerDto(TeamPlayerEntity teamPlayerEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(teamPlayerEntity, TeamPlayerDTO.class);
    }

    public static IndividualPlayerDTO mapToIndividualPlayerDto(IndividualPlayerEntity individualPlayerEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(individualPlayerEntity, IndividualPlayerDTO.class);
    }

    public static PlayerLoggedAchievementDTO mapToPlayerLoggedAchievementDto(LoggedAchievementEntity loggedAchievementEntity) {
        PlayerLoggedAchievementDTO playerLoggedAchievementDto = new PlayerLoggedAchievementDTO();
        playerLoggedAchievementDto.setId(loggedAchievementEntity.getId());
        playerLoggedAchievementDto.setDate(loggedAchievementEntity.getDate());
        playerLoggedAchievementDto.setCategory(loggedAchievementEntity.getAchievementAssignmentEntity().getAchievementEntity().getCategory());
        playerLoggedAchievementDto.setName(loggedAchievementEntity.getAchievementAssignmentEntity().getAchievementEntity().getName());
        playerLoggedAchievementDto.setIcon(loggedAchievementEntity.getAchievementAssignmentEntity().getAchievementEntity().getIcon());
        playerLoggedAchievementDto.setUnits(loggedAchievementEntity.getAchievementAssignmentEntity().getAchievementUnits());
        playerLoggedAchievementDto.setMessage(loggedAchievementEntity.getAchievementAssignmentEntity().getMessage());
        playerLoggedAchievementDto.setViewed(loggedAchievementEntity.getViewed());
        return playerLoggedAchievementDto;
    }
}
