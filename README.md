## Technical Test

- [Requirements](TECHNICAL_TEST.md)

- [JMeter test](src/test/jmeter/inditex-test-grpc.jmx)

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

You can test the endpoint using the [JMeter test](src/test/jmeter/inditex-test-grpc.jmx).