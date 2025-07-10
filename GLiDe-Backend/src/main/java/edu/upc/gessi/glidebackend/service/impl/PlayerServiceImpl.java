package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.dto.*;
import edu.upc.gessi.glidebackend.entity.*;
import edu.upc.gessi.glidebackend.exception.ResourceNotFoundException;
import edu.upc.gessi.glidebackend.mapper.*;
import edu.upc.gessi.glidebackend.repository.IndividualPlayerRepository;
import edu.upc.gessi.glidebackend.repository.PlayerGamificationRepository;
import edu.upc.gessi.glidebackend.repository.PlayerMonitoringRepository;
import edu.upc.gessi.glidebackend.service.PlayerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private IndividualPlayerRepository individualPlayerRepository;

    @Autowired
    private PlayerMonitoringRepository playerMonitoringRepository;

    @Autowired
    private PlayerGamificationRepository playerGamificationRepository;

    @Override
    @Transactional
    public IndividualPlayerDto getIndividualPlayer(String individualPlayerPlayername) {
        IndividualPlayerEntity individualPlayerEntity = individualPlayerRepository.findById(individualPlayerPlayername)
                .orElseThrow(() -> new ResourceNotFoundException("Individual player with playername '" + individualPlayerPlayername + "' not found."));
        return PlayerMapper.mapToIndividualPlayerDto(individualPlayerEntity);
    }

    @Override
    public List<IndividualPlayerDto> getAllIndividualPlayers() {
        List<IndividualPlayerEntity> individualPlayerEntities = individualPlayerRepository.findAll();
        return individualPlayerEntities.stream().map(PlayerMapper::mapToIndividualPlayerDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentUserDto getStudentUser(String individualPlayerPlayername) {
        IndividualPlayerEntity individualPlayerEntity = individualPlayerRepository.findById(individualPlayerPlayername)
                .orElseThrow(() -> new ResourceNotFoundException("Individual player with playername '" + individualPlayerPlayername + "' not found."));
        StudentUserEntity studentUserEntity = individualPlayerEntity.getStudentUserEntity();
        return StudentUserMapper.mapToStudentUserDto(studentUserEntity);
    }

    @Override
    @Transactional
    public PlayerMonitoringDto getPlayerMonitoring(String individualPlayerPlayername) {
        IndividualPlayerEntity individualPlayerEntity = individualPlayerRepository.findById(individualPlayerPlayername)
                .orElseThrow(() -> new ResourceNotFoundException("Individual player with playername '" + individualPlayerPlayername + "' not found."));
        PlayerMonitoringEntity playerMonitoringEntity = playerMonitoringRepository.findByIndividualPlayerEntity(individualPlayerEntity);
        return PlayerMonitoringMapper.mapToPlayerMonitoringDto(playerMonitoringEntity);
    }

    @Override
    public PlayerMonitoringDto setPlayerMonitoring(String playerPlayername, String selectedMetrics, String selectedHistoryMetrics, String selectedBarMetrics) {
        IndividualPlayerEntity individualPlayerEntity = individualPlayerRepository.findById(playerPlayername)
                .orElseThrow(() -> new ResourceNotFoundException("Individual player with playername '" + playerPlayername + "' not found."));
        PlayerMonitoringEntity playerMonitoringEntity = playerMonitoringRepository.findByIndividualPlayerEntity(individualPlayerEntity);
        playerMonitoringEntity.setSelectedMetrics(selectedMetrics);
        playerMonitoringEntity.setSelectedHistoryMetrics(selectedHistoryMetrics);
        playerMonitoringEntity.setSelectedBarMetrics(selectedBarMetrics);
        playerMonitoringRepository.save(playerMonitoringEntity);
        return PlayerMonitoringMapper.mapToPlayerMonitoringDto(playerMonitoringEntity);
    }

    @Override
    public PlayerMonitoringDto setPlayerMonitoringDates(String playerPlayername, String startDate, String endDate) {
        IndividualPlayerEntity individualPlayerEntity = individualPlayerRepository.findById(playerPlayername)
                .orElseThrow(() -> new ResourceNotFoundException("Individual player with playername '" + playerPlayername + "' not found."));
        PlayerMonitoringEntity playerMonitoringEntity = playerMonitoringRepository.findByIndividualPlayerEntity(individualPlayerEntity);
        LocalDate sDate = LocalDate.parse(startDate);
        LocalDate eDate = LocalDate.parse(endDate);
        playerMonitoringEntity.setStartDate(Date.valueOf(sDate));
        playerMonitoringEntity.setEndDate(Date.valueOf(eDate));
        playerMonitoringRepository.save(playerMonitoringEntity);
        return PlayerMonitoringMapper.mapToPlayerMonitoringDto(playerMonitoringEntity);

    }

    @Override
    @Transactional
    public PlayerGamificationDto getPlayerGamification(String individualPlayerPlayername) {
        IndividualPlayerEntity individualPlayerEntity = individualPlayerRepository.findById(individualPlayerPlayername)
                .orElseThrow(() -> new ResourceNotFoundException("Individual player with playername '" + individualPlayerPlayername + "' not found."));
        PlayerGamificationEntity playerGamificationEntity = playerGamificationRepository.findByIndividualPlayerEntity(individualPlayerEntity);
        return PlayerGamificationMapper.mapToPlayerGamificationDto(playerGamificationEntity);
    }


}
