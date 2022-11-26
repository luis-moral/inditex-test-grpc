package inditex.infrastructure.util.url;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UrlEncoderShould {

    @Test public void
    url_encode_utf8_strings() {
        String expected = "so%2BV%3Flu+e";

        Assertions
            .assertThat(UrlEncoder.encode("so+V?lu e"))
            .isEqualTo(expected);
    }

    @Test public void
    url_decode_utf8_strings() {
        String expected = "so+V?lu e";

        Assertions
            .assertThat(UrlEncoder.decode("so%2BV%3Flu+e"))
            .isEqualTo(expected);
    }
}