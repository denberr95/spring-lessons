package com.personal.springlessons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.lov.DomainCategory;
import org.junit.jupiter.api.Test;

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
        assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
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
        DomainCategory actual = Methods.retrieveDomainCategory( "/spring-app/books");
        assertEquals(DomainCategory.BOOKS, actual);
    }

    @Test
    void givenInvalidUri_whenRetrieveDomainCategory_thenReturnDefaultDomainCategory() {
        DomainCategory actual = Methods.retrieveDomainCategory( "/fake-uri");
        assertEquals(DomainCategory.ND, actual);
    }
}
