package com.personal.springlessons.util;

import java.util.UUID;
import com.personal.springlessons.exception.InvalidUUIDException;
import com.personal.springlessons.model.lov.DomainCategory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Methods {

    private Methods() {
        throw new UnsupportedOperationException(
                "This is a utility class and cannot be instantiated");
    }

    public static UUID idValidation(String id) {
        log.debug("UUID to validate: '{}'", id);
        UUID result = null;
        try {
            result = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(id);
        }
        log.debug("UUID: '{}' is valid", id);
        return result;
    }

    public static DomainCategory retrieveDomainCategory(String uri) {
        log.debug("Check Domain Category from uri: '{}'", uri);
        DomainCategory result = DomainCategory.ND;
        for (DomainCategory e : DomainCategory.values()) {
            if (uri.toLowerCase().contains(e.getValue().toLowerCase())) {
                result = e;
            }
        }
        log.debug("Domain Category retrieved: '{}'", result);
        return result;
    }
}
