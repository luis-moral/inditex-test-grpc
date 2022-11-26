## Technical Test

- [Requirements](TECHNICAL_TEST.md)

- [OpenApi definition](doc/open-api/price.yml)

- [JMeter test](src/test/jmeter/inditex-test.jmx)

- [Notes](NOTES.md)

---

## Compile Application

```
./mvnw clean compile
```

---

## Run Tests

```
./mvnw clean test
```

---

## Start Application

```
./mvnw clean spring-boot:run
```

You can test the endpoint in any browser at http://localhost:8081/publc/v1/price, using the [OpenApi definition](doc/open-api/price.yml) or the [JMeter test](src/test/jmeter/inditex-test.jmx). 

Example request:

http://localhost:8081/public/v1/price?productId=35455&brandId=1&date=2020-06-14T10%253A00%253A00%252B02%253A00