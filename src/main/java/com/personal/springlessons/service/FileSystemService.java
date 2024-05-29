package com.personal.springlessons.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileSystemService {

    private final Path root = Paths.get("/tmp").resolve("fs");

    public FileSystemService() {
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
        return root.toFile().getFreeSpace();
    }

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
