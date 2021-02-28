package com.nent.techtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nent.techtest.model.ErrorResponse;
import com.nent.techtest.model.MessageCodes;
import com.nent.techtest.services.RequestValidator;
import com.nent.techtest.services.ArtistService;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication
@RestController
@RequestMapping("/artist")
public class ArtistDetailsApplication {

   protected ArtistService service;
   protected RequestValidator validator;

    public ArtistDetailsApplication(ArtistService service, RequestValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ArtistDetailsApplication.class, args);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json"
    )
    @GetMapping("")
    public String artist(@RequestParam(value = "id") String mdid) throws JsonProcessingException {

        if (!validator.validateMdIdLength(mdid)) {
            return new JSONObject(new ErrorResponse(MessageCodes.INVALID, "Request not valid")).toString();
        }

        return service.getArtistDetails(mdid);
    }
}
