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
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetFacultyById() {
        Faculty faculty = new Faculty("Гриффиндор", "Red");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity("http://localhost:" +
                port + "/faculty/create", faculty, Faculty.class);

        Long facultyId = createResponse.getBody().getId();

        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity("http://localhost:" +
                port + "/faculty/" + facultyId + "/get", Faculty.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Faculty retrievedFaculty = getResponse.getBody();
        assertThat(retrievedFaculty).isNotNull();
        assertThat(retrievedFaculty.getId()).isEqualTo(facultyId);
        assertThat(retrievedFaculty.getName()).isEqualTo("Гриффиндор");
        assertThat(retrievedFaculty.getColor()).isEqualTo("Red");

    }

    @Test
    void testCreateFaculty() {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        ResponseEntity<Faculty> response = restTemplate.postForEntity("http://localhost:" +
                port + "/faculty/create", faculty, Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
    }

    @Test
    void testUpdateFaculty() {
        Faculty faculty = new Faculty("Gryffindor", "Red");
        ResponseEntity<Faculty> response = restTemplate.postForEntity("http://localhost:" +
                port + "/faculty/create", faculty, Faculty.class);

        Long facultyId = response.getBody().getId();
        Faculty updatedFaculty = new Faculty("Slytherin", "Green");

        restTemplate.put("http://localhost:" +
                port + "/faculty/" + facultyId + "/update", updatedFaculty);

        ResponseEntity<Faculty> updateResponse = restTemplate.getForEntity("/faculty/" + facultyId + "/get", Faculty.class);
        assertThat(updateResponse.getBody().getName()).isEqualTo("Slytherin");
        assertThat(updateResponse.getBody().getColor()).isEqualTo("Green");
    }

    @Test
    void testDeleteFaculty() {
        long facultyId = 2;
        restTemplate.delete("http://localhost:" +
                port + "/faculty/" + facultyId + "/delete");

        ResponseEntity<Faculty> response = restTemplate.getForEntity("/faculty/" + facultyId + "/get", Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetFacultiesByColor() {
        String color = "Red";

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("http://localhost:" +
                port + "/faculty/filterByColor?color=" + color, Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testFindFacultiesByColorOrName() {
        String color = "Green";
        String name = "Slytherin";

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity("http://localhost:" +
                port + "/faculty/get/find-By-color-Or-Name?color=" + color + "&Name=" + name, Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testFindFacultyByStudentId() {
        Faculty faculty = new Faculty("Slytherin", "Green");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" +
                port + "/faculty/create", faculty, Faculty.class);

        Student student = new Student("Neville", 13);
        student.setFaculty(facultyResponse.getBody());
        ResponseEntity<Student> createdResponse = restTemplate.postForEntity("http://localhost:" +
                port + "/student/create", student, Student.class);

        Long studentId = createdResponse.getBody().getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity("http://localhost:" +
                port + "/faculty/" + studentId + "/find-faculty-by-studentid", Faculty.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
    }
}


