package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService{
    private final Map<Long, Student> students = new HashMap<>();
    private long idcounter = 1;

    public Student createStudent(String name, int age) {
        Student student = new Student(idcounter++, name, age);
        students.put(student.getId(), student);
        return student;
    }

    public Student getStudent(Long id) {
        return students.get(id);
    }

    public Student updateStudent(Long id, String name, int age) {
        Student student = students.get(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
        }
        return student;
    }

    public Student deleteStudent(Long id) {
        return students.remove(id);
    }

    public Collection<Student> getAllStudents(){
        return students.values();
    }
}
