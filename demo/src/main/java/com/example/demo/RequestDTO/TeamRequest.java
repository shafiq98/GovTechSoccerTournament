package com.example.demo.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TeamRequest {
    @Column(nullable = false)
    private String multilineInput;
}
