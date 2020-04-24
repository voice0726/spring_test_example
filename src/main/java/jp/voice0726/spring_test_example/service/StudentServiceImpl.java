package jp.voice0726.spring_test_example.service;

import jp.voice0726.spring_test_example.entity.Student;
import jp.voice0726.spring_test_example.repository.StudentRepository;
import jp.voice0726.spring_test_example.repository.StudentRepository.StudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Created by akinori on 2020/04/21
 *
 * @author akinori
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Page<Student> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Student findOneById(long id) {
        Optional<Student> op = studentRepository.findById(id);
        return op.orElseThrow(() -> new EntityNotFoundException("Student record not found"));
    }

    @Override
    public Page<Student> findBySpecification(String keyword, Pageable pageable) {
        return studentRepository.findAll(Specification
                .where(StudentSpecification.nameContains(keyword))
                .or(StudentSpecification.studentIdContains(keyword)
                ), pageable);
    }
}
