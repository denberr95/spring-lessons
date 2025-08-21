package com.personal.springlessons.model.dto.external;

import java.time.Instant;

public record AccountDTO(String id, String name, String surname, String nationality, String email,
        String phone, String address, String job, Instant createdAt) {
}
