package com.example.demo.ResponseDTO;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @Column
    private Integer numOfWins;

    @Column
    private Integer numOfLoss;

    @Column
    private Integer numOfDraws;


    @Override
    public int compareTo(TeamResponse o) {
        if (Objects.equals(this.getScore(), o.getScore())) {
            return this.registrationDate.compareTo(o.registrationDate);
        }
        else {
            return this.getScore() - o.getScore();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TeamResponse that = (TeamResponse) o;
        return teamName != null && Objects.equals(teamName, that.teamName);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
