package com.example.demo.Repository;

import com.example.demo.ResponseDTO.TeamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<TeamResponse, String> {
}
