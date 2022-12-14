package com.example.demo.Service;

import com.example.demo.Repository.TournamentRepository;
import com.example.demo.RequestDTO.TeamRequest;
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
import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TournamentRegistrationServiceTest {

    private TournamentRegistrationService tournamentRegistrationService;

    @Mock
    TournamentRepository tournamentRepository;

    @BeforeEach
    public void setUp() {
        tournamentRegistrationService = new TournamentRegistrationService(tournamentRepository);
    }

    @Test
    void registerTeamsTest1() throws MalformedInputException {

        String inputLine1 = "teamA 01/04 1";
        TeamRequest teamRequest = TeamRequest.builder()
                .multilineInput(inputLine1)
                .build();

        TeamResponse expectedOutput = TeamResponse.builder()
                .teamName("teamA")
                .registrationDate(LocalDate.parse("2022-04-01"))
                .teamNumber(1)
                .build();
        List<TeamResponse> expectedOutputList = List.of(expectedOutput);

        List<TeamResponse> outputList = tournamentRegistrationService.registerTeams(teamRequest);
        Assertions.assertEquals(expectedOutputList.get(0), outputList.get(0));
    }

    @Test
    void registerTeamsTest2() throws MalformedInputException {

        String inputLine = "teamA 01/04 1\nteamB 02/05 1";
        TeamRequest teamRequest = TeamRequest.builder()
                .multilineInput(inputLine)
                .build();

        TeamResponse expectedOutput1 = TeamResponse.builder()
                .teamName("teamA")
                .registrationDate(LocalDate.parse("2022-04-01"))
                .teamNumber(1)
                .build();

        TeamResponse expectedOutput2 = TeamResponse.builder()
                .teamName("teamB")
                .registrationDate(LocalDate.parse("2022-05-02"))
                .teamNumber(1)
                .build();


        List<TeamResponse> expectedOutputList = List.of(expectedOutput1, expectedOutput2);

        List<TeamResponse> outputList = tournamentRegistrationService.registerTeams(teamRequest);
        log.debug(String.valueOf(outputList));
        Assertions.assertIterableEquals(expectedOutputList, outputList);
    }

    @Test
    void registerTeamsInvalidInputTestCase1() {

        String inputLine = "teamA 01/04 1\t";
        TeamRequest teamRequest = TeamRequest.builder()
                .multilineInput(inputLine)
                .build();

        NumberFormatException thrown = Assertions.assertThrows(
                NumberFormatException.class,
                () -> {
                    tournamentRegistrationService.registerTeams(teamRequest);
                }
        );
        log.error(thrown.getMessage());
    }

    @Test
    void registerTeamsInvalidInputTestCase2() {

        String inputLine = "teamA 13/13 1\n";
        TeamRequest teamRequest = TeamRequest.builder()
                .multilineInput(inputLine)
                .build();

        DateTimeParseException thrown = Assertions.assertThrows(
                DateTimeParseException.class,
                () -> {
                    tournamentRegistrationService.registerTeams(teamRequest);
                }
        );
        log.error(thrown.getMessage());
    }

    @Test
    void registerTeamsInvalidInputTestCase3() {

        String inputLine = "teamA 01/04 1\nteamB 02/05";
        TeamRequest teamRequest = TeamRequest.builder()
                .multilineInput(inputLine)
                .build();

        MalformedInputException thrown = Assertions.assertThrows(
                MalformedInputException.class,
                () -> {
                    tournamentRegistrationService.registerTeams(teamRequest);
                }
        );
        log.error(thrown.getMessage());
    }
}