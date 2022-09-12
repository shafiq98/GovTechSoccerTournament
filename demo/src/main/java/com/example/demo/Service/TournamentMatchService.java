package com.example.demo.Service;

import com.example.demo.Repository.MatchHistoryRepository;
import com.example.demo.Repository.TournamentRepository;
import com.example.demo.RequestDTO.SoccerMatchRequest;
import com.example.demo.ResponseDTO.SoccerMatchResponse;
import com.example.demo.ResponseDTO.TeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TournamentMatchService {
    private final MatchHistoryRepository matchHistoryRepository;
    private final TournamentRepository tournamentRepository;


    @Autowired
    public TournamentMatchService(MatchHistoryRepository matchHistoryRepository, TournamentRepository tournamentRepository) {
        this.matchHistoryRepository = matchHistoryRepository;
        this.tournamentRepository = tournamentRepository;
    }

    public List<SoccerMatchResponse> updateScore(SoccerMatchRequest matchRequest) throws MalformedInputException {
        String[] matches = matchRequest.getMultilineInput().split("\n");

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

                // update score
                updateScores(arguments);

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

    public List<TeamResponse> getWinner() {
        List<TeamResponse> allTeams = tournamentRepository.findAll();

        allTeams.sort(Collections.reverseOrder());

        log.debug(String.valueOf(allTeams));

        return allTeams;

    }


    private void updateScores(String[] arguments) {
        // TODO add checks to ensure team exists
        log.debug("Team 1 Name : "+ arguments[0] + ", goals : " + arguments[2]);
        log.debug("Team 2 Name : "+ arguments[1] + ", goals : " + arguments[3]);

        TeamResponse team1 = tournamentRepository.findById(arguments[0]).get();
        team1.setScore(team1.getScore()+Integer.parseInt(arguments[2]));
        TeamResponse team2 = tournamentRepository.findById(arguments[1]).get();
        team2.setScore(team2.getScore()+Integer.parseInt(arguments[3]));
    }
}
