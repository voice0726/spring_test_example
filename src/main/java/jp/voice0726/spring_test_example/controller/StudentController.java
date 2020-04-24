package jp.voice0726.spring_test_example.controller;

import jp.voice0726.spring_test_example.dto.StudentIndexDto;
import jp.voice0726.spring_test_example.dto.StudentProfileDto;
import jp.voice0726.spring_test_example.entity.Student;
import jp.voice0726.spring_test_example.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by akinori on 2020/04/21
 *
 * @author akinori
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/students/")
public class StudentController {

    private final StudentService studentService;

    /**
     * 学生一覧のページを表示します。
     *
     * @param mav      model and view
     * @param pageable pageable
     * @return model and view
     */
    @GetMapping
    public ModelAndView index(ModelAndView mav, Pageable pageable) {
        Page<Student> students = studentService.findAll(pageable);
        PageWrapper<Student> page = new PageWrapper<>(students, "/students/");
        List<StudentIndexDto> dtos = convertToIndexDtoList(students.getContent());
        mav.addObject("students", dtos);
        mav.addObject("page", page);
        mav.setViewName("/students/index");
        return mav;
    }

    /**
     * 学生の詳細プロフィールを表示します。
     *
     * @param mav Model and view
     * @param id  id
     * @return model and view
     */
    @GetMapping("/{studentId}")
    public ModelAndView profile(ModelAndView mav, @PathVariable("studentId") long id) {
        Student student = studentService.findOneById(id);
        StudentProfileDto dto = convertToProfileDto(student);
        mav.addObject("profile", dto);
        mav.setViewName("/students/profile");
        return mav;
    }

    /**
     * Studentエンティティを目次表示用のDTOに変換します。
     *
     * @param student Studentエンティティ
     * @return 目次表示用DTO
     */
    private StudentIndexDto convertToIndexDto(Student student) {
        return StudentIndexDto.builder()
                .id(student.getId())
                .name(student.getName())
                .studentId(student.getStudentId())
                .department(student.getDepartment().getName())
                .build();
    }

    /**
     * Studentエンティティのリストを目次表示用DTOのリストに変換します。
     *
     * @param list エンティティのリスト
     * @return DTOのリスト
     */
    private List<StudentIndexDto> convertToIndexDtoList(List<Student> list) {
        return list.stream().map(this::convertToIndexDto).collect(Collectors.toList());
    }

    /**
     * Studentエンティティを詳細表示用のDTOに変換します。
     *
     * @param student Studentエンティティ
     * @return 目次表示用DTO
     */
    private StudentProfileDto convertToProfileDto(Student student) {
        return StudentProfileDto.builder()
                .id(student.getId())
                .name(student.getName())
                .studentId(student.getStudentId())
                .birthDate(student.getBirthDate().toLocalDate())
                .admissionYear(student.getAdmissionYear())
                .department(student.getDepartment().getName())
                .build();
    }
}
