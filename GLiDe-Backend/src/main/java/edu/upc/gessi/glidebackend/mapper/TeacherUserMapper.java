package edu.upc.gessi.glidebackend.mapper;

import edu.upc.gessi.glidebackend.dto.TeacherGameDto;
import edu.upc.gessi.glidebackend.dto.TeacherUserDto;
import edu.upc.gessi.glidebackend.entity.TeacherGameEntity;
import edu.upc.gessi.glidebackend.entity.TeacherUserEntity;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherUserMapper {
    
    private static final ModelMapper modelMapper = new ModelMapper();
    
    public static TeacherUserDto mapToTeacherUserDto(TeacherUserEntity teacherUserEntity) {
        return modelMapper.map(teacherUserEntity, TeacherUserDto.class);
    }
    
    public static TeacherUserEntity mapToTeacherUserEntity(TeacherUserDto teacherUserDto) {
        return modelMapper.map(teacherUserDto, TeacherUserEntity.class);
    }
    
    public static TeacherGameDto mapToTeacherGameDto(TeacherGameEntity teacherGameEntity) {
        TeacherGameDto dto = modelMapper.map(teacherGameEntity, TeacherGameDto.class);
        dto.setTeacherEmail(teacherGameEntity.getTeacherUserEntity().getEmail());
        return dto;
    }
    
    public static TeacherGameEntity mapToTeacherGameEntity(TeacherGameDto teacherGameDto) {
        return modelMapper.map(teacherGameDto, TeacherGameEntity.class);
    }
    
    public static List<TeacherGameDto> mapToTeacherGameDtoList(List<TeacherGameEntity> teacherGameEntities) {
        return teacherGameEntities.stream()
                .map(TeacherUserMapper::mapToTeacherGameDto)
                .collect(Collectors.toList());
    }
    
    public static List<TeacherUserDto> mapToTeacherUserDtoList(List<TeacherUserEntity> teacherUserEntities) {
        return teacherUserEntities.stream()
                .map(TeacherUserMapper::mapToTeacherUserDto)
                .collect(Collectors.toList());
    }
}