package com.personal.springlessons.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
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
            if (!Files.isDirectory(this.root)) {
                Files.createDirectories(this.root);
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public long getFreeDiskSpace() {
        long result = 0L;
        final Span newSpan = this.tracer.nextSpan(this.tracer.currentSpan()).name("freeDiskSpaceService");
        try (Tracer.SpanInScope ws = this.tracer.withSpan(newSpan.start())) {
            newSpan.event("Free space calculation");
            result = this.root.toFile().getFreeSpace();
            newSpan.tag("free.space", result);
            newSpan.event("Free space calculated");
        } finally {
            newSpan.end();
        }
        return result;
    }

    @ContinueSpan(log = "total.space.service")
    public long getTotalSpace() {
        try {
            TimeUnit.MILLISECONDS.sleep(5000L);
            this.tracer.currentSpan().tag("Sleep", false);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(e.getMessage(), e);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return this.root.toFile().getTotalSpace();
    }

    @NewSpan
    public byte[] load(@SpanTag("fileName") final String fileName) {
        byte[] result = null;
        try {
            result = Files.readAllBytes(this.root.resolve(fileName));
            this.tracer.currentSpan().tag("number.of.bytes", result.length);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    @NewSpan
    public void store(@SpanTag("file") final String fileName, final byte[] content) {
        this.tracer.currentSpan().event("Storing file");
        try {
            final Path result = Files.write(this.root.resolve(fileName), content);
            this.tracer.currentSpan().tag("file.size", Files.size(result));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        this.tracer.currentSpan().event("File stored");
    }
}
