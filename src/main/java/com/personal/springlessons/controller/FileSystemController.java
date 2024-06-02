package com.personal.springlessons.controller;

import com.personal.springlessons.model.dto.FileInformationRequest;
import com.personal.springlessons.service.FileSystemService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping(path = "/file-system")
@RequiredArgsConstructor
@Validated
public class FileSystemController {

    private final FileSystemService fileSystemService;
    private final Tracer tracer;

    @GetMapping(path = "/free-disk-space")
    public ResponseEntity<Long> getFreeDiskSpace() {
        log.info("Start API get free disk space");
        Long result = null;
        final Span newSpan = tracer.nextSpan().name("freeDiskSpaceController");
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan.start())) {
            result = fileSystemService.getFreeDiskSpace();
            newSpan.tag("diskSpaceValue", result);
            newSpan.event("freeSpaceCalculated");
        } finally {
            newSpan.end();
        }
        log.info("End API get free disk space");
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/total-space")
    public ResponseEntity<Long> getTotalSpace() {
        log.info("Start API get total space");
        final Long result = null;
        fileSystemService.getTotalSpace();
        log.info("End API get total space");
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/store")
    public ResponseEntity<Void> store(
            @RequestBody final FileInformationRequest fileInformationRequest) {
        log.info("Start API store file");
        fileSystemService.store(fileInformationRequest.getFileName(),
                fileInformationRequest.getFileContent());
        log.info("End API store file");
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/load")
    public ResponseEntity<byte[]> load(@RequestParam final String fileName) {
        log.info("Start API load file");
        final byte[] result = fileSystemService.load(fileName);
        log.info("End API load file");
        return ResponseEntity.ok(result);
    }
}
