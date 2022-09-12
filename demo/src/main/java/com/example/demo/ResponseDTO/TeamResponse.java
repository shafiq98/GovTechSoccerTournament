package com.example.demo.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class TeamResponse {
    @Id
    private String teamName;

    @Column(nullable = false)
    private int teamNumber;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @Column
    private int score;

}
