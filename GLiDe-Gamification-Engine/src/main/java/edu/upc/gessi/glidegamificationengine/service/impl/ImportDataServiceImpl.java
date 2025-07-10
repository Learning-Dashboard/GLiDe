package edu.upc.gessi.glidegamificationengine.service.impl;

import edu.upc.gessi.glidegamificationengine.entity.*;
import edu.upc.gessi.glidegamificationengine.entity.key.GameGroupKey;
import edu.upc.gessi.glidegamificationengine.exception.ConstraintViolationException;
import edu.upc.gessi.glidegamificationengine.exception.ResourceNotFoundException;
import edu.upc.gessi.glidegamificationengine.repository.*;
import edu.upc.gessi.glidegamificationengine.service.ImportDataService;
import edu.upc.gessi.glidegamificationengine.type.PeriodType;
import edu.upc.gessi.glidegamificationengine.type.PlayerType;
import edu.upc.gessi.glidegamificationengine.type.UserType;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ImportDataServiceImpl implements ImportDataService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GameGroupRepository gameGroupRepository;

    @Autowired
    private StudentUserRepository studentUserRepository;

    @Autowired
    private TeamPlayerRepository teamPlayerRepository;

    @Autowired
    private IndividualPlayerRepository individualPlayerRepository;

    @Value("${backend.api.base-url}")
    private String backendBaseUrl;

    private void importToInteraction(MultipartFile importedData, String gameSubjectAcronym, Integer gameCourse, String gamePeriod){
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("importedData", importedData.getResource());

        WebClient webClient = WebClient.builder().baseUrl(backendBaseUrl).build();
        webClient.post()
                .uri("/importData?gameSubjectAcronym=" + gameSubjectAcronym + "&gameCourse=" + gameCourse + "&gamePeriod=" + gamePeriod)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .toEntity(String.class)
                .doOnSuccess(response -> {
                    System.out.println("Import successful: " + response.getBody());
                })
                .doOnError(error -> {
                    System.err.println("Error during import: " + error.getMessage());
                })
                .block();
    }

    @Override
    @Transactional
    public void importData(String gameSubjectAcronym, Integer gameCourse, String gamePeriod, Integer groupNumber, MultipartFile importedData){
        importToInteraction(importedData, gameSubjectAcronym, gameCourse, gamePeriod);

        Resource resource = new ClassPathResource("static/images/ld.png");
        byte[] defaultImage;
        try (InputStream inputStream = resource.getInputStream()) {
            defaultImage = inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Issue with default image: " + e.getMessage());
        }

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(importedData.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.builder().setHeader().setIgnoreHeaderCase(true).setTrim(true).build())){

            PeriodType periodType = PeriodType.fromString(gamePeriod);

            Iterable<CSVRecord> records = csvParser.getRecords();
            for (CSVRecord record : records) {
                if(record.get("Email Address").isBlank() || record.get("Name").isBlank() || record.get("Surname").isBlank() || record.get("Username").isBlank() || record.get("Github Username").isBlank() || record.get("Taiga Username").isBlank() || record.get("Project Name").isBlank() || record.get("Project Github").isBlank() || record.get("Project Taiga").isBlank() || record.get("Project Learningdashboard").isBlank())
                    throw new ConstraintViolationException("Invalid CSV record");

                ProjectEntity projectEntity;

                Optional<ProjectEntity> optionalProject = projectRepository.findByCustomQuery(record.get("Project Name"), gameSubjectAcronym, gameCourse, periodType);
                if (optionalProject.isEmpty()){
                    GameGroupKey gameGroupKey = new GameGroupKey();
                    gameGroupKey.setGameSubjectAcronym(gameSubjectAcronym);
                    gameGroupKey.setGameCourse(gameCourse);
                    gameGroupKey.setGamePeriod(periodType);
                    gameGroupKey.setGroup(groupNumber);

                    GameGroupEntity existingGameGroup = gameGroupRepository.findById(gameGroupKey).orElseThrow(() -> new ResourceNotFoundException("Game Group does not exist"));

                    projectEntity = new ProjectEntity();

                    projectEntity.setGameGroupEntity(existingGameGroup);
                    projectEntity.setGithubIdentifier(record.get("Project Github"));
                    projectEntity.setLearningdashboardIdentifier(record.get("Project Learningdashboard"));
                    projectEntity.setTaigaIdentifier(record.get("Project Taiga"));
                    projectEntity.setName(record.get("Project Name"));
                    projectEntity.setDescription("No description");
                    projectRepository.save(projectEntity);
                }
                else {
                    projectEntity = optionalProject.get();
                }

                StudentUserEntity userEntity = new StudentUserEntity();
                userEntity.setUsername(record.get("Email Address"));
                userEntity.setEmail(record.get("Email Address"));
                userEntity.setName(record.get("Name"));
                userEntity.setSurname(record.get("Surname"));
                userEntity.setPassword("password");
                userEntity.setType(UserType.Student);
                userEntity.setGithubUsername(record.get("Github Username"));
                userEntity.setLearningdashboardUsername(record.get("Github Username"));
                userEntity.setTaigaUsername(record.get("Taiga Username"));
                studentUserRepository.save(userEntity);

                TeamPlayerEntity teamPlayerEntity;
                Optional<TeamPlayerEntity> optionalTeamPlayer = teamPlayerRepository.findById(record.get("Project Name"));
                if (optionalTeamPlayer.isEmpty()){
                    teamPlayerEntity = new TeamPlayerEntity();
                    teamPlayerEntity.setProjectEntity(projectEntity);
                    teamPlayerEntity.setType(PlayerType.Team);
                    teamPlayerEntity.setLogo(Base64.getEncoder().encodeToString(defaultImage));
                    teamPlayerEntity.setLevel(0);
                    teamPlayerEntity.setPoints(0);
                    teamPlayerEntity.setPlayername(record.get("Project Name"));
                    teamPlayerRepository.save(teamPlayerEntity);
                }
                else {
                    teamPlayerEntity = optionalTeamPlayer.get();
                }

                Optional<IndividualPlayerEntity> optionalIndividualPlayer = individualPlayerRepository.findById(record.get("Username"));
                if (optionalIndividualPlayer.isEmpty()) {
                    IndividualPlayerEntity individualPlayerEntity = new IndividualPlayerEntity();
                    individualPlayerEntity.setTeamPlayerEntity(teamPlayerEntity);
                    individualPlayerEntity.setLevel(0);
                    individualPlayerEntity.setPoints(0);
                    individualPlayerEntity.setPlayername(record.get("Username"));
                    individualPlayerEntity.setAvatar(Base64.getEncoder().encodeToString(defaultImage));
                    individualPlayerEntity.setType(PlayerType.Individual);
                    individualPlayerEntity.setRole("Student");
                    individualPlayerEntity.setStudentUserEntity(userEntity);
                    individualPlayerRepository.save(individualPlayerEntity);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }
}
