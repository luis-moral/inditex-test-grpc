package inditex.infrastructure.util.validator;

import inditex.infrastructure.util.validator.exception.InvalidParameterException;
import inditex.infrastructure.util.validator.exception.MandatoryParameterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class RequestParameterValidatorShould {

    private RequestParameterValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new RequestParameterValidator();
    }

    @Test public void
    validate_mandatory_date() {
        ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(1668853977000L), ZoneId.of("CET"));

        String valid = date.toOffsetDateTime().toString();
        String invalid = "Date";
        String empty = "";

        Assertions
            .assertThat(validator.mandatoryDate(valid, "date"))
            .isEqualTo(1668853977000L);

        Assertions
            .assertThatThrownBy(() -> validator.mandatoryDate(invalid, "date"))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessage("Parameter [date] must be a valid ISO-8601 date");

        Assertions
            .assertThatThrownBy(() -> validator.mandatoryDate(empty, "date"))
            .isInstanceOf(MandatoryParameterException.class)
            .hasMessage("Parameter [date] is mandatory");
    }
}