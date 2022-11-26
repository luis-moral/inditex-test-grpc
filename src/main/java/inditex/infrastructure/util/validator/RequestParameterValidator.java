package inditex.infrastructure.util.validator;

import inditex.infrastructure.util.validator.exception.InvalidParameterException;
import inditex.infrastructure.util.validator.exception.MandatoryParameterException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class RequestParameterValidator {

    public long mandatoryLong(Optional<String> optionalValue, String parameterName) {
        String value = optionalValue.orElseThrow(() -> new MandatoryParameterException(parameterName));

        return validateLong(value, parameterName, "a valid number");
    }

    public int mandatoryInteger(Optional<String> optionalValue, String parameterName) {
        String value = optionalValue.orElseThrow(() -> new MandatoryParameterException(parameterName));

        return validateInteger(value, parameterName, "a valid number");
    }

    public long mandatoryDate(Optional<String> optionalValue, String parameterName) {
        String value = optionalValue.orElseThrow(() -> new MandatoryParameterException(parameterName));

        return validateDate(value, parameterName, "a valid ISO-8601 date");
    }

    private int validateInteger(String value, String errorParameter, String errorExpected) {
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            throw new InvalidParameterException(errorParameter, errorExpected);
        }
    }

    private long validateLong(String value, String errorParameter, String errorExpected) {
        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException e) {
            throw new InvalidParameterException(errorParameter, errorExpected);
        }
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
