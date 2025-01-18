package com.personal.springlessons.component.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.personal.springlessons.model.lov.Genre;
import com.personal.springlessons.util.Constants;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class MappingUtils {

    @Named("stringToLocalDate")
    public LocalDate stringToLocalDate(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.S_DATE_FORMAT_1);
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @Named("stringToInteger")
    public Integer stringToInteger(String numberString) {
        if (numberString == null || numberString.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(numberString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Named("stringToEnum")
    public <E extends Enum<E>> E stringToEnum(Class<E> enumClass, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Named("stringToGenre")
    public Genre stringToGenre(String value) {
        return this.stringToEnum(Genre.class, value);
    }
}
