package com.bison.micro;

import com.bison.micro.health.TemplateHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GPSApplication extends Application<GPSAppConfiguration> {
    private static final Logger log = LoggerFactory.getLogger(GPSApplication.class);
    private GPSAppConfiguration configuration;
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
        this.configuration = configuration;
        final String template = configuration.getTemplate();

        final GPSEndPoint resource = new GPSEndPoint(template);
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
                template);

        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

    public GPSAppConfiguration getConfiguration() {
        return configuration;
    } 
}
