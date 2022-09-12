package com.example.demo.Service;

import com.example.demo.Repository.MatchHistoryRepository;
import com.example.demo.RequestDTO.SoccerMatchRequest;
import com.example.demo.ResponseDTO.SoccerMatchResponse;
import com.example.demo.ResponseDTO.TeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TournamentMatchService {
    private final MatchHistoryRepository matchHistoryRepository;

    @Autowired
    public TournamentMatchService(MatchHistoryRepository matchHistoryRepository) {
        this.matchHistoryRepository = matchHistoryRepository;
    }

    public List<SoccerMatchResponse> updateScore(SoccerMatchRequest matchRequest) throws MalformedInputException {
        String[] matches = matchRequest.getMultilineInput().split("\n");

        for (String s : matches) {
            log.debug("String s : " + s);
        }

        List<SoccerMatchResponse> matchList = new ArrayList<>();

        for (String match : matches) {
            String[] arguments = match.split(" ");
            // check if each line has 3 arguments
            if (arguments.length != 4) {
                log.debug("Single line does not have the correct number of arguments");
                throw new MalformedInputException(arguments.length);
            }
            else {
                SoccerMatchResponse i = SoccerMatchResponse.builder()
                            .team1(arguments[0])
                            .team2(arguments[1])
                            .team1Goals(Integer.parseInt(arguments[2]))
                            .team2Goals(Integer.parseInt(arguments[3]))
                            .build();

                matchList.add(i);
            }
        }

        matchHistoryRepository.saveAll(matchList);
        return matchList;
    }

    public List<SoccerMatchResponse> getAllScores() {
        return matchHistoryRepository.findAll();
    }

    public void purgeData() {
        matchHistoryRepository.deleteAll();
        return;
    }

}
