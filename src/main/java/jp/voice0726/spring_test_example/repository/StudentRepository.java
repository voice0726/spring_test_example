package jp.voice0726.spring_test_example.repository;

import jp.voice0726.spring_test_example.entity.Student;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    class StudentSpecification {
        public static Specification<Student> nameContains(String keyword) {
            return (root, cq, cb) -> cb.like(root.get("name"), "%" + keyword + "%");
        }

        public static Specification<Student> studentIdContains(String keyword) {
            return (root, cq, cb) -> cb.like(root.get("studentId"), "%" + keyword + "%");
        }
    }
}
