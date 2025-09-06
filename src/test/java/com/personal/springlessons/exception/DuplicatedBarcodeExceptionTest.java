package com.personal.springlessons.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DuplicatedBarcodeExceptionTest {

    @Test
    void testDuplicatedItemException() {
        String barcode = "Test Item";
        String id = "12345";
        DuplicatedBarcodeException exception = new DuplicatedBarcodeException(barcode, id);
        String expectedMessage =
                String.format("Item '%s' already exists with id '%s'", barcode, id);
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(barcode, exception.getBarcode());
        assertEquals(id, exception.getId());
    }
}
