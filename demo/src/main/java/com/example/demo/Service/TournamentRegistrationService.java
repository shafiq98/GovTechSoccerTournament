package com.example.demo.Service;

import com.example.demo.Repository.TournamentRepository;
import com.example.demo.RequestDTO.TeamRequest;
import com.example.demo.ResponseDTO.TeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.MalformedInputException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
public class TournamentRegistrationService {
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentRegistrationService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public List<TeamResponse> registerTeams(TeamRequest teamRequest) throws MalformedInputException, NumberFormatException, DateTimeParseException {
        String[] teams = teamRequest.getMultilineInput().split("\n");

        for (String s : teams) {
            log.debug("String s : " + s);
        }

        List<TeamResponse> teamList = new ArrayList<>();

        for (String team : teams) {
            String[] arguments = team.split(" ");
            // check if each line has 3 arguments
            if (arguments.length != 3) {
                log.debug("Single line does not have enough arguments");
                throw new MalformedInputException(arguments.length);
            }
            else {
                TeamResponse i = TeamResponse.builder()
                        .teamName(arguments[0])
                        .registrationDate(dateParser(arguments[1]))
                        .teamNumber(Integer.parseInt(arguments[2]))
                        .numOfWins(0)
                        .numOfDraws(0)
                        .numOfLoss(0)
                        .score(0)
                        .build();

                teamList.add(i);
            }
        }
        tournamentRepository.saveAll(teamList);
        return teamList;
    }

    public List<TeamResponse> getAllTeams() {
        return tournamentRepository.findAll();
    }

    private LocalDate dateParser(String dateInput) throws DateTimeParseException {
        dateInput += ("/" + Calendar.getInstance().get(Calendar.YEAR));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        return LocalDate.parse(dateInput, formatter);
    }
    public void purgeData() {
        tournamentRepository.deleteAll();
    }
}
