package jp.voice0726.spring_test_example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class StudentProfileDto {
    private final long id;
    private final String name;
    private final String studentId;
    private final LocalDate birthDate;
    private final int admissionYear;
    private final String department;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
