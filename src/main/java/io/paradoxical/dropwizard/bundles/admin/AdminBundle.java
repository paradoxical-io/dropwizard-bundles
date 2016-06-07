package io.paradoxical.dropwizard.bundles.admin;

import com.godaddy.logging.Logger;
import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Builder;
import lombok.Singular;

import static com.godaddy.logging.LoggerFactory.getLogger;

@SuppressWarnings("unused")
public class AdminBundle implements ConfiguredBundle<Configuration> {
    private static final CharMatcher wildcardMatcher = CharMatcher.anyOf("/*");
    private static final Logger logger = getLogger(AdminBundle.class);

    private final ImmutableList<AdminEnvironmentConfigurator> environmentConfigurators;
    private final String adminRootPath;

    @Builder
    public AdminBundle(
        @Singular("configureEnvironment")
        final ImmutableList<AdminEnvironmentConfigurator> environmentConfigurators,
        final String adminRootPath) {

        this.environmentConfigurators = environmentConfigurators == null ? ImmutableList.of() : environmentConfigurators;
        this.adminRootPath = adminRootPath == null ? "/admin" : wildcardMatcher.trimTrailingFrom(adminRootPath);
    }

    @Override
    public void run(final Configuration configuration, final Environment environment) throws Exception {
        AdminResourceEnvironment adminResourceEnvironment = AdminResourceEnvironment.getOrCreate(environment);

        environment.admin()
                   .addServlet(AdminBundle.class.getCanonicalName(),
                               adminResourceEnvironment.jerseyContainerHolder().getContainer())
                   .addMapping(adminRootPath + "/*");

        environmentConfigurators.forEach(configure -> configure.configure(configuration, adminResourceEnvironment));

        logger.with("adminRootPath", adminRootPath).info("Setup an admin environment");
        adminResourceEnvironment.adminResourceConfig()
                                .logComponents();
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
    }
}
