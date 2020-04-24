package jp.voice0726.spring_test_example.service;

import jp.voice0726.spring_test_example.entity.Student;
import jp.voice0726.spring_test_example.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by akinori on 2020/04/21
 *
 * @author akinori
 */
@SpringJUnitConfig
class StudentServiceImplTest {

    @Configuration
    @ComponentScan(
            basePackages = "jp.voice0726.spring_test_example.service",
            useDefaultFilters = false,
            includeFilters = @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = StudentService.class
            )
    )
    static class Config {
    }

    @MockBean
    StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Test
    void findAll() {
        List<Student> expectedList = this.createTestStudentList(20);
        PageImpl<Student> expectedPage = new PageImpl<>(expectedList);
        when(studentRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Student> fetched = studentService.findAll(PageRequest.of(0, 20));
        verify(studentRepository).findAll(any(Pageable.class));
        assertThat(fetched).usingRecursiveComparison()
                .isEqualTo(expectedPage);
    }

    @Test
    void findOneById() {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(new Student()));
        studentService.findOneById(1L);
        verify(studentRepository).findById(any(Long.class));
    }

    @Test
    void findOneByIdThrowsError() {
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> studentService.findOneById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Student record not found");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "test", "test2", "test3"})
    void findBySpecification(String keyword) throws IOException {
        ArgumentCaptor<Specification<Student>> captor = ArgumentCaptor.forClass(Specification.class);
        studentService.findBySpecification(keyword, Pageable.unpaged());
        verify(studentRepository).findAll(captor.capture(), any(Pageable.class));

        Specification<Student> expectedSpec = Specification
                .where(StudentRepository.StudentSpecification.nameContains(keyword))
                .or(StudentRepository.StudentSpecification.studentIdContains(keyword));

        assertThat(serialize(captor.getValue())).hasSameSizeAs(serialize(expectedSpec));
        assertThat(serialize(captor.getValue())).containsExactly(serialize(expectedSpec));

    }

    private List<Student> createTestStudentList(int count) {
        List<Student> expectedList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            expectedList.add(this.createTestStudent(i, "name" + i));
        }
        return expectedList;
    }

    private Student createTestStudent(long id, String name) {
        Student stu = new Student();
        stu.setId(id);
        stu.setName(name);
        stu.setStudentId("test" + id);
        stu.setAdmissionYear(2015);
        stu.setDepartmentId(1L);
        stu.setBirthDate(Date.valueOf(LocalDate.now()));
        return stu;
    }

    private byte[] serialize(Object data) throws IOException {
        byte[] bytes;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(data);
            bytes = bos.toByteArray();
        }

        return bytes;
    }
}
