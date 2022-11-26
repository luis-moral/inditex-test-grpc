package inditex.infrastructure.util.validator;

import inditex.infrastructure.util.validator.exception.InvalidParameterException;
import inditex.infrastructure.util.validator.exception.MandatoryParameterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class RequestParameterValidatorShould {

    private RequestParameterValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new RequestParameterValidator();
    }

    @Test public void
    validate_mandatory_product_id() {
        Optional<String> valid = Optional.of("1500");
        Optional<String> invalid = Optional.of("Date");
        Optional<String> empty = Optional.empty();

        Assertions
            .assertThat(validator.mandatoryLong(valid, "productId"))
            .isEqualTo(1500);

        Assertions
            .assertThatThrownBy(() -> validator.mandatoryLong(invalid, "productId"))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessage("Parameter [productId] must be a valid number");

        Assertions
            .assertThatThrownBy(() -> validator.mandatoryLong(empty, "productId"))
            .isInstanceOf(MandatoryParameterException.class)
            .hasMessage("Parameter [productId] is mandatory");
    }

    @Test public void
    validate_mandatory_brand_id() {
        Optional<String> valid = Optional.of("1500");
        Optional<String> invalid = Optional.of("Date");
        Optional<String> empty = Optional.empty();

        Assertions
            .assertThat(validator.mandatoryInteger(valid, "brandId"))
            .isEqualTo(1500);

        Assertions
            .assertThatThrownBy(() -> validator.mandatoryInteger(invalid, "brandId"))
            .isInstanceOf(InvalidParameterException.class)
            .hasMessage("Parameter [brandId] must be a valid number");

        Assertions
            .assertThatThrownBy(() -> validator.mandatoryInteger(empty, "brandId"))
            .isInstanceOf(MandatoryParameterException.class)
            .hasMessage("Parameter [brandId] is mandatory");
    }

    @Test public void
    validate_mandatory_date() {
        ZonedDateTime date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(1668853977000L), ZoneId.of("CET"));

        Optional<String> valid = Optional.of(date.toOffsetDateTime().toString());
        Optional<String> invalid = Optional.of("Date");
        Optional<String> empty = Optional.empty();

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