package com.example.autos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutosService {


    AutosRepository autosRepository;
    // constructor injection:
    public AutosService(AutosRepository autosRepository) {
        this.autosRepository = autosRepository;
    }

    public AutosList getAutos() {
        // query: select * from autos;
        // put that in a list
        // return a new AutosList with the list
        return new AutosList(autosRepository.findAll());
    }

    public AutosList getAutos(String color, String make){
        List<Automobile> automobiles = autosRepository.findByColorContainsAndMakeContains(color, make);
        if (!automobiles.isEmpty()) {
            return new AutosList(automobiles);
        }
        return null;
    }

    public AutosList getAutos(String colorOrMake) {
        return null;
    }

    public Automobile addAuto(Automobile auto) {
        return autosRepository.save(auto);
    }

    public Automobile getAuto(String vin) {
        return autosRepository.findByVin(vin).orElse(null);
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        return null;
    }

    public void deleteAuto(String vin) {
    }
}
