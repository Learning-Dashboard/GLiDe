package edu.upc.gessi.glidebackend.mapper;

import edu.upc.gessi.glidebackend.dto.StudentUserDto;
import edu.upc.gessi.glidebackend.entity.StudentUserEntity;
import org.modelmapper.ModelMapper;


public class StudentUserMapper {
    public static StudentUserDto mapToStudentUserDto(StudentUserEntity studentUserEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(studentUserEntity, StudentUserDto.class);
    }
}
