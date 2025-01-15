package edu.upc.gessi.glidebackend.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingInformationException extends RuntimeException{

    public MissingInformationException(String message) {
        super(message);
    }

}
