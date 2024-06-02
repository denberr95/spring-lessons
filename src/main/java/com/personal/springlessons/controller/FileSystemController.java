package com.personal.springlessons.controller;

import com.personal.springlessons.service.FileSystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(path = "/file-system")
@RequiredArgsConstructor
public class FileSystemController {

    private final FileSystemService fileSystemService;

    @GetMapping(path = "/free-disk-space")
    public ResponseEntity<Long> getFreeDiskSpace() {
        return ResponseEntity.ok(fileSystemService.getFreeDiskSpace());
    }

    @GetMapping(path = "/total-space")
    public ResponseEntity<Long> getTotalSpace() {
        return ResponseEntity.ok(fileSystemService.getTotalSpace());
    }
}
