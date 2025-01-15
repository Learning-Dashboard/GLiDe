package edu.upc.gessi.glidebackend.service;

import java.util.List;

public interface MetricService {

    List<Object> getAllCategories();

    List<Object> getProjectCategories(String prj);

    List<Object> getMetrics(String prj);

    List<Object> getProjectMetrics(String prj);

    List<Object> getMetricsHistory(String prj, String from, String to);

    List<Object> getProjectMetricsHistory(String prj, String from, String to);
}
