package io.paradoxical.dropwizard.bundles.admin;

import io.dropwizard.Configuration;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface AdminEnvironmentConfigurator {
    void configure(Configuration config, AdminResourceEnvironment adminResourceEnvironment);

    static AdminEnvironmentConfigurator simple(final Consumer<AdminResourceEnvironment> adminEnvironmentConsumer) {

        return (config, adminEnv) -> adminEnvironmentConsumer.accept(adminEnv);
    }

    static AdminEnvironmentConfigurator forJersey(final JerseyEnvironmentConfigurator jerseyEnvironmentConfigurator) {

        return simple(adminEnv -> jerseyEnvironmentConfigurator.accept(adminEnv.environment(), adminEnv.jerseyEnvironment()));
    }

    @FunctionalInterface
    interface JerseyEnvironmentConfigurator extends BiConsumer<Environment, JerseyEnvironment> {
    }
}
