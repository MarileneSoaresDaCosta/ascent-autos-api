package com.example.autos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    void getAutos_noArgs_returnsList() {
        Automobile automobile =  new Automobile(1967, "Ford", "Mustang", "AABBDD");
        when(autosRepository.findAll()).thenReturn(Arrays.asList(automobile));
        AutosList autoList = autosService.getAutos();
        assertThat(autoList).isNotNull();
        assertThat(autoList.isEmpty()).isFalse();
    }

    @Test
    void getAutos_search_returnsList() {
        Automobile automobile =  new Automobile(1967, "Ford", "Mustang", "AABBDD");
        automobile.setColor("RED");
        when(autosRepository.findByColorContainsAndMakeContains(anyString(), anyString())).thenReturn(Arrays.asList(automobile));
        AutosList autoList = autosService.getAutos("RED", "Ford");
        assertThat(autoList).isNotNull();
        assertThat(autoList.isEmpty()).isFalse();
    }

    // test for getAutos with a single param
    @Test
    void testGetAutos1() {
    }

    @Test
    void addAuto_valid_returnsAuto() {
        Automobile automobile =  new Automobile(1967, "Ford", "Mustang", "AABBDD");
        automobile.setColor("RED");
        when(autosRepository.save(any(Automobile.class)))
                .thenReturn(automobile);
        Automobile auto = autosService.addAuto(automobile);
        assertThat(auto).isNotNull();
        assertThat(auto.getMake()).isEqualTo("Ford");
    }

    // get auto with vin
    @Test
    void getAuto_withVin_returnsAuto() {
        Automobile automobile =  new Automobile(1967, "Ford", "Mustang", "AABBDD");
        automobile.setColor("RED");
        when(autosRepository.findByVin(anyString())).thenReturn(Optional.of(automobile));
        Automobile auto = autosService.getAuto("MYVINY");
        assertThat(auto).isNotNull();
        assertThat(auto.getVin()).isEqualTo(automobile.getVin());
    }

    @Test
    void updateAuto() {
    }

    @Test
    void deleteAuto() {
    }
}