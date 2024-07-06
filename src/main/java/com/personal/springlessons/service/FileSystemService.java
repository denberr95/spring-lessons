package com.personal.springlessons.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.personal.springlessons.config.AppPropertiesConfig;
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

    private final AppPropertiesConfig appPropertiesConfig;
    private final Tracer tracer;
    private Path root = null;

    /**
     * Constructor that initializes the file system service and creates the root directory if it
     * does not exist.
     *
     * @param tracer the tracer for managing tracing and spans.
     * @param appPropertiesConfig the application properties configurations.
     * @throws UncheckedIOException if an error occurs while creating the directory.
     */
    public FileSystemService(final Tracer tracer, final AppPropertiesConfig appPropertiesConfig) {
        this.tracer = tracer;
        this.appPropertiesConfig = appPropertiesConfig;
        log.debug("Create file system !");
        this.root = Paths.get(this.appPropertiesConfig.getDefaultPath());
        try {
            if (!Files.isDirectory(this.root)) {
                Files.createDirectories(this.root);
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Retrieves the available free disk space.
     *
     * @return the amount of free space in bytes.
     * 
     *         This method can be used to monitor the available free disk space of the file system
     *         managed by this class.
     */
    public long getFreeDiskSpace() {
        long result = 0L;
        final Span newSpan =
                this.tracer.nextSpan(this.tracer.currentSpan()).name("freeDiskSpaceService");
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

    /**
     * Retrieves the total disk space.
     *
     * @return the total space in bytes.
     * 
     *         This method can be used to get the total size of the disk of the file system managed
     *         by this class.
     */
    @ContinueSpan(log = "total.space.service")
    public long getTotalSpace() {
        return this.root.toFile().getTotalSpace();
    }

    /**
     * Loads the content of a specified file.
     *
     * @param fileName the name of the file to load.
     * @return a byte array containing the file's content.
     * @throws UncheckedIOException if an error occurs while reading the file.
     * 
     *         This method can be used to read data from a specified file in the file system managed
     *         by this class.
     */
    @NewSpan
    public byte[] load(@SpanTag("fileName") final String fileName) {
        byte[] result = null;
        try {
            result = Files.readAllBytes(this.resolve(fileName));
            this.tracer.currentSpan().tag("number.of.bytes", result.length);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return result;
    }

    /**
     * Stores content in a specified file.
     *
     * @param fileName the name of the file to store the content.
     * @param content a byte array containing the content to store.
     * @throws UncheckedIOException if an error occurs while writing the file.
     * 
     *         This method can be used to write data to a specified file in the file system managed
     *         by this class.
     */
    @NewSpan
    public void store(@SpanTag("file") final String fileName, final byte[] content) {
        this.tracer.currentSpan().event("Storing file");
        try {
            final Path result = Files.write(this.resolve(fileName), content);
            this.tracer.currentSpan().tag("file.size", Files.size(result));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        this.tracer.currentSpan().event("File stored");
    }

    /**
     * Resolves the absolute path of a specified file and checks if it is within the root directory.
     *
     * @param fileName the name of the file to resolve.
     * @return the absolute and normalized path of the file.
     * @throws SecurityException if the resolved file path is outside the root directory.
     * 
     *         This method is used internally to ensure that accessed files are within the root
     *         directory managed by this class.
     */
    private Path resolve(final String fileName) {
        final Path result = this.root.resolve(fileName).toAbsolutePath().normalize();
        if (!result.startsWith(this.root)) {
            throw new SecurityException("Access to path " + result + " denied !");
        }
        return result;
    }
}
