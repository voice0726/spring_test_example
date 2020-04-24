package jp.voice0726.spring_test_example.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentIndexDto {
    private final long id;
    private final String studentId;
    private final String name;
    private final String department;
}
