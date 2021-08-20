package com.java.yaml;

import com.java.yaml.model.Input;
import com.java.yaml.parser.YAMLParserService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@SpringBootApplication
@RestController
public class SpringBootK8sApplication {

    @Autowired
    YAMLParserService service;

    @Autowired
    public static void main(String[] args) {
        SpringApplication.run(SpringBootK8sApplication.class, args);
    }

    @PostMapping(value = "/generate", consumes = "application/json")
    public ResponseEntity<String> generate(@RequestBody List<Input> inputs) throws GitAPIException, IOException, URISyntaxException {
        service.invoke(inputs);
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
