package edu.upc.gessi.glidebackend.mapper;

import edu.upc.gessi.glidebackend.dto.IndividualPlayerDto;
import edu.upc.gessi.glidebackend.entity.IndividualPlayerEntity;

public class PlayerMapper {

    public static IndividualPlayerDto mapToIndividualPlayerDto(IndividualPlayerEntity individualPlayerEntity) {
        IndividualPlayerDto individualPlayerDto = new IndividualPlayerDto();
        individualPlayerDto.setPlayername(individualPlayerEntity.getPlayername());
        individualPlayerDto.setType(individualPlayerEntity.getType());
        individualPlayerDto.setRole(individualPlayerEntity.getRole());
        individualPlayerDto.setTeamPlayername(individualPlayerEntity.getTeamPlayerEntity().getPlayername());
        individualPlayerDto.setProject(individualPlayerEntity.getTeamPlayerEntity().getProject());
        individualPlayerDto.setLearningdashboardUsername(individualPlayerEntity.getStudentUserEntity().getLearningdashboardUsername());
        individualPlayerDto.setGithubUsername(individualPlayerEntity.getStudentUserEntity().getGithubUsername());
        individualPlayerDto.setTaigaUsername(individualPlayerEntity.getStudentUserEntity().getTaigaUsername());
        return individualPlayerDto;
    }


}
