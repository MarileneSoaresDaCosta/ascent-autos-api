package com.example.autos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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


    // GET /api/autos
        // GET: /api/autos returns 200 - list of all autos
    @Test
    void getAutos_noParams_exists_returnsAutosList() throws Exception {
        // Arrange
        List<Automobile> automobiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            automobiles.add(new Automobile(1967+i, "Ford", "Mustang", "AABB"+i));
        }

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
            // json obj with list of matching autos: { "automobiles": [ {}, {} ] }
        // GET: /api/autos?color=BLUE&make=Toyota returns 204 (no autos found)
        // GET: /api/autos?color=RED returns 200 - returns red cars
        // GET: /api/autos?make=Ford returns 200 - returns Ford cars


    // POST /api/autos - request body with car info
        // POST: /api/autos - returns 200 - auto added successfully to DB
        // POST: /api/autos - returns 400 - error message - bad request


    // GET /api/autos/{vin}
        // GET: /api/autos/{vin} - returns 200 - auto found
        // GET: /api/autos/{vin} - returns 204 - auto not found


    // PATCH /api/autos/{vin} - request body with color and owner
        // PATCH: /api/autos/{vin} - return 200 - successfully updated
        // PATCH: /api/autos/{vin} - return 204 - auto not found
        // PATCH: /api/autos/{vin} - return 400 - error message - bad request


    // DELETE /api/autos/{vin}
        // DELETE: /api/autos/{vin} - returns 202 - auto delete request accepted
        // DELETE: /api/autos/{vin} - returns 204 - auto not found
}
