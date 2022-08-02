package com.example.autos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }

    public AutosList getAutos(String colorOrMake) {
        return null;
    }

    public Automobile addAuto(Automobile auto) {
        return null;
    }

    public Automobile getAuto(String vin) {
        return null;
    }

    public Automobile updateAuto(String vin, String color, String owner) {
        return null;
    }

    public void deleteAuto(String vin) {
    }
}
