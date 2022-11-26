package inditex.infrastructure.component;

import inditex.infrastructure.util.database.MigrationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RunMigrationsOnStarted {

    @Value("${spring.flyway.url}")
    private String url;

    @Value("${spring.flyway.user}")
    private String user;

    @Value("${spring.flyway.password}")
    private String password;

    @Value("${spring.flyway.locations}")
    private String locations;

    @Value("${flyway.run-migrations:true}")
    private boolean runMigrations;

    @EventListener(ApplicationStartedEvent.class)
    private void initialize() {
        if (runMigrations) {
            new MigrationRunner().migrate(url, user, password, locations);
        }
    }
}
