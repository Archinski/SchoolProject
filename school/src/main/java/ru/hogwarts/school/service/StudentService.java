package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        studentRepository.save(student);
        return student;
    }

    public Student getStudent(long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student updateStudent(Student student, Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        student.setId(id);
        studentRepository.save(student);
        return student;
    }

    public Student deleteStudent(long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return student;
    }

    public List<Student> getStudentByAge(int age) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    public List<Student> findByAgeBetween(int ageMin, int ageMax) {
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    public List<Student> findByFacultyId(long id) {
        return studentRepository.findByFacultyId(id);
    }

    public Faculty findByStudentId(long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)).getFaculty();
    }

    public Integer getAllStudent() {
        return studentRepository.findByAllStudentName();
    }

    public Integer findAvgStudents() {
        return studentRepository.findAvgStudents();
    }

    public List<Student> findFiveLastStudents() {
        return studentRepository.findFiveLastStudents();
    }
}
