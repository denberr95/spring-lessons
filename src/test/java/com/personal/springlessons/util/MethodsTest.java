package com.personal.springlessons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.lov.DomainCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.info.GitProperties;

class MethodsTest {

    static Stream<Arguments> methodSourceFileNameData() {
        String formattedDate =
                Methods.dateTimeFormatter(Constants.S_DATE_TIME_FORMAT_1, LocalDateTime.now());
        return Stream.of(Arguments.of("report", ".pdf", true, "report_" + formattedDate + ".pdf"),
                Arguments.of("image", ".png", false, "image.png"));
    }

    static Stream<Arguments> methodSourceCheckFileType() {
        return Stream.of(Arguments.of("document.pdf", List.of(".pdf", ".txt"), true),
                Arguments.of("report.txt", List.of(".pdf", ".txt"), true),
                Arguments.of("image.jpg", List.of(".pdf", ".txt"), false),
                Arguments.of("archive.zip", List.of(".zip"), true),
                Arguments.of("presentation.ppt", List.of(), false));
    }

    @Test
    void testMethodsClassCannotBeInstantiated() {
        final Constructor<Methods> constructor;
        try {
            constructor = Methods.class.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @CsvSource({"1.0.0, v1.0.0", ", vnull", "'', v"})
    void givenGitProperties_whenGetApplicationVersion_thenReturnFormattedVersion(
            String buildVersion, String expectedVersion) {
        GitProperties gitProperties = mock(GitProperties.class);
        when(gitProperties.get("build.version")).thenReturn(buildVersion);
        String version = Methods.getApplicationVersion(gitProperties);
        assertEquals(expectedVersion, version);
    }

    @Test
    void givenValidId_whenIdValidation_thenReturnValidUUID() {
        UUID actual = UUID.randomUUID();
        UUID expected = Methods.idValidation(actual.toString());
        assertEquals(expected, actual);
    }

    @Test
    void givenInvalidId_whenIdValidation_thenThrowInvalidUUIDException() {
        assertThrows(InvalidUUIDException.class, () -> {
            Methods.idValidation("fakeId");
        });
    }

    @Test
    void giveValidUri_whenRetrieveDomainCategory_thenReturnSpecificDomainCategory() {
        DomainCategory actual = Methods.retrieveDomainCategory("/spring-app/books");
        assertEquals(DomainCategory.BOOKS, actual);
    }

    @Test
    void givenInvalidUri_whenRetrieveDomainCategory_thenReturnDefaultDomainCategory() {
        DomainCategory actual = Methods.retrieveDomainCategory("/fake-uri");
        assertEquals(DomainCategory.NA, actual);
    }

    @Test
    void givenDateTimeFormat_whenDateTimeTransformer_thenReturnDateTimeFormatter() {
        String format = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime ldt = LocalDateTime.of(9999, 12, 31, 12, 30, 00);
        String expected = "9999-12-31 12:30:00";
        String actual = Methods.dateTimeFormatter(format, ldt);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("methodSourceFileNameData")
    void givenFileMetadata_whenGenerateFileName_thenReturnFileName(String name,
            String fileExtension, boolean useTimestamp, String expectedResult) {
        String result = Methods.generateFileName(name, fileExtension, useTimestamp);
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("methodSourceCheckFileType")
    void givenFileName_whenCheckIsValid_thenReturnIsValid(String fiileName, List<String> fileTypes,
            boolean expectedResult) {
        boolean actual = Methods.isValidFileType(fiileName, fileTypes);
        assertEquals(expectedResult, actual);
    }
}
