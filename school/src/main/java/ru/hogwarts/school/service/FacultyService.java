package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {
    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long idcounter = 1;

    public Faculty createFaculty(String name, String color) {
        Faculty faculty = new Faculty(idcounter++, name, color);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty getFaculty(Long id) {
        return faculties.get(id);
    }

    public Faculty updateFaculty(Long id, String name, String color) {
        Faculty faculty = faculties.get(id);
        if (faculty != null) {
            faculty.setName(name);
            faculty.setColor(color);
        }
        return faculty;
    }

    public Faculty deleteFaculty(Long id) {
        return faculties.remove(id);
    }

    public Collection<Faculty> getAllFaculties(){
        return faculties.values();
    }
}
