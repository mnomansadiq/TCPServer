package com.bison.jms;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.impl.FileConfiguration;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.impl.JMSServerManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EmbeddedJMSServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedJMSServer.class);
    private String jmsHostName;
    private String jmsHostPort;

    public EmbeddedJMSServer(String jmsHostName, String jmsHostPort) {
        
        this.jmsHostName = jmsHostName;
        this.jmsHostPort = jmsHostPort;
    }

    public void startServer()
    {
        try
        {
            FileConfiguration configuration = new FileConfiguration();
            configuration.setConfigurationUrl("hornetq-configuration.xml");
            configuration.start();
            configuration.setPersistenceEnabled(false);
            configuration.setSecurityEnabled(false);

            Map<String, Object> map = new HashMap<>();
            map.put("host", this.jmsHostName);
            map.put("port", this.jmsHostPort);

            TransportConfiguration transpConf = new TransportConfiguration(NettyAcceptorFactory.class.getName(), map);

            HashSet<TransportConfiguration> setTransp = new HashSet<TransportConfiguration>();
            setTransp.add(transpConf);

            configuration.setAcceptorConfigurations(setTransp);
            HornetQServer server = HornetQServers.newHornetQServer(configuration);


            JMSServerManager jmsServerManager = new JMSServerManagerImpl(server, "hornetq-jms.xml");
            //if you want to use JNDI, simple inject a context here or don't call this method and make sure the JNDI parameters are set.
            jmsServerManager.setContext(null);
            jmsServerManager.start();
            LOGGER.info("JMS Server started !!");

            setupProducer();
        }
        catch (Throwable e)
        {
            LOGGER.error("Error while starting JMS server", e);
        }
    }

    private void setupProducer() {
        TrackerMessageProducer.getInstance().setupProducer(this.jmsHostName, this.jmsHostPort);
        
    }
}
