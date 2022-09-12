package com.example.demo.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class TeamResponse implements Comparable<TeamResponse> {
    @Id
    private String teamName;

    @Column(nullable = false)
    private int teamNumber;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @Column
    private Integer score;


    @Override
    public int compareTo(TeamResponse o) {
        if (Objects.equals(this.getScore(), o.getScore())) {
            return this.registrationDate.compareTo(o.registrationDate);
        }
        else {
            return this.getScore() - o.getScore();
        }
    }
}
