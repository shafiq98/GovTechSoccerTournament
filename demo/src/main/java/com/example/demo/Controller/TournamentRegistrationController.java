package com.example.demo.Controller;

import com.example.demo.RequestDTO.TeamRequest;
import com.example.demo.ResponseDTO.TeamResponse;
import com.example.demo.Service.TournamentRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.MalformedInputException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/register")
public class TournamentRegistrationController {

    private final TournamentRegistrationService tournamentRegistrationService;

    public TournamentRegistrationController(TournamentRegistrationService tournamentRegistrationService) {
        this.tournamentRegistrationService = tournamentRegistrationService;
    }

    @PostMapping
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    public ResponseEntity<List<TeamResponse>> registerTeams(@RequestBody TeamRequest teamRequest) {
        try {
            log.debug("TeamRequest : " + teamRequest.getMultilineInput());
            List<TeamResponse> result = tournamentRegistrationService.registerTeams(teamRequest);
            return ResponseEntity.ok(result);
        } catch (MalformedInputException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        List<TeamResponse> result = tournamentRegistrationService.getAllTeams();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    @CrossOrigin(origins = "*", methods = RequestMethod.DELETE)
    public ResponseEntity<Void> purgeAllInformation() {
        tournamentRegistrationService.purgeData();
        return ResponseEntity.ok().build();
    }

}
