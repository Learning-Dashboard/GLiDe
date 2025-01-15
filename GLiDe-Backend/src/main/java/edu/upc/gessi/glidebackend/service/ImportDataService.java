package edu.upc.gessi.glidebackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportDataService {
    void importData(MultipartFile importedData, String gameSubjectAcronym, Integer gameCourse, String gamePeriod);
}
