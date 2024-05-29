package com.personal.springlessons.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.annotation.Timed;


@RestController
@RequestMapping(path = "/file-system")
public class FileSystemController {
    
    @Timed
    @GetMapping(path = "/ok")
    public ResponseEntity<String> ok() {
        return ResponseEntity.ok("OK");
    }
}
