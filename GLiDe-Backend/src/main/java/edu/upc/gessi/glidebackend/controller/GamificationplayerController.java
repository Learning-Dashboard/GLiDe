package edu.upc.gessi.glidebackend.controller;

import edu.upc.gessi.glidebackend.service.GamificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/gamification")
public class GamificationplayerController {

    private GamificationService gamificationService;

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/players/individuals/{playername}")
    public ResponseEntity<?> getIndividualPlayer(@PathVariable("playername") String individualPlayerPlayername) {
        Object individualPlayer = gamificationService.getIndividualPlayer(individualPlayerPlayername);
        return ResponseEntity.ok(individualPlayer);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/players/teams/{playername}")
    public ResponseEntity<?> getTeamPlayer(@PathVariable("playername") String teamPlayerPlayername) {
        Object teamPlayer = gamificationService.getTeamPlayer(teamPlayerPlayername);
        return ResponseEntity.ok(teamPlayer);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/players/{playername}/achievements")
    public ResponseEntity<?> getPlayerAchievements(@PathVariable("playername") String teamPlayerPlayername,
                                                   @RequestParam(value = "attained", required = false) String attained,
                                                   @RequestParam(value = "category", required = false) String category) {
        List<Object> achievements = gamificationService.getPlayerAchievements(teamPlayerPlayername, attained, category);
        return ResponseEntity.ok(achievements);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/evaluableActions")
    public ResponseEntity<?> getEvaluableActions() {
        List<Object> evaluableActions = gamificationService.getEvaluableActions();
        return ResponseEntity.ok(evaluableActions);
    }
}
