package ru.hogwarts.school;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetStudentById() {
        Student student = new Student("Harry Potter", 11);
        ResponseEntity<Student> response = restTemplate.postForEntity("http://localhost:" +
                port + "/student/create", student, Student.class);

        Long studentId = response.getBody().getId();

        ResponseEntity<Student> getResponse = restTemplate.getForEntity("http://localhost:" +
                port + "/student/" + studentId + "/get", Student.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Student retrievedStudent = getResponse.getBody();
        assertThat(retrievedStudent).isNotNull();
        assertThat(retrievedStudent.getId()).isEqualTo(studentId);
        assertThat(retrievedStudent.getName()).isEqualTo("Harry Potter");
        assertThat(retrievedStudent.getAge()).isEqualTo(11);
    }

    @Test
    void testCreateStudent() {
        Student student = new Student("Harry Potter", 11);
        ResponseEntity<Student> response = restTemplate.postForEntity("http://localhost:" +
                port + "/student/create", student, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
        assertThat(response.getBody().getAge()).isEqualTo(11);
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student("Ron Weasley", 12);
        ResponseEntity<Student> createdResponse = restTemplate.postForEntity("http://localhost:" +
                port + "/student/create", student, Student.class);

        Long studentId = createdResponse.getBody().getId();

        Student updatedStudent = new Student("Hermione Granger", 12);
        restTemplate.put("http://localhost:" + port + "/student/" + studentId + "/update", updatedStudent);

        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" +
                port + "/student/" + studentId + "/get", Student.class);
        assertThat(response.getBody().getName()).isEqualTo("Hermione Granger");
        assertThat(response.getBody().getAge()).isEqualTo(12);
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student("Neville Longbottom", 13);
        ResponseEntity<Student> createdResponse = restTemplate.postForEntity("http://localhost:" +
                port + "/student/create", student, Student.class);

        Long studentId = createdResponse.getBody().getId();
        restTemplate.delete("http://localhost:" + port + "/student/" + studentId + "/delete");

        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" +
                port + "/student/" + studentId + "/get", Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetStudentByAge() {
        int age = 11;

        ResponseEntity<Student[]> response = restTemplate.getForEntity("http://localhost:" +
                port + "/student/filterByAge?age=" + age, Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testGetStudentsBetweenAges() {
        int ageMin = 10, ageMax = 20;

        ResponseEntity<Student[]> response = restTemplate.getForEntity("http://localhost:" +
                port + "/student/get/between-age-max-min?ageMin=" + ageMin + "&ageMax=" + ageMax, Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testFindByFacultyId() {
        Faculty faculty = new Faculty("Гриффиндор", "Red");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" +
                port + "/faculty/create", faculty, Faculty.class);

        Long facultyId = facultyResponse.getBody().getId();


        Student student = new Student("Луна Лавгуд", 14);
        student.setFaculty(facultyResponse.getBody());
        restTemplate.postForEntity("http://localhost:" +
                port + "/student/create", student, Student.class);

        ResponseEntity<Student[]> response = restTemplate.getForEntity("http://localhost:" +
                port + "/student/" + facultyId + "/find-students-by-facultyid", Student[].class);

        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
