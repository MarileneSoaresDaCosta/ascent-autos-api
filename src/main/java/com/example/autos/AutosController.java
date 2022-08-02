package com.example.autos;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AutosController {

    AutosService autosService;
    // constructor injection:
    public AutosController(AutosService autosService) {
        this.autosService = autosService;
    }
    @GetMapping("/api/autos")
    public ResponseEntity<AutosList>  getAutos(
            @RequestParam(name = "color", required=false) String color,
            @RequestParam(name = "make", required=false) String make){
        AutosList autosList;
        if (color == null && make == null) {
            autosList = autosService.getAutos();
        } else if (color instanceof String && make == null) {
            autosList = autosService.getAutos(color);
        } else if (color == null && make instanceof String) {
            autosList = autosService.getAutos(make);
        } else {
            autosList = autosService.getAutos(color, make);
        }

        return autosList.isEmpty() ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(autosList);
    }

    @PostMapping("/api/autos")
    public Automobile addauto(@RequestBody Automobile auto){
        return autosService.addAuto(auto);
    }

    @GetMapping("/api/autos/{vin}")
    public ResponseEntity<Automobile> getAuto(@PathVariable String vin){
        Automobile auto = autosService.getAuto(vin);
        return auto.getVin() == null ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(auto);
    }

    @PatchMapping("/api/autos/{vin}")
    public ResponseEntity<Automobile> updateAuto(
            @PathVariable String vin,
            @RequestBody UpdateOwnerRequest update) {
        Automobile automobile = autosService.updateAuto(vin, update.getColor(), update.getOwner());
        automobile.setColor(update.getColor());
        automobile.setOwner(update.getOwner());
        return automobile.getVin() == null ? ResponseEntity.noContent().build() :
                ResponseEntity.ok(automobile);
    }

    @DeleteMapping("/api/autos/{vin}")
    public ResponseEntity deleteAuto(@PathVariable String vin){
        autosService.deleteAuto(vin);
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidAutoExceptionHandler(InvalidAutoException e){
    }


}
