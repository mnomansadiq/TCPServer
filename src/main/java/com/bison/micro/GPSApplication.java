package com.bison.micro;

import com.bison.micro.health.TemplateHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class GPSApplication extends Application<GPSAppConfiguration> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "hello-world";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Bootstrap<GPSAppConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/app", "index.html",
                "assets"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(GPSAppConfiguration configuration, Environment environment)
            throws Exception {
        final String template = configuration.getTemplate();
        final String defaultName = configuration.getDefaultName();

        final GPSEndPoint resource = new GPSEndPoint(template, defaultName);
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
                template);

        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);

    }
}
