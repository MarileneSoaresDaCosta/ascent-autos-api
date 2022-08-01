package com.example.autos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutosController.class)
public class AutosControllerTest {
    @Autowired
    MockMvc mockMvc;
    // GET /api/autos
        // GET: /api/autos returns 200 - list of all autos
    @Test
    public void getAutos_noParams_exists_returnsAutosList() throws Exception {
        // Arrange
        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/autos"))
            // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.automobiles", hasSize(5)));
    }


        // GET: /api/autos returns code 204 (no autos found)
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
