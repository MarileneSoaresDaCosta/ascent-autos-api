package com.example.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutosServiceTest {
    private AutosService autosService;

    @Mock
    AutosRepository autosRepository;

    @BeforeEach
    void setUp() {
        autosService = new AutosService(autosRepository);
    }

    @Test
    void getAutos() {
        Automobile automobile =  new Automobile(1967, "Ford", "Mustang", "AABBDD");
        when(autosRepository.findAll()).thenReturn(Arrays.asList(automobile));
        AutosList autoList = autosService.getAutos();
        assertThat(autoList).isNotNull();
        assertThat(autoList.isEmpty()).isFalse();
    }

    @Test
    void testGetAutos() {
    }

    @Test
    void testGetAutos1() {
    }

    @Test
    void addAuto() {
    }

    @Test
    void getAuto() {
    }

    @Test
    void updateAuto() {
    }

    @Test
    void deleteAuto() {
    }
}