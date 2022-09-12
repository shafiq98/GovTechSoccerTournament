package com.example.demo.Service;

import com.example.demo.Repository.MatchHistoryRepository;
import com.example.demo.Repository.TournamentRepository;
import com.example.demo.RequestDTO.SoccerMatchRequest;
import com.example.demo.ResponseDTO.SoccerMatchResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.MalformedInputException;
import java.util.List;

@Slf4j
class TournamentMatchServiceTest {
    private TournamentMatchService tournamentMatchService;

    @Mock
    MatchHistoryRepository matchHistoryRepository;
    @Mock
    TournamentRepository tournamentRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tournamentMatchService = new TournamentMatchService(matchHistoryRepository, tournamentRepository);
    }

    @Test
    void updateScoresTest1() throws MalformedInputException {
        String inputLine1 = "teamA teamB 0 1";
        SoccerMatchRequest soccerMatchRequest = SoccerMatchRequest.builder()
                .multilineInput(inputLine1)
                .build();

        SoccerMatchResponse soccerMatchResponse = SoccerMatchResponse.builder()
                .team1("teamA")
                .team2("teamB")
                .team1Goals(0)
                .team2Goals(1)
                .build();

        List<SoccerMatchResponse> expectedOutputList = List.of(soccerMatchResponse);
        List<SoccerMatchResponse> outputList = tournamentMatchService.updateScore(soccerMatchRequest);

        Assertions.assertIterableEquals(expectedOutputList, outputList);
    }

    @Test
    void updateScoresTest2() throws MalformedInputException {
        String inputLine1 = "teamA teamB 0 1\nteamA teamC 1 3\n";
        SoccerMatchRequest soccerMatchRequest = SoccerMatchRequest.builder()
                .multilineInput(inputLine1)
                .build();

        SoccerMatchResponse soccerMatchResponse1 = SoccerMatchResponse.builder()
                .team1("teamA")
                .team2("teamB")
                .team1Goals(0)
                .team2Goals(1)
                .build();

        SoccerMatchResponse soccerMatchResponse2 = SoccerMatchResponse.builder()
                .team1("teamA")
                .team2("teamC")
                .team1Goals(1)
                .team2Goals(3)
                .build();

        List<SoccerMatchResponse> expectedOutputList = List.of(soccerMatchResponse1, soccerMatchResponse2);
        List<SoccerMatchResponse> outputList = tournamentMatchService.updateScore(soccerMatchRequest);

        Assertions.assertIterableEquals(expectedOutputList, outputList);
    }
}