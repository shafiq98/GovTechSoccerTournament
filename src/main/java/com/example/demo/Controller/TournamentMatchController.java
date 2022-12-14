package com.example.demo.Controller;

import com.example.demo.RequestDTO.SoccerMatchRequest;
import com.example.demo.ResponseDTO.SoccerMatchResponse;
import com.example.demo.ResponseDTO.TeamResponse;
import com.example.demo.Service.TournamentMatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.MalformedInputException;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("api/v1/score")
public class TournamentMatchController {
    private final TournamentMatchService tournamentMatchService;

    public TournamentMatchController(TournamentMatchService tournamentMatchService) {
        this.tournamentMatchService = tournamentMatchService;
    }

    @PostMapping
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    public ResponseEntity<List<SoccerMatchResponse>> updateScores(@RequestBody SoccerMatchRequest soccerMatchRequest) {
        try {
            List<SoccerMatchResponse> response = tournamentMatchService.updateScore(soccerMatchRequest);
            return ResponseEntity.ok(response);
        } catch (MalformedInputException c) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    public ResponseEntity<List<SoccerMatchResponse>> getAllScores() {
        return ResponseEntity.ok(tournamentMatchService.getAllScores());
    }

    @GetMapping(path = "/winner")
    @CrossOrigin(origins = "*", methods = RequestMethod.GET)
    public ResponseEntity<List<TeamResponse>> getWinners() {
        return ResponseEntity.ok(tournamentMatchService.getWinner());
    }

    @DeleteMapping
    @CrossOrigin(origins = "*", methods = RequestMethod.DELETE)
    public ResponseEntity<Void> purgeAllInformation() {
        tournamentMatchService.purgeData();
        return ResponseEntity.ok().build();
    }
}
