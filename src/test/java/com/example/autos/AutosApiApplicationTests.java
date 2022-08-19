package com.example.autos;

import com.example.autos.security.JwtProperties;
import com.example.autos.security.TokenHelper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class AutosApiApplicationTests {


    @Autowired
    TestRestTemplate restTemplate;
    private RestTemplate patchRestTemplate;

    @Autowired
    AutosRepository autosRepository;

    @Autowired
    JwtProperties jwtProperties;

    TokenHelper tokenHelper;

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
    void getAutos_exists_returnsAutosList(){
//        ResponseEntity<AutosList> response= restTemplate.getForEntity("/api/autos", AutosList.class);
        ResponseEntity<AutosList> response =
                restTemplate.exchange("/api/autos", HttpMethod.GET, getPostRequestHeaders(), AutosList.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isEmpty()).isFalse();

        for (Automobile auto : response.getBody().getAutomobiles()) {
            System.out.println(auto);
        }
    }

    public HttpEntity<String> getPostRequestHeaders() {
        List acceptTypes = new ArrayList();
        acceptTypes.add(MediaType.APPLICATION_JSON);

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON);
        reqHeaders.setAccept(acceptTypes);
//        reqHeaders.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGdsYWIuY29tIiwiZmlyc3RfbmFtZSI6IkdlbmVyYWwiLCJsYXN0X25hbWUiOiJVc2VyIiwiZW1haWwiOiJ1c2VyQGdsYWIuY29tIiwiZ3VpZCI6MiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY2MDgzNjkzNiwiZXhwIjoxNjYwODgwMTM2fQ.DT5x8vIpKwFnefVu5suolw4uBYNVWqu0ocqjZtZ-XseYDEa39ZVE6Flc8ycgeRRlOxiahjy4ZytHccHQ9r5Ozg");
        reqHeaders.add("Authorization", tokenHelper.getToken("me",
                Arrays.asList(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER"))));

        return new HttpEntity(reqHeaders);
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

    // PATCH: /api/autos/{vin}
    @Test
    void updateAuto_ColorAndOwner_returnsAuto() throws Throwable{
        UpdateOwnerRequest updateOwnerRequest = new UpdateOwnerRequest("PINK", "Kitty");
        int seq = r.nextInt(50);
        String vin = testAutos.get(seq).getVin();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<UpdateOwnerRequest> request = new HttpEntity<>(updateOwnerRequest, headers);

        // Act
        // Use patchRestTemplate to make call with PATCH method
        ResponseEntity<Automobile> response = restTemplate.exchange(String.format("/api/autos/%s", vin), HttpMethod.PATCH, request, Automobile.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody().getColor()).isEqualTo("PINK");
        assertThat(response.getBody().getOwner()).isEqualTo("Kitty");
        System.out.println(response.getBody());

    }


    // delete?
}
