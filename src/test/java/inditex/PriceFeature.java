package inditex;

import inditex.infrastructure.util.url.UrlEncoder;
import inditex.test.TestUtils;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
	classes = { Application.class }
)
public class PriceFeature {

	@Value("${endpoint.public.v1.price.path.base}")
	private String priceEndpoint;

	@Autowired
	private WebTestClient webClient;

	@Test void
	return_the_price_for_a_product_in_a_time_period() {
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 14, 10, 0, 0, 0, ZoneId.of("CET"))),
			"feature/price/response/expected_14_06_2020-10_00.json"
		);
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 14, 16, 0, 0, 0, ZoneId.of("CET"))),
			"feature/price/response/expected_14_06_2020-16_00.json"
		);
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 14, 21, 0, 0, 0, ZoneId.of("CET"))),
			"feature/price/response/expected_14_06_2020-21_00.json"
		);
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 15, 10, 0, 0, 0, ZoneId.of("CET"))),
			"feature/price/response/expected_15_06_2020-10_00.json"
		);
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 16, 21, 0, 0, 0, ZoneId.of("CET"))),
			"feature/price/response/expected_16_06_2020-21_00.json"
		);

	}

	@Test public void
	error_when_missing_parameters() {
		webClient
			.get()
			.uri(priceEndpoint)
			.exchange()
			.expectStatus()
			.isEqualTo(HttpStatus.BAD_REQUEST)
			.expectBody(HashMap.class)
			.consumeWith(response ->
				{
					Map body = response.getResponseBody();

					Assertions
						.assertThat(body.get("status"))
						.isEqualTo(400);

					Assertions
						.assertThat(body.get("error"))
						.isEqualTo("Parameter [productId] is mandatory");
				}
			);
	}

	private void assertPriceFor(PriceFilter parameters, String expected) {
		webClient
			.get()
			.uri(builder ->
				builder
					.path(priceEndpoint)
					.queryParam("productId", parameters.productId())
					.queryParam("brandId", parameters.brandId())
					.queryParam("date", UrlEncoder.encode(parameters.date().toOffsetDateTime().toString()))
					.build()
			)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(String.class)
			.consumeWith(response ->
				{
					try {
						JSONAssert
							.assertEquals(
								TestUtils.readFile(expected),
								response.getResponseBody(),
								JSONCompareMode.LENIENT
							);
					}
					catch (JSONException e) {
						throw new RuntimeException(e);
					}
				}
			);
	}

	private record PriceFilter(long productId, int brandId, ZonedDateTime date) {
	}
}
