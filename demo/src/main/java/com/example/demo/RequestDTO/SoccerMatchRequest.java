package com.example.demo.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SoccerMatchRequest {

    @Column(nullable = false)
    private String multilineInput;

}
