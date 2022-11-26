package inditex.infrastructure.util.database;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigrationRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void migrate(String url, String user, String password, String...locations) {
        logger.info("Running migrations for [{}]", url);

        Flyway flyway =
            Flyway
                .configure()
                .dataSource(url, user, password)
                .validateMigrationNaming(true)
                .locations(locations)
                .createSchemas(true)
                .load();
        flyway.migrate();

        logger.info("Migrations completed for [{}]", url);
    }
}
