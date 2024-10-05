package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSchoolProjectApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void testGetFaculty() throws Exception {
        Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" +
                port + "/faculty", String.class)).isNotNull();
    }

    @Test
    void getIdFacultyTest() throws Exception{
        long facultyId = 1;
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity("http://localhost:" +
                port + "/faculty" + "/{id}/get" + facultyId, Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        //Assertions.assertThat(this.testRestTemplate.getForObject("http://localhost:" +
          //      port + "/faculty" + "/{id}/get", Faculty.class)).isEqualTo(facultyId);
    }
}
