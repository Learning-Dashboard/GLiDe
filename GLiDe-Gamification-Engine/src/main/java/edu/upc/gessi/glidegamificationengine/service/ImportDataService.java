package edu.upc.gessi.glidegamificationengine.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportDataService {
    void importData(String gameSubjectAcronym, Integer gameCourse, String gamePeriod, Integer groupNumber, MultipartFile importedData) throws IOException;
}
