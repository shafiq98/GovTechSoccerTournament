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
                updateWinnings(arguments);

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
        generateScores(false);
        List<TeamResponse> allTeams = tournamentRepository.findAll();
        allTeams.sort(Collections.reverseOrder());

        log.debug(String.valueOf(allTeams));

        List<TeamResponse> top4 = allTeams.subList(0,4);
        // check for draw

        return allTeams;
    }


    private void updateWinnings(String[] arguments) {
        // TODO add checks to ensure team exists
        log.debug("Team 1 Name : "+ arguments[0] + ", goals : " + arguments[2]);
        log.debug("Team 2 Name : "+ arguments[1] + ", goals : " + arguments[3]);

        TeamResponse team1 = tournamentRepository.findById(arguments[0]).get();
        TeamResponse team2 = tournamentRepository.findById(arguments[1]).get();
        Integer team1Score = Integer.valueOf(arguments[2]);
        Integer team2Score = Integer.valueOf(arguments[3]);

        if (team1Score > team2Score) {
            team1.setNumOfWins(team1.getNumOfWins()+1);
        }
        else if (team1Score == team2Score) {
            team1.setNumOfDraws(team1.getNumOfDraws()+1);
            team2.setNumOfDraws(team2.getNumOfDraws()+1);
        }
        else {
            team2.setNumOfWins(team2.getNumOfWins()+1);
        }

    }

    private void generateScores(boolean alternate) {
        List<TeamResponse> listOfTeams = tournamentRepository.findAll();

        int WIN_POINT, DRAW_POINT, LOSS_POINT;

        if (!alternate) {
            WIN_POINT = 3;
            DRAW_POINT = 1;
            LOSS_POINT = 0;
        } else {
            WIN_POINT = 5;
            DRAW_POINT = 3;
            LOSS_POINT = 1;
        }

        for (TeamResponse team : listOfTeams) {
            int score = 0;
            score += team.getNumOfWins() * WIN_POINT;
            score += team.getNumOfDraws() * DRAW_POINT;
            score += team.getNumOfLoss() * LOSS_POINT;
            team.setScore(score);
        }
    }

}
