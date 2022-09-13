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
import java.util.List;
import java.util.NoSuchElementException;

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
    }


    private void updateWinnings(String[] arguments) {
        // TODO add checks to ensure team exists
        log.debug("Team 1 Name : "+ arguments[0] + ", goals : " + arguments[2]);
        log.debug("Team 2 Name : "+ arguments[1] + ", goals : " + arguments[3]);

        TeamResponse team1 = tournamentRepository.findById(arguments[0]).orElseThrow(() -> new NoSuchElementException("No team with :" + arguments[0] + "exists"));
        TeamResponse team2 = tournamentRepository.findById(arguments[1]).orElseThrow(() -> new NoSuchElementException("No team with :" + arguments[1] + "exists"));

        Integer team1Score = Integer.valueOf(arguments[2]);
        Integer team2Score = Integer.valueOf(arguments[3]);

        if (team1Score > team2Score) {
            team1.setNumOfWins(team1.getNumOfWins()+1);
        }
        else if (team1Score.equals(team2Score)) {
            team1.setNumOfDraws(team1.getNumOfDraws()+1);
            team2.setNumOfDraws(team2.getNumOfDraws()+1);
        }
        else {
            team2.setNumOfWins(team2.getNumOfWins()+1);
        }
    }



    public List<TeamResponse> getWinner() {
        // Get all teams, and generate scores for each of them
        List<TeamResponse> allTeams = tournamentRepository.findAll();
        for (TeamResponse team : allTeams) {
            generateScores(team, false);
        }

        // sort by 1st scoring system
        List<TeamResponse> allTeamsSortable = new ArrayList<>(allTeams);
        allTeamsSortable.sort(TeamResponse.getScoreComparator());

        log.debug("Calculated Team Order : " + allTeamsSortable);
//
        // create nested list of groups tied for 1st 4 rankings
        List<List<TeamResponse>> tiedGroups = groupDraws(allTeamsSortable);
        log.debug("=========================Tied Groups (Pre-Recalculation) : =========================");
        for (List<TeamResponse> tiedGroup : tiedGroups) {
            log.debug(String.valueOf(tiedGroup));
        }
        log.debug("====================================================================================");

        // recalculate scores for first 4 sublists, if their length is > 1
        List<List<TeamResponse>> recalculatedTeams = new ArrayList<>();
        for (List<TeamResponse> group : tiedGroups) {
            List<TeamResponse> recalculatedTeam = recalculateTeam(group);
            recalculatedTeams.add(recalculatedTeam);
        }

        // flatten recalculatedTeams
        allTeamsSortable = recalculatedTeams.stream()
                .flatMap(List::stream).toList();

        log.debug("Recalculated Team Order : " + allTeamsSortable);

        // create nested list of groups tied for 1st 4 rankings
        tiedGroups = groupDraws(allTeamsSortable);
        log.debug("=========================Tied Groups (Post-Recalculation) : =========================");
        for (List<TeamResponse> tiedGroup : tiedGroups) {
            log.debug(String.valueOf(tiedGroup));
        }
        log.debug("=====================================================================================");

        recalculatedTeams = new ArrayList<>();
        for (List<TeamResponse> group : tiedGroups) {
            List<TeamResponse> resortedGroup = sortByDate(group);
            recalculatedTeams.add(resortedGroup);
        }

        allTeamsSortable = recalculatedTeams.stream()
                .flatMap(List::stream).toList();

        log.debug("Team Order by Date : " + allTeamsSortable);

        return allTeamsSortable;
    }

    private void generateScores(TeamResponse team, boolean alternate) {
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

        int score = 0;
        score += team.getNumOfWins() * WIN_POINT;
        score += team.getNumOfDraws() * DRAW_POINT;
        score += team.getNumOfLoss() * LOSS_POINT;
        team.setScore(score);
    }

    private List<List<TeamResponse>> groupDraws(List<TeamResponse> allTeams) {
        int MAX_WINNERS = 4;
        List<List<TeamResponse>> result = new ArrayList<>();
        int rightPointer = 0;
        for (int i = 0; i < allTeams.size(); i++) {

            while (rightPointer < allTeams.size() && (allTeams.get(rightPointer).getScore().equals(allTeams.get(i).getScore())) && result.size() < MAX_WINNERS) {
                rightPointer++;
            }
            List<TeamResponse> currGroup = allTeams.subList(i, rightPointer);

            i = rightPointer-1;
            rightPointer++;

            result.add(currGroup);
        }

        return result;
    }

    private List<TeamResponse> recalculateTeam(List<TeamResponse> tiedTeams) {
        if (tiedTeams.size() < 2) {
            return tiedTeams;
        }
        for (TeamResponse team : tiedTeams) {
            generateScores(team, true);
        }
        List<TeamResponse> tiedTeamsSortable = new ArrayList<>(tiedTeams);
        tiedTeamsSortable.sort(TeamResponse.getScoreComparator());

        return tiedTeamsSortable;
    }

    private List<TeamResponse> sortByDate(List<TeamResponse> tiedTeams) {
        if (tiedTeams.size() < 2) {
            return tiedTeams;
        }
        List<TeamResponse> tiedTeamsSortable = new ArrayList<>(tiedTeams);
        tiedTeamsSortable.sort(TeamResponse.getDateComparator());

        return tiedTeamsSortable;
    }

}
