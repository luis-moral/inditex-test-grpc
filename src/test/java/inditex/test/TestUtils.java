package inditex.test;

import inditex.infrastructure.util.database.MigrationRunner;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    private TestUtils() {}

    public static String readFile(String path) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + path).toPath(), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static ConnectionFactory initDatabase(String schema) {
        return initDatabase(schema, null);
    }

    public static ConnectionFactory initDatabase(String schema, @Nullable String sqlScript) {
        new MigrationRunner().migrate("jdbc:h2:mem:////" + schema + ";DB_CLOSE_DELAY=-1;", "sa", "", "classpath:db/migration");

        H2ConnectionFactory connectionFactory =
            new H2ConnectionFactory(
                H2ConnectionConfiguration
                    .builder()
                    .url("mem:////" + schema + ";DB_CLOSE_DELAY=-1;")
                    .username("sa")
                    .password("")
                    .build()
            );

        if (sqlScript != null && !sqlScript.isEmpty()) {
            executeSql(connectionFactory, sqlScript);
        }

        return connectionFactory;
    }

    public static void executeSql(ConnectionFactory connectionFactory, String path) {
        LOGGER.info("Executing script=[{}] for datasource=[{}]", path, connectionFactory);

        Resource sqlFile = new ClassPathResource(path);

        Mono
            .from(connectionFactory.create())
            .doOnNext(
                connection -> {
                    connection.setAutoCommit(true);
                    ScriptUtils.executeSqlScript(connection, sqlFile);
                }
            )
            .block();

        LOGGER.info("Script executed");
    }
}
