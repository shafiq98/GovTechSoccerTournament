package com.example.demo.Service;

import com.example.demo.Repository.MatchHistoryRepository;
import com.example.demo.Repository.TournamentRepository;
import com.example.demo.RequestDTO.SoccerMatchRequest;
import com.example.demo.ResponseDTO.SoccerMatchResponse;
import com.example.demo.ResponseDTO.TeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.MalformedInputException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TournamentMatchServiceTest {
    private TournamentMatchService tournamentMatchService;

    @Mock
    MatchHistoryRepository matchHistoryRepository;
    @Mock
    TournamentRepository tournamentRepository;

    @BeforeEach
    public void setup() {
        tournamentMatchService = new TournamentMatchService(matchHistoryRepository, tournamentRepository);
    }

    @Test
    void updateScoresTest1() throws MalformedInputException {
        List<TeamResponse> teams = updateWinningsHelper();

        when(tournamentRepository.findById("firstTeam")).thenReturn(Optional.ofNullable(teams.get(0)));
        when(tournamentRepository.findById("secondTeam")).thenReturn(Optional.ofNullable(teams.get(1)));

        String inputLine1 = "firstTeam secondTeam 0 1";
        SoccerMatchRequest soccerMatchRequest = SoccerMatchRequest.builder()
                .multilineInput(inputLine1)
                .build();

        SoccerMatchResponse soccerMatchResponse = SoccerMatchResponse.builder()
                .team1("firstTeam")
                .team2("secondTeam")
                .team1Goals(0)
                .team2Goals(1)
                .build();

        List<SoccerMatchResponse> expectedOutputList = List.of(soccerMatchResponse);
        List<SoccerMatchResponse> outputList = tournamentMatchService.updateScore(soccerMatchRequest);

        Assertions.assertIterableEquals(expectedOutputList, outputList);
    }

    @Test
    void updateScoresTest2() throws MalformedInputException {
        List<TeamResponse> teams = updateWinningsHelper();

        when(tournamentRepository.findById("firstTeam")).thenReturn(Optional.ofNullable(teams.get(0)));
        when(tournamentRepository.findById("secondTeam")).thenReturn(Optional.ofNullable(teams.get(1)));
        when(tournamentRepository.findById("thirdTeam")).thenReturn(Optional.ofNullable(teams.get(2)));

        String inputLine1 = "firstTeam secondTeam 0 1\nfirstTeam thirdTeam 1 3\n";
        SoccerMatchRequest soccerMatchRequest = SoccerMatchRequest.builder()
                .multilineInput(inputLine1)
                .build();

        SoccerMatchResponse soccerMatchResponse1 = SoccerMatchResponse.builder()
                .team1("firstTeam")
                .team2("secondTeam")
                .team1Goals(0)
                .team2Goals(1)
                .build();

        SoccerMatchResponse soccerMatchResponse2 = SoccerMatchResponse.builder()
                .team1("firstTeam")
                .team2("thirdTeam")
                .team1Goals(1)
                .team2Goals(3)
                .build();

        List<SoccerMatchResponse> expectedOutputList = List.of(soccerMatchResponse1, soccerMatchResponse2);
        List<SoccerMatchResponse> outputList = tournamentMatchService.updateScore(soccerMatchRequest);

        Assertions.assertIterableEquals(expectedOutputList, outputList);
    }

    @Test
    void updateScoresTest3() {
        List<TeamResponse> teams = updateWinningsHelper();

        when(tournamentRepository.findById("firstTeam")).thenReturn(Optional.ofNullable(teams.get(0)));

        String inputLine1 = "firstTeam secondTeam 0 1";
        SoccerMatchRequest soccerMatchRequest = SoccerMatchRequest.builder()
                .multilineInput(inputLine1)
                .build();

        NoSuchElementException thrown = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> {tournamentMatchService.updateScore(soccerMatchRequest);}
        );

        Assertions.assertTrue(thrown.getMessage().contains("secondTeam"));
    }

    @Test
    void getWinnerTest1() {
        // tests alternate scoring system
        List<TeamResponse> inputList = getWinnerTest1Helper();
        when(tournamentRepository.findAll()).thenReturn(inputList);

        List<TeamResponse> top4 = tournamentMatchService.getWinner();

        log.debug(String.valueOf(top4));
        // TODO : Add assertions
    }

    @Test
    void getWinnerTest2() {
        // tests date based scoring system
        List<TeamResponse> inputList = getWinnerTest2Helper();
        when(tournamentRepository.findAll()).thenReturn(inputList);

        List<TeamResponse> top4 = tournamentMatchService.getWinner();

        log.debug(String.valueOf(top4));
        // TODO : Add assertions
    }

    @Test
    void updateScoresBadRequest1() {
        String inputLine1 = "firstTeam secondTeam 0";
        SoccerMatchRequest soccerMatchRequest = SoccerMatchRequest.builder()
                .multilineInput(inputLine1)
                .build();

        MalformedInputException thrown = Assertions.assertThrows(
                MalformedInputException.class,
                () -> {
                    tournamentMatchService.updateScore(soccerMatchRequest);
                }
        );
        Assertions.assertTrue(thrown.getMessage().contains("3"));
    }

    private List<TeamResponse> updateWinningsHelper() {
        TeamResponse team1 = TeamResponse.builder()
                .teamName("firstTeam")
                .teamNumber(2)
                .registrationDate(LocalDate.now())
                .numOfWins(10)
                .numOfDraws(0)
                .numOfLoss(1)
                .score(0)
                .build();

        TeamResponse team2 = TeamResponse.builder()
                .teamName("secondTeam")
                .teamNumber(2)
                .registrationDate(LocalDate.now())
                .numOfWins(9)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        TeamResponse team3 = TeamResponse.builder()
                .teamName("thirdTeam")
                .teamNumber(2)
                .registrationDate(LocalDate.now())
                .numOfWins(0)
                .numOfDraws(3)
                .numOfLoss(0)
                .score(0)
                .build();

        return List.of(team1, team2, team3);
    }
    private List<TeamResponse> getWinnerTest1Helper() {
        TeamResponse team1 = TeamResponse.builder()
                .teamName("firstTeam")
                .teamNumber(2)
                .registrationDate(LocalDate.now())
                .numOfWins(10)
                .numOfDraws(0)
                .numOfLoss(1)
                .score(0)
                .build();

        TeamResponse team2 = TeamResponse.builder()
                .teamName("secondTeam")
                .teamNumber(2)
                .registrationDate(LocalDate.now())
                .numOfWins(9)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        TeamResponse team3 = TeamResponse.builder()
                .teamName("thirdTeam")
                .teamNumber(2)
                .registrationDate(LocalDate.now())
                .numOfWins(0)
                .numOfDraws(3)
                .numOfLoss(0)
                .score(0)
                .build();

        TeamResponse team4 = TeamResponse.builder()
                .teamName("fourthTeam")
                .teamNumber(2)
                .registrationDate(LocalDate.now())
                .numOfWins(1)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        TeamResponse team5 = TeamResponse.builder()
                .teamName("fifthTeam")
                .teamNumber(2)
                .registrationDate(LocalDate.now())
                .numOfWins(0)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        return List.of(team1, team2, team3, team4, team5);
    }
    private List<TeamResponse> getWinnerTest2Helper() {
        LocalDate date = LocalDate.now();
        TeamResponse team1 = TeamResponse.builder()
                .teamName("firstTeam")
                .teamNumber(2)
                .registrationDate(date)
                .numOfWins(0)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        date = date.minusDays(1);

        TeamResponse team2 = TeamResponse.builder()
                .teamName("secondTeam")
                .teamNumber(2)
                .registrationDate(date)
                .numOfWins(0)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        date = date.minusDays(1);

        TeamResponse team3 = TeamResponse.builder()
                .teamName("thirdTeam")
                .teamNumber(0)
                .registrationDate(date)
                .numOfWins(0)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        date = date.minusDays(1);

        TeamResponse team4 = TeamResponse.builder()
                .teamName("fourthTeam")
                .teamNumber(2)
                .registrationDate(date)
                .numOfWins(0)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        date = date.minusDays(1);

        TeamResponse team5 = TeamResponse.builder()
                .teamName("fifthTeam")
                .teamNumber(0)
                .registrationDate(date)
                .numOfWins(0)
                .numOfDraws(0)
                .numOfLoss(0)
                .score(0)
                .build();

        return List.of(team1, team2, team3, team4, team5);
    }
}