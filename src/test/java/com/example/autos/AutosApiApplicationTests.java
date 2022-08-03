package com.example.autos;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AutosApiApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
    private RestTemplate patchRestTemplate;

    @Autowired
    AutosRepository autosRepository;

    Random r = new Random();
    List<Automobile> testAutos;

    @BeforeEach
    void setUp(){
        this.testAutos = new ArrayList<>();
        Automobile auto;
        String[] colors = {"RED", "BLUE", "GREEN", "ORANGE", "YELLOW", "BLACK", "BROWN",
        "ROOT BEER", "MAGENTA", "AMBER"};

        for (int i = 0; i < 50; i++) {
            if ( i % 3 == 0 ) {
                auto = new Automobile(1967, "Ford", "Mustang", "AABBCC" + (i * 13));
                auto.setColor(colors[r.nextInt(10)]);
            } else if ( i % 2 == 0 ) {
                auto = new Automobile(2000, "Dodge", "Viper", "VVBBXX" + (i * 12));
                auto.setColor(colors[r.nextInt(10)]);
            } else {
                auto = new Automobile(2020, "Audi", "Quatro", "QQZZAA" + (i * 12));
                auto.setColor(colors[r.nextInt(10)]);
            }
            this.testAutos.add(auto);
        }
        autosRepository.saveAll(this.testAutos);

        // setup for PATCH
        // Add Apache HttpClient as TestRestTemplate
        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

    }

    @AfterEach
    void tearDown() {
        autosRepository.deleteAll();
    }
    // the test below was commented out until integration tests were added here
	@Test
	void contextLoads() {
	}

    @Test
    void getAutos_exits_returnsAutosList(){
        ResponseEntity<AutosList> response= restTemplate.getForEntity("/api/autos", AutosList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();

        for (Automobile auto : response.getBody().getAutomobiles()) {
            System.out.println(auto);
        }
    }

    @Test
    void getAutos_search_returnsAutosList(){
        int seq = r.nextInt(50);
        String color = testAutos.get(seq).getColor();
        String make = testAutos.get(seq).getMake();

        ResponseEntity<AutosList> response= restTemplate.getForEntity(
                String.format("/api/autos?color=%s&make=%s", color, make), AutosList.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();

        for (Automobile auto : response.getBody().getAutomobiles()) {
            System.out.println(auto);
        }
    }

    @Test
    void addAuto_returnsNewAutoDetails(){
        // Arrange
        Automobile automobile = new Automobile();
        automobile.setVin("ABC123XX");
        automobile.setYear(1995);
        automobile.setMake("Ford");
        automobile.setModel("Windstar");
        automobile.setColor("Blue");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Automobile> request = new HttpEntity<>(automobile, headers);

        // Act
        ResponseEntity<Automobile> response = restTemplate.postForEntity("/api/autos", request, Automobile.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getVin()).isEqualTo(automobile.getVin());
    }

    // patch
    /*
    @Test
    void updateAuto_ColorAndOwner_returnsAuto(){
        // Arrange: create new auto and POST
        Automobile automobile = new Automobile();
        automobile.setVin("XYZ123XX");
        automobile.setYear(2004);
        automobile.setMake("Lexus");
        automobile.setModel("RX330");
        automobile.setColor("GREY");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Automobile> request = new HttpEntity<>(automobile, headers);
        ResponseEntity<Automobile> response = restTemplate.postForEntity("/api/autos", request, Automobile.class);

        // create json obj
        String json = "{\"color\":\"SILVER\",\"owner\":\"Bob\"}";
        HttpHeaders headersPatch = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        headersPatch.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        // Use patchRestTemplate to make call with PATCH method
        ResponseEntity<Automobile> responseUpdated = patchRestTemplate.exchange("/api/autos", HttpMethod.PATCH, entity);

        // Ensure Status Code is 200 OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Ensure Content-Type is application/json
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());

        // Ensure that PATCH updated color and owner
        // from "NotRyan" to "Ryan"
        Automobile autoUpdated = responseEntity.getBody();
        assertEquals("SILVER", autoUpdated.getColor());
        assertEquals("Bob", autoUpdated.getOwner());

    }


    // delete?

     */
}
