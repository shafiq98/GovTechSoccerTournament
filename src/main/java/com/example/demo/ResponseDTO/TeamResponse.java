package com.example.demo.ResponseDTO;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    private Integer score;

    @Column
    private Integer numOfWins;

    @Column
    private Integer numOfLoss;

    @Column
    private Integer numOfDraws;

    public static Comparator<TeamResponse> getDateComparator() {
        // compare using date attribute
        return Comparator.comparing(TeamResponse::getRegistrationDate);
    }

    public static Comparator<TeamResponse> getScoreComparator() {
        // compare using score attribute
        return (o1, o2) -> o2.getScore() - o1.getScore();
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
