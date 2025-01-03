package com.personal.springlessons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.lov.DomainCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.info.GitProperties;

class MethodsTest {

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

    @Test
    void givenValidIdAndLastModifiedTime_whenCalculateETag_thenReturnExpectedETag()
            throws NoSuchAlgorithmException {
        String id = "12345";
        OffsetDateTime lastModifiedTime = OffsetDateTime.parse("9999-12-01T10:00:00+01:00");
        String baseString = "%s-%s".formatted(id, lastModifiedTime);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(baseString.getBytes());
        String expectedETag = "\"" + Base64.getEncoder().encodeToString(hash) + "\"";

        String actualETag = Methods.calculateETag(id, lastModifiedTime);

        assertEquals(expectedETag, actualETag);
    }

    @Test
    void givenDifferentIdAndLastModifiedTime_whenCalculateETag_thenReturnDifferentETag()
            throws NoSuchAlgorithmException {
        String id1 = "12345";
        OffsetDateTime lastModifiedTime1 = OffsetDateTime.parse("9999-12-01T10:00:00+01:00");
        String id2 = "67890";
        OffsetDateTime lastModifiedTime2 = OffsetDateTime.parse("9999-12-31T10:00:00+01:00");

        String eTag1 = Methods.calculateETag(id1, lastModifiedTime1);
        String eTag2 = Methods.calculateETag(id2, lastModifiedTime2);

        assertNotEquals(eTag1, eTag2);
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
        assertEquals(DomainCategory.ND, actual);
    }

    @Test
    void givenDateTimeFormat_whenDateTimeTransformer_thenReturnDateTimeFormatter() {
        String format = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime ldt = LocalDateTime.of(9999, 12, 31, 12, 30, 00);
        String expected = "9999-12-31 12:30:00";
        String actual = Methods.dateTimeFormatter(format, ldt);
        assertEquals(expected, actual);
    }

    @Test
    void givenInvalidAlgorithm_whenCalculateETag_thenThrowNoSuchAlgorithmException() {
        assertThrows(NoSuchAlgorithmException.class, () -> {
            MessageDigest.getInstance("INVALID_ALGORITHM");
        });
    }
}
