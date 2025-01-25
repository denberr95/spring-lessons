package com.personal.springlessons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.lov.DomainCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.info.GitProperties;

class MethodsTest {

    static List<Object[]> provideFileNameAndTypes() {
        return Arrays
                .asList(new Object[][] {{"document.pdf", Arrays.asList("pdf", "txt", "docx"), true},
                        {"image.bmp", Arrays.asList("jpg", "png", "gif"), false},});
    }

    static List<Object[]> provideFileNameData() {
        return Arrays.asList(new Object[][] {
                {"report", ".pdf", true,
                        "report_" + Methods.dateTimeFormatter(Constants.S_DATE_TIME_FORMAT_1,
                                LocalDateTime.now()) + ".pdf"},
                {"image", ".png", false, "image.png"},});
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
    @MethodSource("provideFileNameAndTypes")
    void givenFileTypes_whenIsFileTypeValid_thenCheck(String fileName, List<String> extensions,
            boolean expectedResult) {
        assertEquals(expectedResult, Methods.isValidFileType(fileName, extensions));
    }

    @ParameterizedTest
    @MethodSource("provideFileNameData")
    void testGenerateFileName(String name, String fileExtension, boolean useTimestamp,
            String expectedResult) {
        String result = Methods.generateFileName(name, fileExtension, useTimestamp);
        assertEquals(expectedResult, result);
    }
}
