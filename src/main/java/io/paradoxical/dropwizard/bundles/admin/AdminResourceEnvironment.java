package io.paradoxical.dropwizard.bundles.admin;

import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.annotation.Nonnull;

@Getter
@Accessors(fluent = true)
public final class AdminResourceEnvironment {
    private final DropwizardResourceConfig adminResourceConfig;
    private final JerseyContainerHolder jerseyContainerHolder;
    private final JerseyEnvironment jerseyEnvironment;
    private final Environment environment;

    private AdminResourceEnvironment(@NonNull @Nonnull final Environment environment) {
        this.environment = environment;
        adminResourceConfig = new DropwizardResourceConfig(environment.metrics());
        jerseyContainerHolder = new JerseyContainerHolder(new ServletContainer(adminResourceConfig));
        jerseyEnvironment = new JerseyEnvironment(jerseyContainerHolder, adminResourceConfig);
    }

    public static AdminResourceEnvironment getOrCreate(@NonNull @Nonnull final Environment environment) {
        final AdminResourceEnvironment currentEnvironment = environment.getAdminContext().getBean(AdminResourceEnvironment.class);
        if(currentEnvironment != null) {
            return currentEnvironment;
        }

        final AdminResourceEnvironment adminResourceEnvironment = new AdminResourceEnvironment(environment);

        environment.getAdminContext().addBean(adminResourceEnvironment);

        return adminResourceEnvironment;
    }
}