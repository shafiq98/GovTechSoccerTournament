package com.example.demo.Controller;

import com.example.demo.Repository.TournamentRepository;
import com.example.demo.RequestDTO.TeamRequest;
import com.example.demo.ResponseDTO.TeamResponse;
import com.example.demo.Service.TournamentRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
class TournamentRegistrationControllerTest {

    private TournamentRegistrationController tournamentRegistrationController;
    @Mock
    TournamentRegistrationService tournamentRegistrationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tournamentRegistrationController = new TournamentRegistrationController(tournamentRegistrationService);
    }

    @Test
    void testCase1() {
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
        ResponseEntity<List<TeamResponse>> expectedOutputObject = ResponseEntity.ok(expectedOutputList);

        ResponseEntity<List<TeamResponse>> output = tournamentRegistrationController.registerTeams(teamRequest);
        log.debug(Objects.requireNonNull(output.getBody()).toString());

    }
}