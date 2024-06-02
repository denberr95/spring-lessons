package com.personal.springlessons.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileSystemService {

    private final Path root = Paths.get("/tmp").resolve("fs");
    private final Tracer tracer;

    public FileSystemService(final Tracer tracer) {
        this.tracer = tracer;
        log.debug("Create file system !");
        try {
            if (!Files.isDirectory(root)) {
                Files.createDirectories(root);
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public long getFreeDiskSpace() {
        long result = 0L;
        final Span newSpan = tracer.nextSpan(tracer.currentSpan()).name("freeDiskSpaceService");
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan.start())) {
            result = root.toFile().getFreeSpace();
            newSpan.tag("key", "value");
            newSpan.event("event");
        } finally {
            newSpan.end();
        }
        return result;
    }

    @NewSpan(value = "totalSpaceService")
    public long getTotalSpace() {
        return root.toFile().getTotalSpace();
    }

    public byte[] load(final String fileName) {
        byte[] result = null;
        try {
            result = Files.readAllBytes(root.resolve(fileName));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    public void store(final String fileName, final byte[] content) {
        try {
            Files.write(root.resolve(fileName), content);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
