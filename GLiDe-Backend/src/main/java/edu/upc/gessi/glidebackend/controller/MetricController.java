package edu.upc.gessi.glidebackend.controller;

import edu.upc.gessi.glidebackend.service.MetricService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/metrics")
public class MetricController {

    private final MetricService metricService;

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/categories")
    public ResponseEntity<?> getAllCategories() {
        List<Object> categories = metricService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping()
    public ResponseEntity<?> getProjectCategories(@RequestParam(value = "prj") String prj) {
        List<Object> projectCategories = metricService.getProjectCategories(prj);
        return ResponseEntity.ok(projectCategories);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/students")
    public ResponseEntity<?> getMetrics(@RequestParam(value = "prj") String prj) {
        List<Object> metrics = metricService.getMetrics(prj);
        return ResponseEntity.ok(metrics);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/current")
    public ResponseEntity<?> getProjectMetrics(@RequestParam(value = "prj") String prj) {
        List<Object> metrics = metricService.getProjectMetrics(prj);
        return ResponseEntity.ok(metrics);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/students/historical")
    public ResponseEntity<?> getMetricsHistory(@RequestParam(value = "prj") String prj,
                                               @RequestParam(value = "from") String from,
                                               @RequestParam(value = "to") String to) {
        List<Object> metrics = metricService.getMetricsHistory(prj, from, to);
        return ResponseEntity.ok(metrics);
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @GetMapping(value="/historical")
    public ResponseEntity<?> getProjectMetricsHistory(@RequestParam(value = "prj") String prj,
                                               @RequestParam(value = "from") String from,
                                               @RequestParam(value = "to") String to) {
        List<Object> metrics = metricService.getProjectMetricsHistory(prj, from, to);
        return ResponseEntity.ok(metrics);
    }

}
