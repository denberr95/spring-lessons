package com.personal.springlessons.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConstantsTest {

    @Test
    @DisplayName("Test constant class cannot be instantiated")
    void testConstantsClassCannotBeInstantiated() {
        final Constructor<Constants> constructor;
        try {
            constructor = Constants.class.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
    }
}
