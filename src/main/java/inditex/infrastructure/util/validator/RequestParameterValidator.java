package inditex.infrastructure.util.validator;

import inditex.infrastructure.util.validator.exception.InvalidParameterException;
import inditex.infrastructure.util.validator.exception.MandatoryParameterException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class RequestParameterValidator {

    public long mandatoryDate(String value, String parameterName) {
        if (value.isEmpty()) {
            throw new MandatoryParameterException(parameterName);
        }

        return validateDate(value, parameterName, "a valid ISO-8601 date");
    }

    private long validateDate(String value, String errorParameter, String errorExpected) {
        try {
            return ZonedDateTime.parse(value).toInstant().toEpochMilli();
        }
        catch (DateTimeParseException e) {
            throw new InvalidParameterException(errorParameter, errorExpected);
        }
    }
}
