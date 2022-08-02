package com.example.autos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutosControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AutosService autoService;

    ObjectMapper mapper = new ObjectMapper();


    // GET /api/autos
        // GET: /api/autos returns 200 - list of all autos
    @Test
    void getAutos_noParams_exists_returnsAutosList() throws Exception {
        // Arrange
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1967+i, "Ford", "Mustang", "AABB"+i));
        }
//        System.out.println(automobiles.toString());
        when(autoService.getAutos()).thenReturn(new AutosList(automobiles));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos"))
                .andDo(print())
            // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }


    // GET: /api/autos returns code 204 (no autos found)
    @Test
    void getAutos_noParams_none_returnsNoContent() throws Exception {
        // Arrange
        when(autoService.getAutos()).thenReturn(new AutosList());
        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos"))
                .andDo(print())
                // Assert
                .andExpect(status().isNoContent());
    }

    // GET: /api/autos?color=RED&make=Ford returns 200 - at least one auto
    @Test
    void getAutos_searchParams_exists_returnsAutosList() throws Exception {
        // OBS: just checking if search happens - endpoint accepts 2 params, not what it returns
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1967+i, "Ford", "Mustang", "AABB"+i));
        }
        when(autoService.getAutos(anyString(), anyString())).thenReturn(new AutosList(automobiles));
        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos?color=RED&make=Ford"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // GET: /api/autos?color=BLUE&make=Toyota returns 204 (no autos found)
    @Test
    void getAutos_searchParams_none_returnsNoContent() throws Exception {
        when(autoService.getAutos(anyString(), anyString())).thenReturn(new AutosList());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos?color=BLUE&make=Toyota"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // GET: /api/autos?color=RED returns 200 - returns red cars
    @Test
    void getAutos_searchParams_exists_returnsRedCars() throws Exception {
        // tests only whether getAutos accepts a single param color
        // Arrange
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1967+i, "Ford", "Mustang", "AABB"+i));
        }
        when(autoService.getAutos("RED")).thenReturn(new AutosList(automobiles));
        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos?color=RED"))
                .andDo(print())
        // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }

    // GET: /api/autos?make=Ford returns 200 - returns Ford cars
    @Test
    void getAutos_searchParams_exists_returnsFordCars() throws Exception {
        // tests only whether getAutos accepts a single param color
        // Arrange
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1967+i, "Ford", "Mustang", "AABB"+i));
        }
        when(autoService.getAutos("Ford")).thenReturn(new AutosList(automobiles));
        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos?make=Ford"))
                .andDo(print())
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }


    // POST /api/autos - request body with car info
        // POST: /api/autos - returns 200 - auto added successfully
    @Test
    void addAuto_valid_returnsAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBDD");
//        String json = "{\"year\":1967,\"make\":\"Ford\",\"model\":\"Mustang\",\"color\":null,\"owner\":null,\"vin\":\"AABBDD\"}";
        when(autoService.addAuto(any(Automobile.class))).thenReturn(automobile);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/autos")
                        .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(automobile)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("make").value("Ford"));
    }

        // POST: /api/autos - returns 400 - error message - bad request
    @Test
    void addAuto_badRequest_returns400() throws Exception {
        when(autoService.addAuto(any(Automobile.class))).thenThrow(InvalidAutoException.class);
        String json = "{\"year\":1967,\"make\":\"Ford\",\"model\":\"Mustang\",\"color\":null,\"owner\":null,\"vin\":\"AABBDD\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/autos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    // GET /api/autos/{vin} >> here vin is not a parameter, but part of the path!
        // GET: /api/autos/{vin} - returns 200 - auto found
    @Test
    void getAuto_withVin_returnsAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBDD");
        when(autoService.getAuto(anyString())).thenReturn(automobile);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos/" + automobile.getVin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("vin").value(automobile.getVin()));
    }
        // GET: /api/autos/{vin} - returns 204 - auto not found
    @Test
    void getAuto_withVin_none_returnsNoContent() throws Exception {
        when(autoService.getAuto(anyString())).thenReturn(new Automobile());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos/"  + "AABBCC"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // PATCH /api/autos/{vin} - request body with color and owner
        // PATCH: /api/autos/{vin} - return 200 - successfully updated
    @Test
    void updateAuto_withObj_returnsAuto() throws Exception {
        Automobile automobile = new Automobile(1967, "Ford", "Mustang", "AABBDD");
        when(autoService.updateAuto(anyString(), anyString(), anyString() ))
                .thenReturn(automobile);
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/autos/"
                + automobile.getVin())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"color\":\"RED\",\"owner\":\"Bob\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("color").value("RED"))
                .andExpect(jsonPath("owner").value("Bob"));

    }
        // PATCH: /api/autos/{vin} - return 204 - auto not found
        // PATCH: /api/autos/{vin} - return 400 - error message - bad request


    // DELETE /api/autos/{vin}
        // DELETE: /api/autos/{vin} - returns 202 - auto delete request accepted
        // DELETE: /api/autos/{vin} - returns 204 - auto not found
}
