package edu.upc.gessi.glidebackend.controller;

import edu.upc.gessi.glidebackend.dto.*;
import edu.upc.gessi.glidebackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/individuals", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<IndividualPlayerDto>> getAllIndividualPlayers() {
        List<IndividualPlayerDto> individualPlayerDto = playerService.getAllIndividualPlayers();
        return ResponseEntity.ok(individualPlayerDto);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/individuals/{playername}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IndividualPlayerDto> getIndividualPlayer(@PathVariable("playername") String individualPlayerPlayername) {
        IndividualPlayerDto individualPlayerDto = playerService.getIndividualPlayer(individualPlayerPlayername);
        return ResponseEntity.ok(individualPlayerDto);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/individuals/{playername}/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentUserDto> getStudentUser(@PathVariable("playername") String individualPlayerPlayername) {
        StudentUserDto studentUserDto = playerService.getStudentUser(individualPlayerPlayername);
        return ResponseEntity.ok(studentUserDto);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value ="/{playername}/monitoring", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerMonitoringDto> getPlayerMonitoring(@PathVariable("playername") String playerPlayername) {
        PlayerMonitoringDto playerMonitoringDto = playerService.getPlayerMonitoring(playerPlayername);
        return ResponseEntity.ok(playerMonitoringDto);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @PatchMapping(value = "/{playername}/monitoring/selectedMetrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerMonitoringDto> setPlayerMonitoring(@PathVariable("playername") String playerPlayername,
                                                                   @RequestParam(value = "selectedMetrics", required = false) String selectedMetrics,
                                                                   @RequestParam(value = "selectedHistoryMetrics", required = false) String selectedHistoryMetrics,
                                                                   @RequestParam(value = "selectedBarMetrics", required = false) String selectedBarMetrics) {
        PlayerMonitoringDto playerMonitoringDto = playerService.setPlayerMonitoring(playerPlayername, selectedMetrics, selectedHistoryMetrics, selectedBarMetrics);
        return ResponseEntity.ok(playerMonitoringDto);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @PatchMapping(value = "/{playername}/monitoring/selectedDates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerMonitoringDto> setDatesMonitoring(@PathVariable("playername") String playerPlayername,
                                                                  @RequestParam(value = "startDate") String startDate,
                                                                  @RequestParam(value = "endDate") String endDate) {
        PlayerMonitoringDto playerMonitoringDto = playerService.setPlayerMonitoringDates(playerPlayername, startDate, endDate);
        return ResponseEntity.ok(playerMonitoringDto);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value ="/{playername}/gamification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerGamificationDto> getPlayerGamification(@PathVariable("playername") String playerPlayername) {
        PlayerGamificationDto playerGamificationDto = playerService.getPlayerGamification(playerPlayername);
        return ResponseEntity.ok(playerGamificationDto);
    }

}
