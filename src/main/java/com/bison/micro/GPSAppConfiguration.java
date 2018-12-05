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

    @NotEmpty
    @JsonProperty
    private String jmsHost;

    @NotEmpty
    @JsonProperty
    private String jmsPort;
    
    @NotEmpty
    @JsonProperty
    private String jmsQueueName;

    @NotEmpty
    @JsonProperty
    private String jmsQueueRecordKey;

    public String getJmsHost() {
        return jmsHost;
    }

    public String getJmsPort() {
        return jmsPort;
    }

    public String getTcpServerPort() {
        return tcpServerPort;
    }
    public String getTemplate() {
        return template;
    }
    public String getJmsQueueName() {
        return jmsQueueName;
    }

    public String getJmsQueueRecordKey() {
        return jmsQueueRecordKey;
    }

}
