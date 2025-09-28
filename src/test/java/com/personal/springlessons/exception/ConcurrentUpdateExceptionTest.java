package com.personal.springlessons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


class ConcurrentUpdateExceptionTest {

    @Test
    void testConcurrentUpdateException() {
        String testId = "abc";
        String testVersion = "5";

        ConcurrentUpdateException ex = new ConcurrentUpdateException(testId, testVersion);
        String expectedMessage =
                "Resource id abc with version 5 was updated by another transaction";

        assertEquals(expectedMessage, ex.getMessage());
        assertEquals(testId, ex.getId());
        assertEquals(testVersion, ex.getVersion());
    }
}
