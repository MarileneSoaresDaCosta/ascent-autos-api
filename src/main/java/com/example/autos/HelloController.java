package com.example.autos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    MyConfig myConfig;

    public HelloController(MyConfig myConfig){
        this.myConfig = myConfig;
    }

    @GetMapping("/api/autos/hello")
    public String sayHello(){
        return myConfig.getMessage();
    }


}
