package jp.voice0726.spring_test_example.service;

import jp.voice0726.spring_test_example.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Created by akinori on 2020/04/21
 *
 * @author akinori
 */
@Service
public interface StudentService {
    /**
     * DBからstudentエンティティをすべて取得し返します。
     * @param pageable pageable
     * @return Studentページ
     */
    Page<Student> findAll(Pageable pageable);

    /**
     * DBからID指定でstudentエンティティを1つ取得し返します。
     * @param id id
     * @return Studentエンティティ
     */
    Student findOneById(long id);

    /**
     * DBからキーワード指定でstudentエンティティをすべて取得し返します。
     *
     * @param keyword  キーワード
     * @param pageable pageable
     * @return Studentページ
     */
    Page<Student> findBySpecification(String keyword, Pageable pageable);
}
