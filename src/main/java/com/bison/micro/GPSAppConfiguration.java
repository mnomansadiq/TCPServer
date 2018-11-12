package com.bison.micro;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class GPSAppConfiguration extends Configuration {

    @NotEmpty
    @JsonProperty
    private String template;

    @NotEmpty
    @JsonProperty
    private String tcpServerPort;

    public String getTcpServerPort() {
        return tcpServerPort;
    }
    public String getTemplate() {
        return template;
    }

}
