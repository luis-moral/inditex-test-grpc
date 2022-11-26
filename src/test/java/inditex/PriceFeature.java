package inditex;

import inditex.infrastructure.grpc.price.get.GetPriceGrpc;
import inditex.infrastructure.grpc.price.get.GetPriceRequest;
import inditex.infrastructure.grpc.price.get.GetPriceResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ActiveProfiles(profiles = "test")
@SpringBootTest(classes = { Application.class })
public class PriceFeature {

	@Value("${server.port}")
	private int serverPort;

	private ManagedChannel channel;

	@BeforeEach
	public void setUp() {
		channel =
			ManagedChannelBuilder
				.forAddress("localhost", serverPort)
				.usePlaintext()
				.build();
	}

	@Test void
	return_the_price_for_a_product_in_a_time_period() {
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 14, 10, 0, 0, 0, ZoneId.of("CET"))),
			response(1, 35455, 1, "2020-06-14T00:00+02:00", "2020-12-31T23:59:59+01:00", 35.5, "EUR")
		);
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 14, 16, 0, 0, 0, ZoneId.of("CET"))),
			response(2, 35455, 1, "2020-06-14T15:00+02:00", "2020-06-14T18:30+02:00", 25.45, "EUR")
		);
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 14, 21, 0, 0, 0, ZoneId.of("CET"))),
			response(1, 35455, 1, "2020-06-14T00:00+02:00", "2020-12-31T23:59:59+01:00", 35.5, "EUR")
		);
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 15, 10, 0, 0, 0, ZoneId.of("CET"))),
			response(3, 35455, 1, "2020-06-15T00:00+02:00", "2020-06-15T11:00+02:00", 30.5, "EUR")
		);
		assertPriceFor(
			new PriceFilter(35455, 1, ZonedDateTime.of(2020, 6, 16, 21, 0, 0, 0, ZoneId.of("CET"))),
			response(4, 35455, 1, "2020-06-15T16:00+02:00", "2020-12-31T23:59:59+01:00", 38.95, "EUR")
		);

	}

	@Test public void
	return_default_when_no_prices() {
		GetPriceGrpc.GetPriceBlockingStub getPriceStub = GetPriceGrpc.newBlockingStub(channel);

		GetPriceRequest request =
			GetPriceRequest
				.newBuilder()
				.setProductId(1)
				.setBrandId(2)
				.setDate("2020-06-15T00:00+02:00")
				.build();

		Assertions
			.assertThat(getPriceStub.price(request))
			.isEqualTo(GetPriceResponse.getDefaultInstance());
	}

	@Test public void
	error_when_missing_parameters() {
		GetPriceGrpc.GetPriceBlockingStub getPriceStub = GetPriceGrpc.newBlockingStub(channel);

		GetPriceRequest request =
			GetPriceRequest
				.newBuilder()
				.setProductId(1)
				.setBrandId(2)
				.setDate("")
				.build();

		Assertions
			.assertThatThrownBy(() -> getPriceStub.price(request))
			.isInstanceOf(StatusRuntimeException.class)
			.hasMessage("INVALID_ARGUMENT: Parameter [date] is mandatory");

	}

	private void assertPriceFor(PriceFilter parameters, GetPriceResponse expected) {
		GetPriceGrpc.GetPriceBlockingStub getPriceStub = GetPriceGrpc.newBlockingStub(channel);
		GetPriceRequest request =
			GetPriceRequest
				.newBuilder()
				.setProductId(parameters.productId())
				.setBrandId(parameters.brandId())
				.setDate(parameters.date().toOffsetDateTime().toString())
				.build();

		Assertions
			.assertThat(getPriceStub.price(request))
			.isEqualTo(expected);
	}

	private GetPriceResponse response(
		long id,
		long productId,
		int brandId,
		String startDate,
		String endDate,
		double price,
		String currency
	) {
		return
			GetPriceResponse
				.newBuilder()
				.setId(id)
				.setProductId(productId)
				.setBrandId(brandId)
				.setStartDate(startDate)
				.setEndDate(endDate)
				.setPrice(price)
				.setCurrency(currency)
				.build();
	}

	private record PriceFilter(long productId, int brandId, ZonedDateTime date) {
	}
}
