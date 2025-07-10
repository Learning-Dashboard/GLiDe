package edu.upc.gessi.glidebackend.service.impl;

import edu.upc.gessi.glidebackend.entity.*;
import edu.upc.gessi.glidebackend.repository.*;
import edu.upc.gessi.glidebackend.service.ImportDataService;
import edu.upc.gessi.glidebackend.type.PlayerType;
import edu.upc.gessi.glidebackend.type.PeriodType;
import edu.upc.gessi.glidebackend.exception.ConstraintViolationException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Optional;

@Service
public class ImportDataServiceImpl implements ImportDataService {

    @Autowired
    private StudentUserRepository studentUserRepository;

    @Autowired
    private TeamPlayerRepository teamPlayerRepository;

    @Autowired
    private IndividualPlayerRepository individualPlayerRepository;

    @Autowired
    private PlayerMonitoringRepository playerMonitoringRepository;

    @Autowired
    private PlayerGamificationRepository playerGamificationRepository;

    public void importData(MultipartFile importedData, String gameSubjectAcronym, Integer gameCourse, String gamePeriod) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(importedData.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.builder().setHeader().setIgnoreHeaderCase(true).setTrim(true).build())) {

            LocalDate today = LocalDate.now();
            LocalDate lastWeek = today.minusWeeks(1);

            Date startDate = Date.valueOf(lastWeek);
            Date endDate = Date.valueOf(today);

            Iterable<CSVRecord> records = csvParser.getRecords();
            for (CSVRecord record : records) {
                if(record.get("Email Address").isBlank() || record.get("Name").isBlank() || record.get("Surname").isBlank() || record.get("Username").isBlank() || record.get("Github Username").isBlank() || record.get("Taiga Username").isBlank() || record.get("Project Name").isBlank() || record.get("Project Github").isBlank() || record.get("Project Taiga").isBlank() || record.get("Project Learningdashboard").isBlank())
                    throw new ConstraintViolationException("Invalid CSV record");

                StudentUserEntity studentUserEntity = new StudentUserEntity();
                studentUserEntity.setUsername(record.get("Email Address"));
                studentUserEntity.setLearningdashboardUsername(record.get("Name") + ' ' + record.get("Surname"));
                studentUserEntity.setGithubUsername(record.get("Github Username"));
                studentUserEntity.setTaigaUsername(record.get("Taiga Username"));

                studentUserRepository.save(studentUserEntity);

                TeamPlayerEntity teamPlayerEntity;
                Optional<TeamPlayerEntity> optionalTeamPlayerEntity = teamPlayerRepository.findById(record.get("Project Name"));
                if (optionalTeamPlayerEntity.isEmpty()){
                    teamPlayerEntity = new TeamPlayerEntity();
                    teamPlayerEntity.setProject(record.get("Project Learningdashboard"));
                    teamPlayerEntity.setType(PlayerType.Team);
                    teamPlayerEntity.setPoints(0);
                    teamPlayerEntity.setLevel(0);
                    teamPlayerEntity.setPlayername(record.get("Project Name"));
                    teamPlayerRepository.save(teamPlayerEntity);
                }
                else {
                    teamPlayerEntity = optionalTeamPlayerEntity.get();
                }

                IndividualPlayerEntity individualPlayerEntity = new IndividualPlayerEntity();
                individualPlayerEntity.setTeamPlayerEntity(teamPlayerEntity);
                individualPlayerEntity.setLevel(0);
                individualPlayerEntity.setPoints(0);
                individualPlayerEntity.setPlayername(record.get("Username"));
                individualPlayerEntity.setType(PlayerType.Individual);
                individualPlayerEntity.setRole("Student");
                individualPlayerEntity.setStudentUserEntity(studentUserEntity);
                individualPlayerRepository.save(individualPlayerEntity);

                Optional<PlayerMonitoringEntity> optionalPlayerMonitoringEntity = playerMonitoringRepository.findOptionalByIndividualPlayerEntity(individualPlayerEntity);
                if (optionalPlayerMonitoringEntity.isEmpty()) {
                    PlayerMonitoringEntity playerMonitoringEntity = new PlayerMonitoringEntity();
                    playerMonitoringEntity.setStartDate(startDate);
                    playerMonitoringEntity.setEndDate(endDate);
                    playerMonitoringEntity.setSelectedMetrics("acceptance_criteria_check");
                    playerMonitoringEntity.setSelectedHistoryMetrics("acceptance_criteria_check");
                    playerMonitoringEntity.setSelectedBarMetrics("acceptance_criteria_check");
                    playerMonitoringEntity.setIndividualPlayerEntity(individualPlayerEntity);
                    playerMonitoringRepository.save(playerMonitoringEntity);
                }

                Optional<PlayerGamificationEntity> optionalPlayerGamificationEntity = playerGamificationRepository.findOptionalByIndividualPlayerEntity(individualPlayerEntity);
                if (optionalPlayerGamificationEntity.isEmpty()) {
                    PlayerGamificationEntity playerGamificationEntity = new PlayerGamificationEntity();
                    playerGamificationEntity.setIndividualPlayerEntity(individualPlayerEntity);
                    playerGamificationEntity.setGameSubjectAcronym(gameSubjectAcronym);
                    playerGamificationEntity.setGameCourse(gameCourse);
                    playerGamificationEntity.setGamePeriod(PeriodType.fromString(gamePeriod));
                    playerGamificationRepository.save(playerGamificationEntity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }
}
